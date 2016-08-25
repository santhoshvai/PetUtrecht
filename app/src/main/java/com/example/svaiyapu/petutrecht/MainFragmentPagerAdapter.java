package com.example.svaiyapu.petutrecht;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.example.svaiyapu.petutrecht.Map.MapFragment;
import com.example.svaiyapu.petutrecht.Map.MapPresenter;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;

    private String tabTitles[];
    private Context context;

    FragmentManager mFragmentManager;

    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mFragmentManager = fm;
        this.context = context;
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
        MapFragment mapFragment = MapFragment.newInstance();
        MapPresenter mapPresenter = new MapPresenter(mapFragment);
        return mapFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    /**
     * When there is an orientation change, instantiateItem notices that there is already a fragment
     * for the given position, so it reuses that fragment instead of
     * getting the Fragment from getItem(position).
     * So we need to create a presenter for the fragment again
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object ret = super.instantiateItem(container, position);
        MapFragment mapFragment = (MapFragment) ret;
        if(mapFragment != null) {
            MapPresenter mapPresenter = new MapPresenter(mapFragment);
        }
        return ret;
    }

}