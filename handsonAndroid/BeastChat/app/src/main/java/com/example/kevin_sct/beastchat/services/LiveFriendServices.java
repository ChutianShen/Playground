package com.example.kevin_sct.beastchat.services;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin_sct.beastchat.Utils.CONSTANT;
import com.example.kevin_sct.beastchat.entites.ChatRoom;
import com.example.kevin_sct.beastchat.entites.Message;
import com.example.kevin_sct.beastchat.entites.User;
import com.example.kevin_sct.beastchat.fragments.FindFriendFragment;
import com.example.kevin_sct.beastchat.views.ChatRoomViews.ChatRoomViewAdapter;
import com.example.kevin_sct.beastchat.views.FindFriendsViews.FindFriendsAdapter;
import com.example.kevin_sct.beastchat.views.FriendRequestViews.FriendRequestAdapter;
import com.example.kevin_sct.beastchat.views.GameRequestViews.GameRequestAdapter;
import com.example.kevin_sct.beastchat.views.MessageViews.MessageViewAdapter;
import com.example.kevin_sct.beastchat.views.UserFriendViews.UserFriendAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.socket.client.Socket;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//import java.net.Socket;



/**
 * Created by kevin_sct on 4/29/17.
 */

public class LiveFriendServices {

    private final int SERVER_SUCCESS = 6;
    private final int SERVER_FAILURE = 7;

    public static LiveFriendServices mLiveFriendServices;

    public static LiveFriendServices getInstance(){
        if(mLiveFriendServices == null){
            return new LiveFriendServices();
        }else {
            return mLiveFriendServices;
        }
    }

    public ValueEventListener getAllFriends(final RecyclerView recyclerView, final UserFriendAdapter adapter, final TextView textView){
        final List<User> users = new ArrayList<>();
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user =snapshot.getValue(User.class);
                    users.add(user);
                }

                if (users.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                } else{
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    adapter.setmUsers(users);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public ValueEventListener getAllGameFriends(final Intent intent, final Activity activityNow){
        final List<User> users = new ArrayList<>();
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user =snapshot.getValue(User.class);
                    users.add(user);
                }
                if(!users.isEmpty())
                    activityNow.startActivity(intent);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }


    public ValueEventListener getAllFriendRequests(final FriendRequestAdapter adapter, final RecyclerView recyclerView, final TextView textView){

        final List<User> users = new ArrayList<>();

        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Log.i("Friend", "Friend Sent Change");

                users.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    users.add(user);
                }

