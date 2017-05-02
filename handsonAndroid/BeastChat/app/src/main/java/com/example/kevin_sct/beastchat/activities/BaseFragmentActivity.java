package com.example.kevin_sct.beastchat.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.Utils.CONSTANT;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Created by kevin_sct on 4/23/17.
 */

public abstract class BaseFragmentActivity extends AppCompatActivity {

    abstract Fragment createFragment();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_base);

        SharedPreferences sharedPreferences = getSharedPreferences(CONSTANT.USER_INFO_PREFERENCE,
                Context.MODE_PRIVATE);
        final String userEmail = sharedPreferences.getString(CONSTANT.USER_EMAIL,"");

        mAuth = FirebaseAuth.getInstance();

        if (!((this instanceof LoginActivity) || (this instanceof RegisterActivity))){
            mListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    if (user ==null){
                        Intent intent = new Intent(getApplication(),LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else if(userEmail.equals("")){
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }

                }
            };
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.activity_fragment_base_fragmentContainer);

        if(fragment == null){
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.activity_fragment_base_fragmentContainer, fragment)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!((this instanceof LoginActivity) || (this instanceof RegisterActivity))){
            mAuth.addAuthStateListener(mListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!((this instanceof LoginActivity) || (this instanceof RegisterActivity))){
            mAuth.removeAuthStateListener(mListener);
        }
    }

}
