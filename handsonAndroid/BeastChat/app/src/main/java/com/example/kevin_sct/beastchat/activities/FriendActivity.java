package com.example.kevin_sct.beastchat.activities;

import android.support.v4.app.Fragment;

import com.example.kevin_sct.beastchat.fragments.FriendFragment;

/**
 * Created by kevin_sct on 4/28/17.
 */

public class FriendActivity extends BaseFragmentActivity {
    @Override
    Fragment createFragment(){
        return FriendFragment.newInstance();
    }
}
