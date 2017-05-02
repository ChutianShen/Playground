package com.example.kevin_sct.beastchat.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.Utils.CONSTANT;
import com.example.kevin_sct.beastchat.activities.FriendActivity;
import com.example.kevin_sct.beastchat.activities.InboxActivity;
import com.example.kevin_sct.beastchat.activities.ProfileActivity;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by kevin_sct on 4/24/17.
 */

public class BaseFragment extends Fragment {

    protected CompositeSubscription mCompositeSubscription;
    protected SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeSubscription = new CompositeSubscription();
        mSharedPreferences = getActivity().getSharedPreferences(CONSTANT.USER_INFO_PREFERENCE,
                Context.MODE_PRIVATE);
    }

    public void setUpBottomBar(BottomBar bottomBar, final int index){
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (index){
                    case 1:
                        if(tabId == R.id.tab_profile){
                            Intent intent = new Intent(getActivity(), ProfileActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        } else if(tabId == R.id.tab_friends){
                            Intent intent = new Intent(getActivity(), FriendActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                        break;

                    case 2:
                        if(tabId == R.id.tab_messages){
                            Intent intent = new Intent(getActivity(), InboxActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        } else if(tabId == R.id.tab_profile){
                            Intent intent = new Intent(getActivity(), ProfileActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                        break;

                    case 3:
                        if(tabId == R.id.tab_messages){
                            Intent intent = new Intent(getActivity(), InboxActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        } else if(tabId == R.id.tab_friends){
                            Intent intent = new Intent(getActivity(), FriendActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.clear();
    }
}
