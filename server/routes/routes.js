var express = require('express');
var router = express.Router();
var userController = require('../controllers/users.js');
var partyController = require('../controllers/parties.js');

//API Endpoints
router.route('/users/register')
	.post(userController.createUser);

router.route('/users/login')
	.post(userController.authUser);

router.route('/users/index')
	.get(userController.getUser);

router.route('/users/update_user')
	.post(userController.updateUser);

router.route('/users/update_pass')
	.post(userController.updatePass);

router.route('/party/create')
	.post(partyController.createParty);

router.route('/party/index')
	.get(partyController.getParty);

router.route('/party/adduser')
	.post(partyController.addUser);

router.route('/party/deleteuser')
	.post(partyController.deleteUser);

module.exports = router;