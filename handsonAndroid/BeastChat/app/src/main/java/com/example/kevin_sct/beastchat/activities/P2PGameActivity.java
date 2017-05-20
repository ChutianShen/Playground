package com.example.kevin_sct.beastchat.activities;

import android.support.v4.app.Fragment;

import com.example.kevin_sct.beastchat.fragments.P2PGameFragment;

/**
 * Created by kevin_sct on 5/19/17.
 */

public class P2PGameActivity extends BaseFragmentActivity {
    @Override
    Fragment createFragment() {
        return P2PGameFragment.newInstance();
    }
}
