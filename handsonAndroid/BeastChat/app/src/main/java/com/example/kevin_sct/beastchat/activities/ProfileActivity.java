package com.example.kevin_sct.beastchat.activities;

import android.support.v4.app.Fragment;

import com.example.kevin_sct.beastchat.fragments.ProfileFragment;

/**
 * Created by kevin_sct on 4/28/17.
 */

public class ProfileActivity extends BaseFragmentActivity {
    @Override
    Fragment createFragment(){
        return ProfileFragment.newInstance();
    }
}
