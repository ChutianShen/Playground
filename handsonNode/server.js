var app = require('express')();

var http = require('http').Server(app);

var io = require('socket.io')(http);

var admin = require("firebase-admin");

var firbaseCredential = require(__dirname + '/private/serviceCredential.json');

admin.initializeApp({
  credential: admin.credential.cert(firbaseCredential),
  databaseURL: "https://beastchat-7e7a7.firebaseio.com"
});

var accountRequests = require('./firebase/account-service');
var friendRequests = require('./firebase/friend-service');

accountRequests.userAccountRequests(io);
friendRequests.userFriendRequests(io);

var port = process.env.PORT || 4000;

app.get('/', function(req, res){
  res.send('Hello World!')
})

http.listen(port, () => {
	console.log('Server is listening on port ' + port)
});
