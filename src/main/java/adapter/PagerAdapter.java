package com.example.foodallergyapp.App.adapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 **************************************
 Created :
 @author : Ian Holderness 14023756
 @version  : 1.0.0
 Last Update:

 Version Updates:

 Refrences:

 **************************************
 */
public class PagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> mFragList;
    ArrayList<String> mFragTitle;

    /**
     * initlises array lists
     * @param fm
     */
    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        mFragList = new ArrayList<>();
        mFragTitle = new ArrayList<>();
    }

    /**
     * Adds the fragment and titles to the array lists
     * @param fragment
     * @param title
     */
    public void addFragment(Fragment fragment, String title){
        mFragTitle.add(title);
        mFragList.add(fragment);
    }

    /**
     * gets the fragment from the array list
     * @param position
     * @return mFragList
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragList.get(position);
    }

    /**
     * get the size of the fragment list array
     * @return mFragList.size()
     */

    @Override
    public int getCount() {
        return mFragList.size();
    }

    /**
     * gets the fragment tiles from the list array
     * @param position
     * @return mFragTitle
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragTitle.get(position);
    }
}
