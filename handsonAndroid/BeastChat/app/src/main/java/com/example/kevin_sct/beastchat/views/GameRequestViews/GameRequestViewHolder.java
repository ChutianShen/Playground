package com.example.kevin_sct.beastchat.views.GameRequestViews;

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
 * Created by kevin_sct on 5/19/17.
 */

public class GameRequestViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.list_game_request_userPicture)
    ImageView userPicture;

    @BindView(R.id.list_game_request_userName)
    TextView userName;

    @BindView(R.id.list_game_request_acceptRequest)
    ImageView approveImageView;

    @BindView(R.id.list_game_request_rejectRequest)
    ImageView rejectImageView;

    public GameRequestViewHolder(View itemView){
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

