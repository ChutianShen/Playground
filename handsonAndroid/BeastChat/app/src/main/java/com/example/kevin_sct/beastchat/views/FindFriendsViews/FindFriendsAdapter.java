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

    private HashMap<String, User> mFriendRequestSentMap;
    private HashMap<String, User> mFriendRequestReceivedMap;
    private HashMap<String, User> mCurrentUserFriendsMap;

    public FindFriendsAdapter(BaseFragmentActivity mActivity, UserListener mListener) {
        this.mActivity = mActivity;
        this.mListener = mListener;
        mInflater = mActivity.getLayoutInflater();
        mUsers = new ArrayList<>();
        mFriendRequestSentMap = new HashMap<>();
        mFriendRequestReceivedMap = new HashMap<>();
        mCurrentUserFriendsMap = new HashMap<>();

    }

    public void setmFriendRequestSentMap(HashMap<String, User> friendRequestSentMap) {
        mFriendRequestSentMap.clear();
        mFriendRequestSentMap.putAll(friendRequestSentMap);
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
        return findFriendsViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FindFriendsViewHolder) holder).populate(mActivity, mUsers.get(position),
                mFriendRequestSentMap,
                mFriendRequestReceivedMap,
                mCurrentUserFriendsMap);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public interface UserListener{
        void OnUserClicked(User user);
    }
}
