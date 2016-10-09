package com.jashlabs.bunkin.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jashlabs.bunkin.Fragments.BuddyListFragment;
import com.jashlabs.bunkin.Fragments.RecentChatFragment;

/**
 * Created by Jaswanth on 24-02-2016.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a ConnectionsFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return RecentChatFragment.newInstance();
            case 1:
              return BuddyListFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "CHATS";
            case 1:
                return "CONTACTS";
        }
        return null;
    }
}
