package com.example.kevin_sct.beastchat.activities;

import android.support.v4.app.Fragment;

import com.example.kevin_sct.beastchat.fragments.Connect3Fragment;

/**
 * Created by kevin_sct on 5/15/17.
 */

public class GameActivity extends BaseFragmentActivity {
    @Override
    Fragment createFragment(){
        return Connect3Fragment.newInstance();
    }


}
