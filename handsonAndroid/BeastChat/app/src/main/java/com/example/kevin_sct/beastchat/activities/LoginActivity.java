package com.example.kevin_sct.beastchat.activities;

import android.support.v4.app.Fragment;

import com.example.kevin_sct.beastchat.fragments.LoginFragment;


public class LoginActivity extends BaseFragmentActivity {

    @Override
    Fragment createFragment(){
        return LoginFragment.newInstance();
    }

}
