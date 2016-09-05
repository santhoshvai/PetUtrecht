package com.example.svaiyapu.petutrecht;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.svaiyapu.petutrecht.Grid.GridContract;
import com.example.svaiyapu.petutrecht.Grid.GridFragment;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
//        getWindow().setReenterTransition(new Fade());

        FragmentManager fm = getSupportFragmentManager();
        for(Fragment fragment: fm.getFragments()) {
            if(fragment instanceof GridContract.View) {
                final View view = ((GridContract.View)fragment).activityReenter(data);
                if(view != null) {
//                    postponeEnterTransition();
                    MainSharedElementCallback mainSharedElementCallback = new MainSharedElementCallback();
                    mainSharedElementCallback.setSharedView(view);
                    setExitSharedElementCallback(mainSharedElementCallback);
//                    setExitSharedElementCallback(new SharedElementCallback() {
//                        @Override
//                        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
//                            if(view != null) {
//                                Log.d(LOG_TAG, "onMapSharedElements - view not null");
////                                names.clear();
////                                sharedElements.clear();
//                                names.add(view.getTransitionName());
//                                sharedElements.put(view.getTransitionName(), view);
//                            }
//                        }
//                    });
//                    view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                        @Override
//                        public boolean onPreDraw() {
//                            view.getViewTreeObserver().removeOnPreDrawListener(this);
////                            startPostponedEnterTransition();
//                            return true;
//                    }
//                    });
                }

            }
        }

    }

}
