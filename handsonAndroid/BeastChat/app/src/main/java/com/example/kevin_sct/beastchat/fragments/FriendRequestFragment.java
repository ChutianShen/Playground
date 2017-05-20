package com.example.kevin_sct.beastchat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.Utils.CONSTANT;
import com.example.kevin_sct.beastchat.activities.BaseFragmentActivity;
import com.example.kevin_sct.beastchat.activities.RSPActivity;
import com.example.kevin_sct.beastchat.entites.User;
import com.example.kevin_sct.beastchat.services.LiveFriendServices;
import com.example.kevin_sct.beastchat.views.FriendRequestViews.FriendRequestAdapter;
import com.example.kevin_sct.beastchat.views.GameRequestViews.GameRequestAdapter;
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

public class FriendRequestFragment extends BaseFragment {

    @BindView(R.id.fragment_friend_request_recyclerView)
    RecyclerView mFriendRecycleView;

    @BindView(R.id.fragment_game_request_recyclerView)
    RecyclerView mGameRecycleView;

    @BindView(R.id.fragment_friend_request_message)
    TextView mTextView;

    @BindView(R.id.request_linearlayout)
    LinearLayout mLinearLayout;

    private LiveFriendServices mLiveFriendServices;

    private DatabaseReference mGetAllUsersFriendRequestReference;
    private ValueEventListener mGetAllUserFriendRequestListener;

    private DatabaseReference mGetAllUsersGameRequestReference;
    private ValueEventListener mGetAllUserGameRequestListener;

    private DatabaseReference mGetAllCurrenUsersGameFriendsReference;
    private ValueEventListener mGetAllCurrentUsersGameFriendsListener;

    private FriendRequestAdapter.OnOptionListener mFriendListener;
    private GameRequestAdapter.GameOnOptionListener mGameListener;

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

        mFriendListener = new FriendRequestAdapter.OnOptionListener() {
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
        };
        mGameListener = new GameRequestAdapter.GameOnOptionListener() {
            @Override
            public void GameOnOptionClicked(User user, String result) {
                if(result.equals("0")){
                    DatabaseReference userFriendGameReference = FirebaseDatabase.getInstance().getReference()
                            .child(CONSTANT.FIRE_BASE_PATH_USER_GAME_FRIENDS).child(CONSTANT.encodeEmail(mUserEmailString))
                            .child(CONSTANT.encodeEmail(user.getEmail()));

                    userFriendGameReference.setValue(user);
                    mGetAllUsersGameRequestReference.child(CONSTANT.encodeEmail(user.getEmail()))
                            .removeValue();
                    mCompositeSubscription.add(mLiveFriendServices.approveDeclineGameRequest(mSocket, mUserEmailString, user.getEmail(), "0"));
                    startActivity(new Intent(getActivity(), RSPActivity.class));
                    Toast.makeText(getActivity(), "Accept the request", Toast.LENGTH_SHORT).show();
                }  else {
                    mGetAllUsersGameRequestReference.child(CONSTANT.encodeEmail(user.getEmail()))
                            .removeValue();
                    mCompositeSubscription.add(mLiveFriendServices.approveDeclineGameRequest(mSocket, mUserEmailString, user.getEmail(), "1"));
                    Toast.makeText(getActivity(), "Decline the request", Toast.LENGTH_SHORT).show();
                }
            }
        };

        FriendRequestAdapter adapter = new FriendRequestAdapter((BaseFragmentActivity) getActivity(), mFriendListener);
        GameRequestAdapter adapterGame = new GameRequestAdapter((BaseFragmentActivity) getActivity(), mGameListener);

        mFriendRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGameRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGetAllUsersFriendRequestReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_FRIEND_REQUEST_RECEIVED).child(CONSTANT.encodeEmail(mUserEmailString));
        mGetAllUsersGameRequestReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_GAME_REQUEST_RECEIVED).child(CONSTANT.encodeEmail(mUserEmailString));

        mGetAllUserFriendRequestListener = mLiveFriendServices.getAllFriendRequests(adapter, mFriendRecycleView, mTextView);
        mGetAllUsersFriendRequestReference.addValueEventListener(mGetAllUserFriendRequestListener);

        mGetAllUserGameRequestListener = mLiveFriendServices.getAllGameRequests(adapterGame, mGameRecycleView, mTextView);
        mGetAllUsersGameRequestReference.addValueEventListener(mGetAllUserGameRequestListener);


        mGetAllCurrenUsersGameFriendsReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_GAME_FRIENDS).child(CONSTANT.encodeEmail(mUserEmailString));

        Intent intent = new Intent(getActivity(), RSPActivity.class);
        mGetAllCurrentUsersGameFriendsListener = mLiveFriendServices.getAllGameFriends(intent, getActivity());

        mGetAllCurrenUsersGameFriendsReference.addValueEventListener(mGetAllCurrentUsersGameFriendsListener);


        mFriendRecycleView.setAdapter(adapter);
        mGameRecycleView.setAdapter(adapterGame);



        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();

        if(mGetAllUserFriendRequestListener != null){
            mGetAllUsersFriendRequestReference.removeEventListener(mGetAllUserFriendRequestListener);
        }

        if(mGetAllUserGameRequestListener != null){
            mGetAllUsersGameRequestReference.removeEventListener(mGetAllUserGameRequestListener);
        }
    }

    /*
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
    */

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
