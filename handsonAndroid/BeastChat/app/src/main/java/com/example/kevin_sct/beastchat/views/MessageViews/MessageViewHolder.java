package com.example.kevin_sct.beastchat.views.MessageViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.entites.Message;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kevin_sct on 4/30/17.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.list_messages_friendPicture)
    ImageView mFriendPicture;

    @BindView(R.id.list_messages_userPicture)
    ImageView mUserPicture;

    @BindView(R.id.list_messages_UserText)
    TextView mUserText;

    @BindView(R.id.list_messages_friendText)
    TextView mFriendText;

    public MessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void populate(Context context, Message message, String currentUserEmail){
        if (!currentUserEmail.equals(message.getMessageSenderEmail())){
            mUserPicture.setVisibility(View.GONE);
            mUserText.setVisibility(View.GONE);
            mFriendPicture.setVisibility(View.VISIBLE);
            mFriendText.setVisibility(View.VISIBLE);

            Picasso.with(context)
                    .load(message.getMessageSenderPicture())
                    .into(mFriendPicture);
            mFriendText.setText(message.getMessageText());
        } else{
            mUserPicture.setVisibility(View.VISIBLE);
            mUserText.setVisibility(View.VISIBLE);
            mFriendPicture.setVisibility(View.GONE);
            mFriendText.setVisibility(View.GONE);

            Picasso.with(context)
                    .load(message.getMessageSenderPicture())
                    .into(mUserPicture);
            mUserText.setText(message.getMessageText());
        }
    }
}
