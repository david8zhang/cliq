var AWS = require('aws-sdk');
var sha1 = require('sha-1');
var docClient = new AWS.DynamoDB.DocumentClient({ region: 'us-west-2' });

/** POST request to create a new party. */
exports.createParty = function(req, res) {
	var user_id = req.body.user_id;
	var party_id = sha1(Math.floor(Date.now() / 1000).toString());
	var party_name = req.body.party_name;
	var party_description = req.body.party_description;
	var party_members = docClient.createSet([user_id]);
	var params = {};
	params.TableName = 'cliq-parties';
	params.Item = {
		party_id: party_id,
		party_name: party_name,
		party_description: party_description,
		party_members: party_members
	};
	docClient.put(params, function(err, data) {
		if(err) {
			res.send(err);
		} else {
			response = {};
			response.party_id = party_id;
			res.status(200).send(response);
		}
	})
}

/** Get the party based on the party id. */
exports.getParty = function(req, res) {
	var party_id = req.query.party_id;
	params = {};
	params.TableName = 'cliq-parties';
	params.Key = {
		party_id: party_id
	}
	docClient.get(params, function(err, data) {
		if(err)
			res.send(err);
		else
			res.json(data);
	});
}

/** Add a user to the party. */
exports.addUser = function(req, res) {
	var party_id = req.body.party_id;
	var party_member_id = docClient.createSet([req.body.party_member_id]);
	params = {};
	params.Key = {
		party_id: party_id
	}
	params.TableName = 'cliq-parties';
	params.UpdateExpression = "ADD party_members :party_member";
	params.ExpressionAttributeValues = {
		":party_member" : party_member_id
	};
	docClient.update(params, function(err, data) {
		if(err)
			res.send(err);
		else {
			res.status(200).send("successfully added party_member");
		}
	})
}

/** Remove a user from the party. */
exports.deleteUser = function(req, res) {
	var party_id = req.body.party_id;
	var party_member_id = docClient.createSet([req.body.party_member_id]);
	params = {};
	params.Key = {
		party_id: party_id
	}
	params.TableName = 'cliq-parties';
	params.UpdateExpression = "DELETE party_members :party_member";
	params.ExpressionAttributeValues = {
		":party_member": party_member_id
	};
	docClient.update(params, function(err, data) {
		if(err) 
			res.send(err);
		res.status(200).send('successfully deleted party_member');
	});
}