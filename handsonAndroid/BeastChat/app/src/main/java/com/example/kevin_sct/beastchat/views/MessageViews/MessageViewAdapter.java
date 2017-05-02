package com.example.kevin_sct.beastchat.views.MessageViews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.activities.BaseFragmentActivity;
import com.example.kevin_sct.beastchat.entites.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin_sct on 4/30/17.
 */

public class MessageViewAdapter extends RecyclerView.Adapter {

    private BaseFragmentActivity mActivity;
    private List<Message> mMessages;
    private LayoutInflater mInflator;
    private String mCurrentUserEmail;

    public MessageViewAdapter(BaseFragmentActivity mActivity, String mCurrentUserEmail) {
        this.mActivity = mActivity;
        this.mCurrentUserEmail = mCurrentUserEmail;
        mInflator = mActivity.getLayoutInflater();
        mMessages = new ArrayList<>();
    }

    public void setmMessages(List<Message> messages) {
        mMessages.clear();
        mMessages.addAll(messages);
        notifyDataSetChanged();
    }

    public List<Message> getmMessages() {
        return mMessages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflator.inflate(R.layout.list_messages,parent,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MessageViewHolder) holder).populate(mActivity,mMessages.get(position),mCurrentUserEmail);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }
}
