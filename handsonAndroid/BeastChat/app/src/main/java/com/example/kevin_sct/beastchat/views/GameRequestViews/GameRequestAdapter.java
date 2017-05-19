package com.example.kevin_sct.beastchat.views.GameRequestViews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.activities.BaseFragmentActivity;
import com.example.kevin_sct.beastchat.entites.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin_sct on 5/19/17.
 */

public class GameRequestAdapter extends RecyclerView.Adapter {

    private BaseFragmentActivity mActivity;
    private LayoutInflater mInflater;
    private List<User> mUsers;
    private GameRequestAdapter.GameOnOptionListener mGameListener;

    public GameRequestAdapter(BaseFragmentActivity mActivity, GameOnOptionListener mListener) {
        this.mActivity = mActivity;
        this.mGameListener = mListener;
        mInflater = mActivity.getLayoutInflater();
        mUsers = new ArrayList<>();
    }


    public void setmUsers(List<User> users) {
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_game_requests,parent,false);
        final GameRequestViewHolder gameRequestsViewHolder = new GameRequestViewHolder(view);

        gameRequestsViewHolder.approveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = (User) gameRequestsViewHolder.itemView.getTag();
                mGameListener.GameOnOptionClicked(user,"0");
            }
        });

        gameRequestsViewHolder.rejectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = (User) gameRequestsViewHolder.itemView.getTag();
                mGameListener.GameOnOptionClicked(user,"1");
            }
        });

        return gameRequestsViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((GameRequestViewHolder) holder).populate(mActivity,mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public interface GameOnOptionListener{
        void GameOnOptionClicked(User user, String result);
    }
}
