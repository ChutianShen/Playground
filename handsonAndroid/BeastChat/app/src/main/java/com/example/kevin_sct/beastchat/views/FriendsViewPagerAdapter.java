package com.example.kevin_sct.beastchat.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.kevin_sct.beastchat.fragments.FindFriendFragment;
import com.example.kevin_sct.beastchat.fragments.FriendRequestFragment;
import com.example.kevin_sct.beastchat.fragments.UserFriendsFragment;

/**
 * Created by kevin_sct on 4/28/17.
 */

public class FriendsViewPagerAdapter extends FragmentStatePagerAdapter {
    public FriendsViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    public Fragment getItem(int position){
        Fragment returnFragment;

        switch (position){
            case 0:
                returnFragment = UserFriendsFragment.newInstance();
                break;
            case 1:
                returnFragment = FriendRequestFragment.newInstance();
                break;
            case 2:
                returnFragment = FindFriendFragment.newInstance();
                break;

            default:
                return null;
        }
        return returnFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title;

        switch (position){
            case 0:
                title = "Friends";
                break;
            case 1:
                title = "Requests";
                break;
            case 2:
                title = "Find Friends";
                break;
            default:
                return null;
        }

        return title;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
