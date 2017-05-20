package com.example.kevin_sct.beastchat.activities;

import android.support.v4.app.Fragment;

import com.example.kevin_sct.beastchat.fragments.RSPGameFragment;

/**
 * Created by kevin_sct on 5/19/17.
 */

public class RSPActivity extends BaseFragmentActivity {
    @Override
    Fragment createFragment() {
        return RSPGameFragment.newinstance();
    }
}
