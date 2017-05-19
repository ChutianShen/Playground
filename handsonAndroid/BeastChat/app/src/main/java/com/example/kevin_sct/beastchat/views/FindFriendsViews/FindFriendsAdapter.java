package com.example.kevin_sct.beastchat.views.FindFriendsViews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.activities.BaseFragmentActivity;
import com.example.kevin_sct.beastchat.entites.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kevin_sct on 4/29/17.
 */

public class FindFriendsAdapter extends RecyclerView.Adapter {

    private BaseFragmentActivity mActivity;
    private List<User> mUsers;
    private LayoutInflater mInflater;

    private UserListener mListener;
    private GameInviteListener mGameListener;

    private HashMap<String, User> mFriendRequestSentMap;
    private HashMap<String, User> mFriendRequestReceivedMap;
    private HashMap<String, User> mCurrentUserFriendsMap;

    private HashMap<String, User> mGameRequestSentMap;
    private HashMap<String, User> mGameRequestReceivedMap;

    public FindFriendsAdapter(BaseFragmentActivity mActivity, UserListener mListener, GameInviteListener mGameListener) {
        this.mActivity = mActivity;
        this.mListener = mListener;
        this.mGameListener = mGameListener;

        /*
        if(this.mGameListener==this.mListener)
            Toast.makeText(mActivity, "***********", Toast.LENGTH_SHORT).show();
        */

        mInflater = mActivity.getLayoutInflater();
        mUsers = new ArrayList<>();
        mFriendRequestSentMap = new HashMap<>();
        mFriendRequestReceivedMap = new HashMap<>();
        mGameRequestSentMap = new HashMap<>();
        mGameRequestReceivedMap = new HashMap<>();
        mCurrentUserFriendsMap = new HashMap<>();


    }

    public void setmFriendRequestSentMap(HashMap<String, User> friendRequestSentMap) {
        mFriendRequestSentMap.clear();
        mFriendRequestSentMap.putAll(friendRequestSentMap);
        notifyDataSetChanged();
    }

    public void setmGameRequestSentMap(HashMap<String, User> gameRequestSentMap) {
        mGameRequestSentMap.clear();
        mGameRequestSentMap.putAll(gameRequestSentMap);
        notifyDataSetChanged();
    }

    public void setmCurrentUserFriendsMap(HashMap<String, User> currentUserFriendsMap){
        mCurrentUserFriendsMap.clear();
        mCurrentUserFriendsMap.putAll(currentUserFriendsMap);
        notifyDataSetChanged();
    }

    public void setmFriendRequestReceivedMap(HashMap<String, User> friendRequestReceivedMap) {
        mFriendRequestReceivedMap.clear();
        mFriendRequestReceivedMap.putAll(friendRequestReceivedMap);
        notifyDataSetChanged();
    }

    public void setmGameRequestReceivedMap(HashMap<String, User> gameRequestReceivedMap) {
        mGameRequestReceivedMap.clear();
        mGameRequestReceivedMap.putAll(gameRequestReceivedMap);
        notifyDataSetChanged();
    }

    public void setmUsers(List<User> users) {
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View userView = mInflater.inflate(R.layout.list_users, parent, false);

        final FindFriendsViewHolder findFriendsViewHolder = new FindFriendsViewHolder(userView);

        findFriendsViewHolder.mAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = (User) findFriendsViewHolder.itemView.getTag();
                mListener.OnUserClicked(user);
            }
        });


        findFriendsViewHolder.mInviteGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                User user2 = (User) findFriendsViewHolder.itemView.getTag();
                mGameListener.GameInviteClicked(user2);
            }
        });


        return findFriendsViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FindFriendsViewHolder) holder).populate(mActivity, mUsers.get(position),
                mFriendRequestSentMap,
                mFriendRequestReceivedMap,
                mGameRequestSentMap,
                mGameRequestReceivedMap,
                mCurrentUserFriendsMap);
        /*
        ((FindFriendsViewHolder) holder).gamePopulate(mActivity, mUsers.get(position),
                mGameRequestSentMap,
                mGameRequestReceivedMap,
                mCurrentUserFriendsMap);
        */
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public interface UserListener{
        void OnUserClicked(User user);

    }

    public interface GameInviteListener{
        void GameInviteClicked(User user);
    }
}
