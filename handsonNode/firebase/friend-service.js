var app = require('express')();

var http = require('http').Server(app);

var io = require('socket.io')(http);
var admin = require("firebase-admin");

var FCM = require('fcm-push');
var serverKey = 'AAAAuBDLoMk:APA91bEZ0dol4QTtdRGC8eeGrvUDampt09kkXobcwqVwz7p1t8rmrZs5MihfF8ULCcolgowKUHUNmdTVQifn04cqQRGHIfxmgNh9e5rK28YlWUF_iB8zXUI3FgCypbs3MXTZXQ0UTPhe';
var fcm = new FCM(serverKey);

var userFriendRequests = (io) =>{
  io.on('connection',(socket)=>{
    console.log(`Client ${socket.id} has connected to friend service!`);

    sendMessage(socket, io);
    sendMove(socket, io);
    approveOrDeclineFrienqRequest(socket,io);
    approveOrDeclineGameRequest(socket,io);
    sendOrDeleteFriendRequest(socket, io);
    sendOrDeleteGameRequest(socket, io);
    detectDisconnection(socket, io);
	});
};

function sendMove(socket, io){
  socket.on('movedetails', (data) => {
    var db = admin.database();
    var friendMoveRef = db.ref('userMove').child(encodeEmail(data.gameFriendEmail))
    .child(encodeEmail(data.moveSenderEmail)).push();

    console.log(data.moveSenderEmail + ": " + data.moveText);

    var newfriendMoveRef = db.ref('newUserMoves').child(encodeEmail(data.gameFriendEmail))
    .child(friendMoveRef.key).push();

    var move={
      moveId: friendMoveRef.key,
      moveText: data.moveText,
      moveSenderEmail: data.moveSenderEmail
    };

    friendMoveRef.set(move);
    newfriendMoveRef.set(move);

  });
}

function sendMessage(socket, io){
  socket.on('details', (data) => {
    var db = admin.database();
    var friendMessageRef = db.ref('userMessages').child(encodeEmail(data.friendEmail))
    .child(encodeEmail(data.senderEmail)).push();

    var newfriendMessagesRef = db.ref('newUserMessages').child(encodeEmail(data.friendEmail))
    .child(friendMessageRef.key).push();

    var chatRoomRef = db.ref('userChatRooms').child(encodeEmail(data.friendEmail)).child(encodeEmail(data.senderEmail));

    var message={
      messageId: friendMessageRef.key,
      messageText: data.messageText,
      messageSenderEmail: data.senderEmail,
      messageSenderPicture: data.senderPicture
    };

    var chatRoom = {
      friendPicture: data.senderPicture,
      friendName:data.senderName,
      friendEmail: data.senderEmail,
      lastMessage: data.messageText,
      lastMessageSenderEmail: data.senderEmail,
      lastMessageRead:false,
      sentLastMessage:true
    };

    friendMessageRef.set(message);
    newfriendMessagesRef.set(message);
    chatRoomRef.set(chatRoom);

  });
}

function approveOrDeclineFrienqRequest(socket,io){
  socket.on('friendRequestResponse', (data) => {
    var db = admin.database();
    var friendRequestRef = db.ref('friendRequestsSent').child(encodeEmail(data.friendEmail))
        .child(encodeEmail(data.userEmail));
        friendRequestRef.remove();

        console.log("Friend Answer Code: " + data.requestCode);

        if (data.requestCode ==0) {
          var db = admin.database();
          var ref = db.ref('users');
          var userRef = ref.child(encodeEmail(data.userEmail));

          var userFriendsRef = db.ref('userFriends');
          var friendFriendRef = userFriendsRef.child(encodeEmail(data.friendEmail))
          .child(encodeEmail(data.userEmail));

          userRef.once('value',(snapshot)=>{
            friendFriendRef.set({
              email:snapshot.val().email,
              userName:snapshot.val().userName,
              userPicture:snapshot.val().userPicture,
              dateJoined:snapshot.val().dateJoined,
              hasLoggedIn:snapshot.val().hasLoggedIn
            });
          });
        }
  });
}

