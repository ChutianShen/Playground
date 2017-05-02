package com.example.kevin_sct.beastchat.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.kevin_sct.beastchat.fragments.MessageFragment;

import java.util.ArrayList;

/**
 * Created by kevin_sct on 4/30/17.
 */

public class MessageActivity extends BaseFragmentActivity {

    public static final String EXTRA_FRIEND_DETAILS = "EXTRA_FRIENDS_DETAILS";

    @Override
    Fragment createFragment() {
        ArrayList<String> friendDetails = getIntent().getStringArrayListExtra(EXTRA_FRIEND_DETAILS);
        getSupportActionBar().setTitle(friendDetails.get(2));
        return MessageFragment.newInstance(friendDetails);
    }

    public static Intent newInstance(Context context, ArrayList<String> friendDetails){
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putStringArrayListExtra(EXTRA_FRIEND_DETAILS, friendDetails);
        return intent;
    }
}
