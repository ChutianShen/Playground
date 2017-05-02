package com.example.kevin_sct.beastchat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.Utils.CONSTANT;
import com.example.kevin_sct.beastchat.services.LiveFriendServices;
import com.example.kevin_sct.beastchat.views.FriendsViewPagerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kevin_sct on 4/28/17.
 */

public class FriendFragment extends BaseFragment {

    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;

    @BindView(R.id.fragment_friends_tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.fragment_friends_viewPager)
    ViewPager mViewPager;

    public static FriendFragment newInstance(){
        return new FriendFragment();
    }

    private Unbinder mUnbinder;

    private LiveFriendServices mLiveFriendServices;
    private DatabaseReference mAllFriendRequestReference;
    private ValueEventListener mAllFriendRequestListener;
    private String mUserEmailString;

    private DatabaseReference mUsersNewMessagesReference;
    private ValueEventListener mUsersNewMessagesListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLiveFriendServices = LiveFriendServices.getInstance();
        mUserEmailString = mSharedPreferences.getString(CONSTANT.USER_EMAIL, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        mBottomBar.selectTabWithId(R.id.tab_friends);
        setUpBottomBar(mBottomBar, 2);

        mUsersNewMessagesReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_NEW_MESSAGES).child(CONSTANT.encodeEmail(mUserEmailString));
        mUsersNewMessagesListener = mLiveFriendServices.getAllNewMessages(mBottomBar,R.id.tab_messages);

        mUsersNewMessagesReference.addValueEventListener(mUsersNewMessagesListener);


        FriendsViewPagerAdapter friendsViewPagerAdapter = new FriendsViewPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(friendsViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mAllFriendRequestReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_FRIEND_REQUEST_RECEIVED).child(CONSTANT.encodeEmail(mUserEmailString));
        mAllFriendRequestListener = mLiveFriendServices.getFriendRequestBottom(mBottomBar,R.id.tab_friends);
        mAllFriendRequestReference.addValueEventListener(mAllFriendRequestListener);


        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if (mAllFriendRequestListener!=null){
            mAllFriendRequestReference.removeEventListener(mAllFriendRequestListener);
        }

        if (mUsersNewMessagesListener!=null){
            mUsersNewMessagesReference.removeEventListener(mUsersNewMessagesListener);
        }
    }
}
