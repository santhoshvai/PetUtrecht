package com.example.svaiyapu.petutrecht;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.svaiyapu.petutrecht.Grid.GridFragment;
import com.example.svaiyapu.petutrecht.Map.MapFragment;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;

    private String tabTitles[];
    private static String LOG_TAG = "MainFragmentPagerAdapter";

    FragmentManager mFragmentManager;

    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mFragmentManager = fm;
        // tab titles are stored in arrays.xml
        tabTitles = context.getResources().getStringArray(R.array.tab_names);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    /**
     * Called when new fragments are needed
     */
    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            MapFragment mapFragment = MapFragment.newInstance();
            return mapFragment;
        } else {
            GridFragment gridFragment = GridFragment.newInstance(tabTitles[position]);
            return gridFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


}