                if(users.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    adapter.setmUsers(users);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public ValueEventListener getAllGameRequests(final GameRequestAdapter adapter, final RecyclerView recyclerView, final TextView textView){

        final List<User> users = new ArrayList<>();

        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    users.add(user);
                }

                if(users.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    adapter.setmUsers(users);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public Subscription approveDeclineFriendRequest(final Socket socket, String userEmail, String friendEmail, String requestCode){
        List<String> details = new ArrayList<>();
        details.add(userEmail);
        details.add(friendEmail);
        details.add(requestCode);

        Observable<List<String>> listObservable = Observable.just(details);

        return listObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<String>, Integer>() {
                    @Override
                    public Integer call(List<String> strings) {
                        JSONObject sendData = new JSONObject();

                        try {
                            sendData.put("userEmail",strings.get(0));
                            sendData.put("friendEmail",strings.get(1));
                            sendData.put("requestCode",strings.get(2));
                            socket.emit("friendRequestResponse",sendData);
                            return SERVER_SUCCESS;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return SERVER_FAILURE;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }
                });
    }

    public Subscription approveDeclineGameRequest(final Socket socket, String userEmail, String friendEmail, String requestCode){
        List<String> details = new ArrayList<>();
        details.add(userEmail);
        details.add(friendEmail);
        details.add(requestCode);

        Observable<List<String>> listObservable = Observable.just(details);

        Log.i("approve","approve or decline function works");

        return listObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<String>, Integer>() {
                    @Override
                    public Integer call(List<String> strings) {
                        JSONObject sendData = new JSONObject();

                        try {
                            sendData.put("userEmail",strings.get(0));
                            sendData.put("friendEmail",strings.get(1));
                            sendData.put("requestCode",strings.get(2));
                            socket.emit("gameRequestResponse",sendData);
                            return SERVER_SUCCESS;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return SERVER_FAILURE;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }
                });
    }


    public Subscription addOrRemoveGameRequest(final Socket socket, String userEmail, String friendEmail, String requestCode){
        List<String> details = new ArrayList<>();

        details.add(userEmail);
        details.add(friendEmail);
        details.add(requestCode);

        Observable<List<String>> listObservable = Observable.just(details);

        return listObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<String>, Integer>() {
                    @Override
                    public Integer call(List<String> strings) {
                        JSONObject sendData = new JSONObject();

                        try {
                            sendData.put("userEmail",strings.get(0));
                            sendData.put("friendEmail",strings.get(1));
                            sendData.put("requestCode",strings.get(2));
                            socket.emit("gameRequest",sendData);
                            return SERVER_SUCCESS;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return SERVER_FAILURE;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }
                });
    }

    public Subscription addOrRemoveFriendRequest(final Socket socket, String userEmail, String friendEmail, String requestCode){
        List<String> details = new ArrayList<>();

        details.add(userEmail);
        details.add(friendEmail);
        details.add(requestCode);

        Observable<List<String>> listObservable = Observable.just(details);

        return listObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<String>, Integer>() {
                    @Override
                    public Integer call(List<String> strings) {
                        JSONObject sendData = new JSONObject();

                        try {
                            sendData.put("userEmail",strings.get(0));
                            sendData.put("friendEmail",strings.get(1));
                            sendData.put("requestCode",strings.get(2));
                            socket.emit("friendRequest",sendData);
                            return SERVER_SUCCESS;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return SERVER_FAILURE;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }
                });
    }



    public ValueEventListener getAllChatRooms(final RecyclerView recyclerView, final TextView textView,
                                              final ChatRoomViewAdapter adapter){
        final List<ChatRoom> chatRooms = new ArrayList<>();
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatRooms.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ChatRoom chatRoom = snapshot.getValue(ChatRoom.class);
                    chatRooms.add(chatRoom);
                }
                if (chatRooms.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                } else{
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    adapter.setmChatRooms(chatRooms);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public ValueEventListener getAllNewMessages(final BottomBar bottomBar, final int tagId){
        final List<Message> messages = new ArrayList<>();
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Message message = snapshot.getValue(Message.class);
                    messages.add(message);
                }

                if (!messages.isEmpty()){
                    bottomBar.getTabWithId(tagId).setBadgeCount(messages.size());
                } else{
                    bottomBar.getTabWithId(tagId).removeBadge();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public ValueEventListener getAllMessages(final RecyclerView recyclerView, final TextView textView, final ImageView imageView,
                                             final MessageViewAdapter adapter, final String userEmail){
        final List<Message> messages = new ArrayList<>();
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                DatabaseReference newMessagesReference = FirebaseDatabase.getInstance().getReference()
                        .child(CONSTANT.FIRE_BASE_PATH_USER_NEW_MESSAGES).child(CONSTANT.encodeEmail(userEmail));
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Message message = snapshot.getValue(Message.class);
                    newMessagesReference.child(message.getMessageId()).removeValue();
                    messages.add(message);
                }

                if (messages.isEmpty()){
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else{
                    imageView.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.setmMessages(messages);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }



    public Subscription sendMessage(final Socket socket, String messageSenderEmail, String messageSenderPicture, String messageText
                    , String friendEmail, String messageSenderName){
        List<String> details = new ArrayList<>();
        details.add(messageSenderEmail);
        details.add(messageSenderPicture);
        details.add(messageText);
        details.add(friendEmail);
        details.add(messageSenderName);
        Observable<List<String>> listObservable = Observable.just(details);

        return  listObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<String>, Integer>() {
                    @Override
                    public Integer call(List<String> strings) {
                        JSONObject sendData = new JSONObject();

                        try {
                            sendData.put("senderEmail",strings.get(0));
                            sendData.put("senderPicture",strings.get(1));
                            sendData.put("messageText",strings.get(2));
                            sendData.put("friendEmail",strings.get(3));
                            sendData.put("senderName",strings.get(4));
                            socket.emit("details",sendData);
                            return SERVER_SUCCESS;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return SERVER_FAILURE;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }
                });
    }


    public ValueEventListener getAllCurrentUsersFriendMap(final FindFriendsAdapter adapter){
        final HashMap<String,User> userHashMap = new HashMap<>();
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userHashMap.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    userHashMap.put(user.getEmail(),user);
                }

                adapter.setmCurrentUserFriendsMap(userHashMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public ValueEventListener getFriendRequestsSent(final FindFriendsAdapter adapter, final FindFriendFragment fragment){
        final HashMap<String, User> userHashMap = new HashMap<>();
        Log.i("Friend", "Friend Sent Change");
        return new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userHashMap.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    userHashMap.put(user.getEmail(), user);
                }

                adapter.setmFriendRequestSentMap(userHashMap);
                fragment.setmFriendRequestsSentMap(userHashMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public ValueEventListener getGameRequestsSent(final FindFriendsAdapter adapter, final FindFriendFragment fragment, final Activity mActivity){
        final HashMap<String, User> userHashMap2 = new HashMap<>();
        Log.i("Friend", "Game Sent Change");
        return new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userHashMap2.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(user == null){
                        Toast.makeText(mActivity, "Can't find user in Database", Toast.LENGTH_SHORT).show();
                    }
                    userHashMap2.put(user.getEmail(), user);
                }

                adapter.setmGameRequestSentMap(userHashMap2);
                fragment.setmGameRequestsSentMap(userHashMap2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public ValueEventListener getFriendRequestsReceived(final FindFriendsAdapter adapter, final FindFriendFragment fragment){
        final HashMap<String, User> userHashMap = new HashMap<>();

        return new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userHashMap.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    userHashMap.put(user.getEmail(), user);
                }

                adapter.setmFriendRequestReceivedMap(userHashMap);
                fragment.setmFriendRequestsSentMap(userHashMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public ValueEventListener getGameRequestsReceived(final FindFriendsAdapter adapter, final FindFriendFragment fragment){
        final HashMap<String, User> userHashMap = new HashMap<>();

        return new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userHashMap.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    userHashMap.put(user.getEmail(), user);
                }

                adapter.setmGameRequestReceivedMap(userHashMap);
                fragment.setmGameRequestsSentMap(userHashMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public ValueEventListener getFriendRequestBottom(final BottomBar bottomBar, final int tagId){
        final List<User> users = new ArrayList<>();
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    users.add(user);
                }

                if (!users.isEmpty()){      // we do have some requests
                    bottomBar.getTabWithId(tagId).setBadgeCount(users.size());
                } else{
                    bottomBar.getTabWithId(tagId).removeBadge();    //if our actual user lists are ampty, it means we have no friend request. And it should not have anything in the bottom bar
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public ValueEventListener getGameRequestBottom(final BottomBar bottomBar, final int tagId){
        final List<User> users = new ArrayList<>();
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    users.add(user);
                }

                if (!users.isEmpty()){      // we do have some requests
                    bottomBar.getTabWithId(tagId).setBadgeCount(users.size());
                } else{
                    bottomBar.getTabWithId(tagId).removeBadge();    //if our actual user lists are ampty, it means we have no friend request. And it should not have anything in the bottom bar
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public List<User> getMatchingUsers(List<User> users, String userEmail){
        if(userEmail.isEmpty()) return users;

        List<User> usersFound = new ArrayList<>();

        for(User user : users){
            if(user.getEmail().toLowerCase().startsWith(userEmail.toLowerCase())){
                usersFound.add(user);
            }
        }

        return usersFound;
    }
}
