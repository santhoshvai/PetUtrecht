package com.example.svaiyapu.petutrecht;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.util.Log;
import android.view.View;

import com.example.svaiyapu.petutrecht.Grid.GridContract;
import com.example.svaiyapu.petutrecht.Util.IntentUtil;
import com.example.svaiyapu.petutrecht.Util.MainSharedElementCallback;
import com.example.svaiyapu.petutrecht.Util.TransitionCallback;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Listener to reset shared element exit transition callbacks.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getSharedElementExitTransition().addListener(new TransitionCallback() {
                @Override
                @TargetApi(21)
                public void onTransitionEnd(Transition transition) {
                    ((Activity) MainActivity.this).setExitSharedElementCallback(null);
                }
            });
        }

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        FragmentPagerAdapter pageAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this);
        viewPager.setAdapter(pageAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    // called before shared element for exit comes back, need to tweak for viewpager
    @TargetApi(21)
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if(! IntentUtil.hasAll(data,
                IntentUtil.SELECTED_ITEM_POSITION,
                IntentUtil.DETAIL_TO_GRID_PET_TYPE)) {
            Log.e(LOG_TAG, "onActivityReenter - Intent does not have everything");
            Log.e(LOG_TAG, IntentUtil.intentToString(data));
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        for(Fragment fragment: fm.getFragments()) {
            if(fragment instanceof GridContract.View) {
                View view = ((GridContract.View)fragment).activityReenter(data);
                if(view != null) {
                    MainSharedElementCallback mainSharedElementCallback = new MainSharedElementCallback();
                    mainSharedElementCallback.setSharedView(view);
                    setExitSharedElementCallback(mainSharedElementCallback);
                }

            }
        }
    }

}
