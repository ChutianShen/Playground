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
import com.example.kevin_sct.beastchat.entites.User;
import com.example.kevin_sct.beastchat.services.LiveFriendServices;
import com.example.kevin_sct.beastchat.views.UserFriendViews.UserFriendAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kevin_sct on 4/28/17.
 */

public class UserFriendsFragment extends BaseFragment implements UserFriendAdapter.UserClickedListener {
    @BindView(R.id.fragment_user_friends_reyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.fragment_user_friends_message)
    TextView mTextView;

    private LiveFriendServices mLiveFriendServices;
    private String mUserEmailString;

    private DatabaseReference mGetAllCurrenUsersFriendsReference;
    private ValueEventListener mGetAllCurrentUsersFriendsListener;

    private Unbinder mUnbinder;

    public static UserFriendsFragment newInstance(){
        return new UserFriendsFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLiveFriendServices = LiveFriendServices.getInstance();
        mUserEmailString = mSharedPreferences.getString(CONSTANT.USER_EMAIL,"");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_friends,container,false);
        mUnbinder = ButterKnife.bind(this,rootView);


        UserFriendAdapter adapter = new UserFriendAdapter((BaseFragmentActivity) getActivity(),this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mGetAllCurrenUsersFriendsReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_FRIENDS).child(CONSTANT.encodeEmail(mUserEmailString));

        mGetAllCurrentUsersFriendsListener = mLiveFriendServices.getAllFriends(mRecyclerView,adapter,mTextView);

        mGetAllCurrenUsersFriendsReference.addValueEventListener(mGetAllCurrentUsersFriendsListener);

        mRecyclerView.setAdapter(adapter);
        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();

        if (mGetAllCurrentUsersFriendsListener!=null){
            mGetAllCurrenUsersFriendsReference.removeEventListener(mGetAllCurrentUsersFriendsListener);
        }
    }

    @Override
    public void OnUserClicked(User user) {
        ArrayList<String> friendDetails = new ArrayList<>();
        friendDetails.add(user.getEmail());
        friendDetails.add(user.getUserPicture());
        friendDetails.add(user.getUserName());
        Intent intent = MessageActivity.newInstance(getActivity(), friendDetails);

        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
