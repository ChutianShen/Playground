package com.example.kevin_sct.beastchat.views.ChatRoomViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.entites.ChatRoom;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kevin_sct on 5/1/17.
 */

public class ChatRoomViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.list_chat_room_lastMessage)
    TextView mLastMessage;

    @BindView(R.id.list_chat_room_newMessageIndicator)
    ImageView mLastMessageIndicator;

    @BindView(R.id.list_chat_room_userName)
    TextView mUserName;

    @BindView(R.id.list_chat_room_userPicture)
    ImageView mUserPicture;

    public ChatRoomViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void populate(Context context, ChatRoom chatRoom, String currentUserEmail){
        itemView.setTag(chatRoom);

        Picasso.with(context)
                .load(chatRoom.getFriendPicture())
                .into(mUserPicture);

        mUserName.setText(chatRoom.getFriendName());

        String lastMessageSent = chatRoom.getLastMessage();

        if (lastMessageSent.length()>40){
            lastMessageSent = lastMessageSent.substring(0,40) + " ...";
        }

        if (!chatRoom.isSentLastMessage()){
            lastMessageSent = lastMessageSent + " (Draft)";
        }

        if (chatRoom.getLastMessageSenderEmail().equals(currentUserEmail)){
            lastMessageSent = "Me: " + lastMessageSent;
        }

        if (!chatRoom.isLastMessageRead()){
            mLastMessageIndicator.setVisibility(View.VISIBLE);
        } else{
            mLastMessageIndicator.setVisibility(View.GONE);
        }

        mLastMessage.setText(lastMessageSent);
    }
}