function approveOrDeclineGameRequest(socket,io){

  socket.on('gameRequestResponse', (data) => {
    var db = admin.database();
    var gameRequestRef = db.ref('gameRequestsSent').child(encodeEmail(data.friendEmail))
        .child(encodeEmail(data.userEmail));
        gameRequestRef.remove();

        console.log("Game Answer Code: " + data.requestCode);

        if (data.requestCode ==0) {
          var db = admin.database();
          var ref = db.ref('users');
          var userRef = ref.child(encodeEmail(data.userEmail));

          var userGameFriendsRef = db.ref('userGameFriends');
          var friendGameFriendRef = userGameFriendsRef.child(encodeEmail(data.friendEmail))
          .child(encodeEmail(data.userEmail));

          userRef.once('value',(snapshot)=>{
            friendGameFriendRef.set({
              email:snapshot.val().email,
              userName:snapshot.val().userName,
              userPicture:snapshot.val().userPicture,
              dateJoined:snapshot.val().dateJoined,
              hasLoggedIn:snapshot.val().hasLoggedIn
            });
          });
        }
  });
}

function sendOrDeleteFriendRequest(socket, io){
  socket.on('friendRequest', (data) => {
    var friendEmail = data.friendEmail;
    var userEmail = data.userEmail;
    var requestCode = data.requestCode;

    var db = admin.database();
    var friendRef = db.ref('friendRequestReceieved').child(encodeEmail(friendEmail))
    .child(encodeEmail(userEmail));

    console.log("requestCode: " + requestCode); //for debugging


    if(requestCode == 0){
      var db = admin.database();
      var ref = db.ref('users');
      var userRef = ref.child(encodeEmail(data.userEmail));

      userRef.once('value', (snapshot) => {
        friendRef.set({
          email:snapshot.val().email,
          userName:snapshot.val().userName,
          userPicture:snapshot.val().userPicture,
          dateJoined:snapshot.val().dateJoined,
          hasLoggedIn:snapshot.val().hasLoggedIn
        });
      });


      var tokenRef = db.ref('userToken');
      var friendToken = tokenRef.child(encodeEmail(friendEmail));

      friendToken.once('value', (snapshot) => {
        var message = {
          to : snapshot.val().token,    //send to the guy who has this tokenRef

          data : {
            title : 'Beast Chat',
            body : `Friend Request from ${userEmail}`
          },
        };
        fcm.send(message)
        .then((response) => {
          console.log('Message sent!');
        }).catch((err) => {
          console.log(err);
        });
      });

    } else {
      friendRef.remove();
    }
  });
}



function sendOrDeleteGameRequest(socket, io){
  socket.on('gameRequest', (data) => {
    var friendEmail = data.friendEmail;
    var userEmail = data.userEmail;
    var requestCode = data.requestCode;

    var db = admin.database();
    var gameRef = db.ref('gameRequestReceieved').child(encodeEmail(friendEmail))
    .child(encodeEmail(userEmail));

    console.log("Game invitation requestCode: " + requestCode); //for debugging


    if(requestCode == 0){
      var db = admin.database();
      var ref = db.ref('users');
      var userRef = ref.child(encodeEmail(data.userEmail));

      userRef.once('value', (snapshot) => {
        gameRef.set({
          email:snapshot.val().email,
          userName:snapshot.val().userName,
          userPicture:snapshot.val().userPicture,
          dateJoined:snapshot.val().dateJoined,
          hasLoggedIn:snapshot.val().hasLoggedIn
        });
      });


      var tokenRef = db.ref('userToken');
      var friendToken = tokenRef.child(encodeEmail(friendEmail));

      friendToken.once("value", (snapshot) => {
        console.log("Game Notification is working! ");
        var message = {
          to : snapshot.val().token,    //send to the guy who has this tokenRef

          data : {
            title : 'Beast Chat',
            body : `Game Request from ${userEmail}`
          },
        };
        fcm.send(message)
        .then((response) => {
          console.log('Message sent!');
        }).catch((err) => {
          console.log(err);
        });
      });

    } else {
      gameRef.remove();
    }

  });
}

function detectDisconnection(socket,io){
      socket.on('disconnect',()=>{
        console.log('A client has disconnected from the friend service');
      });
}

function encodeEmail(eamil){
	return eamil.replace('.', ',');
}

module.exports = {
	userFriendRequests
}
