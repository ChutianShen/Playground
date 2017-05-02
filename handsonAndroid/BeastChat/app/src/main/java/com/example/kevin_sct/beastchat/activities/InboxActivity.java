package com.example.kevin_sct.beastchat.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.Utils.CONSTANT;
import com.example.kevin_sct.beastchat.fragments.InboxFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by kevin_sct on 4/28/17.
 */

public class InboxActivity extends BaseFragmentActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String messageToken = FirebaseInstanceId.getInstance().getToken();

        SharedPreferences sharedPreferences = getSharedPreferences(CONSTANT.USER_INFO_PREFERENCE, Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString(CONSTANT.USER_EMAIL, "");

        if(messageToken != null && !userEmail.equals("")){
            Log.i(InboxActivity.class.getSimpleName(), messageToken);
            DatabaseReference tokenReference = FirebaseDatabase.getInstance().getReference()
                    .child(CONSTANT.FIRE_BASE_PATH_USER_TOKEN).child(CONSTANT.encodeEmail(userEmail));

            tokenReference.child("token").setValue(messageToken);
            getSupportActionBar().setTitle(sharedPreferences.getString(CONSTANT.USER_NAME,"") + "'s Inbox");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_create_new_message:
                Intent intent = new Intent(getApplication(),FriendActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                finish();
                return true;
        }

        return true;
    }

    @Override
    Fragment createFragment() {
        return InboxFragment.newInstance();
    }
}
