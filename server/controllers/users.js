var AWS = require('aws-sdk');
var sha1 = require('sha-1');
var docClient = new AWS.DynamoDB.DocumentClient({ region: 'us-west-2' });

/** GET the user based on the id. */
exports.getUser = function(req, res) {
	var user_id = req.query.user_id;
	params = {};
	params.TableName = 'cliq_users';
	params.FilterExpression = "user_id = :user_id";
	params.ExpressionAttributeValues = {
		":user_id" : user_id
	};
	docClient.scan(params, function(err, data) {
		if(err) {
			res.send(err);
		} else {
			res.json(data);
		}
	})
};

/** POST a new user to the database. */
exports.createUser = function(req, res) {
	var user_id = sha1(Math.floor(Date.now() / 1000).toString());
	var username = req.body.username;
	var email = req.body.email;
	var password = req.body.password;
	var params = {};
	params.TableName = 'cliq_users';
	params.Item = {
		user_id: user_id,
		username: username,
		email: email,
		password: password
	}
	if(req.body.reg_token !== null) {
		params.reg_token = req.body.reg_token
	}
	docClient.put(params, function(err, data) {
		if(err) {
			res.send(err);
		} else {
			res.status(200).send("Success! User was created!");
		}
	})
};

/** POST login information. */
exports.authUser = function(req, res) {
	var username = req.body.username;
	var password = req.body.password;
	var params = {};
	params.TableName = 'cliq_users';
	params.FilterExpression = "password = :password AND username=:username";
	params.ExpressionAttributeValues = {
		":username" : username,
		":password" : password
	}
	docClient.scan(params, function(err, data) {
		if(err) {
			res.send(err);
		} else {
			var response = {};
			response.user_id = data.Items[0].user_id;
			res.send(response);
		}
	})
};

exports.updateUser = function(req, res) {
	var user_id = req.body.user_id;
	var username = req.body.username;
	var params = {};
	params.Key = {
		user_id: user_id
	};
	params.TableName = 'cliq_users';
	params.UpdateExpression = "set username = :username";
	params.ExpressionAttributeValues = {
		":username" : username
	}
	docClient.update(params, function(err, data) {
		if(err) {
			res.send(err);
		} else {
			res.status(200).send("Successfully updated username");
		}
	})
}

exports.updatePass = function(req, res) {
	var password = req.body.password;
	var user_id = req.body.user_id;
	var params = {};
	params.Key = {
		user_id: user_id
	};
	params.TableName = 'cliq_users';
	params.UpdateExpression = "set password = :password";
	params.ExpressionAttributeValues = {
		":password" : password
	}
	docClient.update(params, function(err, data) {
		if(err) {
			res.send(err);
		} else {
			res.status(200).send("Successfully updated password");
		}
	})
}