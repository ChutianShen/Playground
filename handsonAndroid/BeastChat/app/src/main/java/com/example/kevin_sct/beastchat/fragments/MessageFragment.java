package com.example.kevin_sct.beastchat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.Utils.CONSTANT;
import com.example.kevin_sct.beastchat.activities.BaseFragmentActivity;
import com.example.kevin_sct.beastchat.entites.ChatRoom;
import com.example.kevin_sct.beastchat.entites.Message;
import com.example.kevin_sct.beastchat.services.LiveFriendServices;
import com.example.kevin_sct.beastchat.views.MessageViews.MessageViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.socket.client.IO;
import io.socket.client.Socket;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by kevin_sct on 4/30/17.
 */

public class MessageFragment extends BaseFragment {

    public static final String FRIEND_DETAILS_EXTRA = "FRIEND_DETAILS_EXTRA";

    @BindView(R.id.fragment_messages_friendPicture)
    ImageView mFriendPicture;

    @BindView(R.id.fragment_messages_friendName)
    TextView mFriendName;

    @BindView(R.id.fragment_messages_messageBox)
    EditText mMessageBox;

    @BindView(R.id.fragment_messages_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.fragment_messages_sendArrow)
    ImageView mSendMessage;

    private Unbinder mUnbinder;
    private Socket mSocket;
    private LiveFriendServices mLiveFriendServices;

    private String mFriendEmailString;
    private String mFriendPictureString;
    private String mFriendNameString;
    private String mUserEmailString;

    private MessageViewAdapter mAdapter;

    private DatabaseReference mGetAllMessageReference;
    private ValueEventListener mGetAllMessageListener;

    private DatabaseReference mUserChatRoomReference;
    private ValueEventListener mUserChatRoomListener;

    private PublishSubject<String> mMessageSubject;


    public static MessageFragment newInstance(ArrayList<String> friendDetails){
        Bundle arguments = new Bundle();
        arguments.putStringArrayList(FRIEND_DETAILS_EXTRA, friendDetails);
        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setArguments(arguments);
        return messageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mSocket = IO.socket(CONSTANT.IP_LOCAL_HOST);
        } catch (URISyntaxException e) {
            Log.i(LoginFragment.class.getSimpleName(),e.getMessage());
            Toast.makeText(getActivity(),"Can't connect to the server",Toast.LENGTH_SHORT).show();
        }

        mSocket.connect();

        mLiveFriendServices = LiveFriendServices.getInstance();
        ArrayList<String> friendDetails = getArguments().getStringArrayList(FRIEND_DETAILS_EXTRA);
        mFriendEmailString = friendDetails.get(0);
        mFriendPictureString = friendDetails.get(1);
        mFriendNameString = friendDetails.get(2);
        mUserEmailString = mSharedPreferences.getString(CONSTANT.USER_EMAIL,"");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        Picasso.with(getActivity())
                .load(mFriendPictureString)
                .into(mFriendPicture);
        mFriendName.setText(mFriendNameString);

        mAdapter = new MessageViewAdapter((BaseFragmentActivity) getActivity(),mUserEmailString);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mUserChatRoomReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_CHAT_ROOMS)
                .child(CONSTANT.encodeEmail(mUserEmailString))
                .child(CONSTANT.encodeEmail(mFriendEmailString));

        mUserChatRoomListener = getCurrentChatRoomListener();
        mUserChatRoomReference.addValueEventListener(mUserChatRoomListener);

        mGetAllMessageReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_MESSAGES)
                .child(CONSTANT.encodeEmail(mUserEmailString))
                .child(CONSTANT.encodeEmail(mFriendEmailString));

        mGetAllMessageListener = mLiveFriendServices.getAllMessages(mRecyclerView, mFriendName, mFriendPicture, mAdapter, mUserEmailString);

        mGetAllMessageReference.addValueEventListener(mGetAllMessageListener);
        mRecyclerView.setAdapter(mAdapter);

        mCompositeSubscription.add(createChatRoomSubscription());
        mRecyclerView.scrollToPosition(mAdapter.getmMessages().size());

        messageBoxListener();
        return rootView;
    }


    @OnClick(R.id.fragment_messages_sendArrow)
    public void setmSendMessage(){
        if(mMessageBox.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Message can't be blank!", Toast.LENGTH_SHORT).show();
        }else {
            ChatRoom chatRoom = new ChatRoom(mFriendPictureString,mFriendNameString,
                    mFriendEmailString,mMessageBox.getText().toString(),mUserEmailString,true, true);

            mUserChatRoomReference.setValue(chatRoom);

            DatabaseReference newMessageReference = mGetAllMessageReference.push();
            Message message = new Message(newMessageReference.getKey(), mMessageBox.getText().toString()
                    , mUserEmailString, mSharedPreferences.getString(CONSTANT.USER_PICTURE, ""));

            newMessageReference.setValue(message);

            mCompositeSubscription.add(
                    mLiveFriendServices.sendMessage(mSocket, mUserEmailString,
                    mSharedPreferences.getString(CONSTANT.USER_PICTURE,""),
                    mMessageBox.getText().toString(),
                    mFriendEmailString,
                    mSharedPreferences.getString(CONSTANT.USER_NAME,""))
            );

            //Toast.makeText(getActivity(), mMessageBox.getText().toString(), Toast.LENGTH_SHORT).show();
            View view = getActivity().getCurrentFocus();
            //Toast.makeText(getActivity(), "View ID: " + view.getId(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(), "Another View ID: " + getView().getId(), Toast.LENGTH_SHORT).show();
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

            mRecyclerView.scrollToPosition(mAdapter.getmMessages().size());
            mMessageBox.setText("");
        }
    }

    private Subscription createChatRoomSubscription(){
        mMessageSubject = PublishSubject.create();
        return mMessageSubject
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String message) {
                        if(!message.isEmpty()){
                            ChatRoom chatRoom = new ChatRoom(mFriendPictureString,mFriendNameString,
                                    mFriendEmailString,message,mUserEmailString,true,false);

                            mUserChatRoomReference.setValue(chatRoom);        //send the chatroom to the reference
                        }
                    }
                });
    }

    private void messageBoxListener(){
        mMessageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMessageSubject.onNext(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mMessageBox.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
        });
    }

    public ValueEventListener getCurrentChatRoomListener(){
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ChatRoom chatRoom = dataSnapshot.getValue(ChatRoom.class);
                if(chatRoom != null){
                    mUserChatRoomReference.child("lastMessageRead").setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();

        if(mGetAllMessageListener != null){
            mGetAllMessageReference.removeEventListener(mGetAllMessageListener);
        }

        if(mUserChatRoomListener != null){
            mUserChatRoomReference.removeEventListener(mUserChatRoomListener);
        }
    }
}
