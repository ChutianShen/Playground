package com.example.kevin_sct.beastchat.views.UserFriendViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.entites.User;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kevin_sct on 4/30/17.
 */

public class UserFriendViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.list_users_friends_friendImageView)
    ImageView mUserPicture;

    @BindView(R.id.list_users_friends_userName)
    TextView mUserName;

    @BindView(R.id.list_users_friends_startChat)
    ImageView mStartChat;

    public UserFriendViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void populate(Context context, User user){
        itemView.setTag(user);

        Picasso.with(context)
                .load(user.getUserPicture())
                .into(mUserPicture);

        mUserName.setText(user.getUserName());
    }
}
