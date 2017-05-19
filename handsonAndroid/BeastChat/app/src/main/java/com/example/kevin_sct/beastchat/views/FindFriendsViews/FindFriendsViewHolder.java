package com.example.kevin_sct.beastchat.views.FindFriendsViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.Utils.CONSTANT;
import com.example.kevin_sct.beastchat.entites.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kevin_sct on 4/29/17.
 */

public class FindFriendsViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.list_user_userPicture)
    ImageView mUserPicture;

    @BindView(R.id.list_user_addFriend)
    ImageView mAddFriend;


    @BindView(R.id.list_user_inviteGame)
    ImageView mInviteGame;


    @BindView(R.id.list_user_userName)
    TextView mUserName;

    @BindView(R.id.list_user_userStatus)
    TextView mUserStatus;



    public FindFriendsViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void populate(Context context, User user,
                         HashMap<String, User> friendRequestSentMap,
                         HashMap<String, User> friendRequestReceivedMap,
                         HashMap<String, User> gameRequestSentMap,
                         HashMap<String, User> gameRequestReceivedMap,
                         HashMap<String, User> currentUserFriendMap){
        itemView.setTag(user);
        mUserName.setText(user.getUserName());

        Picasso.with(context)
                .load(user.getUserPicture())
                .into(mUserPicture);


        if(CONSTANT.isIncludedInMap(gameRequestSentMap, user)){
            //mInviteGame.setVisibility(View.GONE);
            //mInviteGame.setImageResource(R.mipmap.ic_launcher);
            //mInviteGame.setVisibility(View.VISIBLE);

            mInviteGame.setImageResource(R.mipmap.ic_cancel_game);
            mInviteGame.setVisibility(View.VISIBLE);
            Toast.makeText(context, "Game " + user.getEmail() + " Game Sent", Toast.LENGTH_SHORT).show();
        } else if(CONSTANT.isIncludedInMap(gameRequestReceivedMap, user)){
            //mInviteGame.setImageResource(R.mipmap.ic_cancel_game);
            //mInviteGame.setVisibility(View.VISIBLE);
            mInviteGame.setVisibility(View.GONE);
            Toast.makeText(context, "Game " + user.getEmail() + " Game Receive", Toast.LENGTH_SHORT).show();
        }  else{
            mInviteGame.setVisibility(View.VISIBLE);
            mInviteGame.setImageResource(R.mipmap.ic_launcher);
            Toast.makeText(context, "Game " + user.getEmail() + " else", Toast.LENGTH_SHORT).show();
        }

        if(CONSTANT.isIncludedInMap(friendRequestSentMap, user)){
            mUserStatus.setVisibility(View.VISIBLE);
            mUserStatus.setText("Friend Request Sent");
            mAddFriend.setImageResource(R.mipmap.ic_cancel_request);
            mAddFriend.setVisibility(View.VISIBLE);
            Toast.makeText(context, "Friend " + user.getEmail() + " Friend Sent", Toast.LENGTH_SHORT).show();
        } else if(CONSTANT.isIncludedInMap(friendRequestReceivedMap, user)) {
            mAddFriend.setVisibility(View.GONE);
            mUserStatus.setVisibility(View.VISIBLE);
            mUserStatus.setText("This user wants to be friend with you!");
            Toast.makeText(context, "Friend " + user.getEmail() + " Friend Receive", Toast.LENGTH_SHORT).show();
        } else if(CONSTANT.isIncludedInMap(currentUserFriendMap, user)){
            mUserStatus.setVisibility(View.VISIBLE);
            mUserStatus.setText("You are friends now!");
            mAddFriend.setVisibility(View.GONE);
            //Toast.makeText(context, user.getEmail() + " Current User Friend", Toast.LENGTH_SHORT).show();
        }
        else{
            mAddFriend.setVisibility(View.VISIBLE);
            mUserStatus.setVisibility(View.GONE);
            mAddFriend.setImageResource(R.mipmap.ic_add);
            Toast.makeText(context, "Friend " + user.getEmail() + " else", Toast.LENGTH_SHORT).show();
        }



    }

    /*
    public void gamePopulate(Context context, User user,
                             HashMap<String, User> gameRequestSentMap,
                             HashMap<String, User> gameRequestReceivedMap,
                             HashMap<String, User> currentUserFriendMap){

        itemView.setTag(user);

        if(CONSTANT.isIncludedInMap(gameRequestSentMap, user)){
            mInviteGame.setVisibility(View.VISIBLE);
            mInviteGame.setImageResource(R.mipmap.ic_cancel_game);
            mInviteGame.setVisibility(View.VISIBLE);
            Toast.makeText(context, "Game Request Sent", Toast.LENGTH_SHORT).show();
        } else if(CONSTANT.isIncludedInMap(gameRequestReceivedMap, user)){
            mInviteGame.setVisibility(View.VISIBLE);
        } else if(CONSTANT.isIncludedInMap(currentUserFriendMap, user)){
            mInviteGame.setVisibility(View.VISIBLE);
            mInviteGame.setImageResource(R.mipmap.ic_cancel_game);
            mInviteGame.setVisibility(View.VISIBLE);
        } else{
            mInviteGame.setVisibility(View.VISIBLE);
            mInviteGame.setImageResource(R.mipmap.ic_launcher);
        }

    }
    */

}
