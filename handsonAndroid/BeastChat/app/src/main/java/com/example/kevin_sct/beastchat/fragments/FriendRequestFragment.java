package com.example.kevin_sct.beastchat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.Utils.CONSTANT;
import com.example.kevin_sct.beastchat.activities.BaseFragmentActivity;
import com.example.kevin_sct.beastchat.entites.User;
import com.example.kevin_sct.beastchat.services.LiveFriendServices;
import com.example.kevin_sct.beastchat.views.FriendRequestViews.FriendRequestAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by kevin_sct on 4/28/17.
 */

public class FriendRequestFragment extends BaseFragment implements FriendRequestAdapter.OnOptionListener {

    @BindView(R.id.fragment_friend_request_recyclerView)
    RecyclerView mRecycleView;

    @BindView(R.id.fragment_friend_request_message)
    TextView mTextView;

    private LiveFriendServices mLiveFriendServices;

    private DatabaseReference mGetAllUsersFriendRequestReference;
    private ValueEventListener mGetAllUserFriendRequestListener;

    private DatabaseReference mGetAllUsersGameRequestReference;
    private ValueEventListener mGetAllUserGameRequestListener;

    private Unbinder mUnbinder;

    private String mUserEmailString;

    private Socket mSocket;

    public static FriendRequestFragment newInstance(){
        return new FriendRequestFragment();
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
        mUserEmailString = mSharedPreferences.getString(CONSTANT.USER_EMAIL, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friend_request, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);


        FriendRequestAdapter adapter = new FriendRequestAdapter((BaseFragmentActivity) getActivity(), this);

        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGetAllUsersFriendRequestReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_FRIEND_REQUEST_RECEIVED).child(CONSTANT.encodeEmail(mUserEmailString));
        mGetAllUsersGameRequestReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_GAME_REQUEST_RECEIVED).child(CONSTANT.encodeEmail(mUserEmailString));

        mGetAllUserFriendRequestListener = mLiveFriendServices.getAllFriendRequests(adapter, mRecycleView, mTextView);
        mGetAllUsersFriendRequestReference.addValueEventListener(mGetAllUserFriendRequestListener);

        mGetAllUserGameRequestListener = mLiveFriendServices.getAllGameRequests(adapter, mRecycleView, mTextView);
        mGetAllUsersGameRequestReference.addValueEventListener(mGetAllUserGameRequestListener);

        mRecycleView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();

        if(mGetAllUserFriendRequestListener != null){
            mGetAllUsersFriendRequestReference.removeEventListener(mGetAllUserFriendRequestListener);
        }
    }

    @Override
    public void OnOptionClicked(User user, String result) {
        if(result.equals("0")){
            DatabaseReference userFriendReference = FirebaseDatabase.getInstance().getReference()
                    .child(CONSTANT.FIRE_BASE_PATH_USER_FRIENDS).child(CONSTANT.encodeEmail(mUserEmailString))
                    .child(CONSTANT.encodeEmail(user.getEmail()));

            userFriendReference.setValue(user);
            mGetAllUsersFriendRequestReference.child(CONSTANT.encodeEmail(user.getEmail()))
                    .removeValue();
            mCompositeSubscription.add(mLiveFriendServices.approveDeclineFriendRequest(mSocket, mUserEmailString, user.getEmail(), "0"));
        }  else {
            mGetAllUsersFriendRequestReference.child(CONSTANT.encodeEmail(user.getEmail()))
                    .removeValue();
            mCompositeSubscription.add(mLiveFriendServices.approveDeclineFriendRequest(mSocket, mUserEmailString, user.getEmail(), "1"));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
