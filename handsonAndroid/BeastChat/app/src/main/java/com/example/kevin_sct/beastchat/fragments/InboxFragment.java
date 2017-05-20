package com.example.kevin_sct.beastchat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.Utils.CONSTANT;
import com.example.kevin_sct.beastchat.activities.BaseFragmentActivity;
import com.example.kevin_sct.beastchat.activities.MessageActivity;
import com.example.kevin_sct.beastchat.entites.ChatRoom;
import com.example.kevin_sct.beastchat.services.LiveFriendServices;
import com.example.kevin_sct.beastchat.views.ChatRoomViews.ChatRoomViewAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kevin_sct on 4/28/17.
 */

public class InboxFragment extends BaseFragment implements ChatRoomViewAdapter.ChatRoomListener {

    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;

    @BindView(R.id.fragment_inbox_recyclerView)
    RecyclerView mRecycler;

    @BindView(R.id.fragment_inbox_message)
    TextView mTextView;

    private Unbinder mUnbinder;

    private LiveFriendServices mLiveFriendsService;

    private DatabaseReference mAllFriendRequestsReference;
    private ValueEventListener mAllFriendRequestsListener;

    private DatabaseReference mAllGameRequestReference;
    private ValueEventListener mAllGameRequestListener;


    private DatabaseReference mUserChatRoomReference;
    private ValueEventListener mUserChatRoomListener;

    private String mUserEmailString;

    private DatabaseReference mUsersNewMessagesReference;
    private ValueEventListener mUsersNewMessagesListener;

    public  static  InboxFragment newInstance(){
        return new InboxFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLiveFriendsService = LiveFriendServices.getInstance();
        mUserEmailString = mSharedPreferences.getString(CONSTANT.USER_EMAIL,"");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        mBottomBar.selectTabWithId(R.id.tab_messages);
        setUpBottomBar(mBottomBar, 1);

        ChatRoomViewAdapter adapter = new ChatRoomViewAdapter((BaseFragmentActivity)getActivity(), this, mUserEmailString);

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        mUserChatRoomReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_CHAT_ROOMS).child(CONSTANT.encodeEmail(mUserEmailString));

        mUserChatRoomListener = mLiveFriendsService.getAllChatRooms(mRecycler,mTextView,adapter);
        mUserChatRoomReference.addValueEventListener(mUserChatRoomListener);

        mAllFriendRequestsReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_FRIEND_REQUEST_RECEIVED).child(CONSTANT.encodeEmail(mUserEmailString));
        mAllFriendRequestsListener = mLiveFriendsService.getFriendRequestBottom(mBottomBar,R.id.tab_friends);
        mAllFriendRequestsReference.addValueEventListener(mAllFriendRequestsListener);

        mAllGameRequestListener = mLiveFriendsService.getGameRequestBottom(mBottomBar, R.id.tab_friends);
        mAllGameRequestReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_GAME_REQUEST_RECEIVED)
                .child(CONSTANT.encodeEmail(mUserEmailString));
        mAllGameRequestReference.addValueEventListener(mAllGameRequestListener);

        mUsersNewMessagesReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_NEW_MESSAGES).child(CONSTANT.encodeEmail(mUserEmailString));
        mUsersNewMessagesListener = mLiveFriendsService.getAllNewMessages(mBottomBar,R.id.tab_messages);

        mUsersNewMessagesReference.addValueEventListener(mUsersNewMessagesListener);

        mRecycler.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if (mAllFriendRequestsListener!=null){
            mAllFriendRequestsReference.removeEventListener(mAllFriendRequestsListener);
        }

        if(mAllGameRequestListener != null){
            mAllGameRequestReference.removeEventListener(mAllGameRequestListener);
        }

        if (mUsersNewMessagesListener!=null){
            mUsersNewMessagesReference.removeEventListener(mUsersNewMessagesListener);
        }

        if (mUsersNewMessagesListener!=null){
            mUsersNewMessagesReference.removeEventListener(mUsersNewMessagesListener);
        }
    }

    @Override
    public void OnChatRoomClicked(ChatRoom chatRoom) {
        ArrayList<String> friendDetails = new ArrayList<>();
        friendDetails.add(chatRoom.getFriendEmail());
        friendDetails.add(chatRoom.getFriendPicture());
        friendDetails.add(chatRoom.getFriendName());
        Intent intent = MessageActivity.newInstance(getActivity(),friendDetails);
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }
}
