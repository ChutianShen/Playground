package com.example.kevin_sct.beastchat.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.kevin_sct.beastchat.fragments.RSPGameFragment;

import java.util.ArrayList;

/**
 * Created by kevin_sct on 5/19/17.
 */

public class RSPActivity extends BaseFragmentActivity {

    public static final String EXTRA_GAME_DETAILS = "EXTRA_GAME_DETAILS";

    @Override
    Fragment createFragment() {
        ArrayList<String> gameFriendDetails = getIntent().getStringArrayListExtra(EXTRA_GAME_DETAILS);
        //getSupportActionBar().setTitle(friendDetails.get(2));
        return RSPGameFragment.newInstance(gameFriendDetails);
    }

    public static Intent newInstance(Context context, ArrayList<String> gameFriendDetails){
        Intent intent = new Intent(context, RSPActivity.class);
        intent.putStringArrayListExtra(EXTRA_GAME_DETAILS, gameFriendDetails);
        return intent;
    }
}
