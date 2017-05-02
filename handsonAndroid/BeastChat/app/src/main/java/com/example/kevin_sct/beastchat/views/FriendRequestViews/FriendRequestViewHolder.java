package com.example.kevin_sct.beastchat.views.FriendRequestViews;

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
 * Created by kevin_sct on 4/29/17.
 */

public class FriendRequestViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.list_friend_request_userPicture)
    ImageView userPicture;

    @BindView(R.id.list_friend_request_userName)
    TextView userName;

    @BindView(R.id.list_friend_request_acceptRequest)
    ImageView approveImageView;

    @BindView(R.id.list_friend_request_rejectRequest)
    ImageView rejectImageView;

    public FriendRequestViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void populate(Context context, User user){
        itemView.setTag(user);
        userName.setText(user.getUserName());

        Picasso.with(context)
                .load(user.getUserPicture())
                .into(userPicture);
    }
}
