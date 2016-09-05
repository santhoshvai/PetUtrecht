package com.example.svaiyapu.petutrecht.Detail;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import com.example.svaiyapu.petutrecht.R;
import com.example.svaiyapu.petutrecht.Util.IntentUtil;
import com.example.svaiyapu.petutrecht.data.Model.Pet;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements DetailContract.View {

    private DetailContract.Presenter mPresenter;
    private AppCompatActivity mAppCompatActivity;
    private ViewPager mViewPager;
    private int mInitialItem;

    private final View.OnClickListener navigationOnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.finishAfterTransition(mAppCompatActivity);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);
        mAppCompatActivity = this;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
            TransitionSet transitions = new TransitionSet();
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.setInterpolator(AnimationUtils.loadInterpolator(this,
                    android.R.interpolator.linear_out_slow_in));
            slide.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
            // best practice - exclude window decors during shared element transition
            // Video: a window into transitions - google IO 2016
            slide.excludeTarget(android.R.id.statusBarBackground, true);
            slide.excludeTarget(android.R.id.navigationBarBackground, true);

            transitions.addTransition(slide);
            transitions.addTransition(new Fade());
            getWindow().setEnterTransition(transitions);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(navigationOnClickListener);

        Intent intent = getIntent();
        final String message_pet_name = intent.getStringExtra(IntentUtil.GRID_TO_DETAIL_PET_NAME);
        final String message_pet_type = intent.getStringExtra(IntentUtil.GRID_TO_DETAIL_PET_TYPE);

        mPresenter = new DetailPresenter(this);
        mPresenter.loadPet(message_pet_name, message_pet_type);

        super.onCreate(savedInstanceState);
    }

    private void setUpViewPager(List<Pet> pets) {
        mViewPager = (ViewPager) findViewById(R.id.detail_pager);
        mViewPager.setAdapter(new DetailViewPagerAdapter(this, pets));
        mViewPager.setCurrentItem(mInitialItem);

        mViewPager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mViewPager.getChildCount() > 0) {
                    mViewPager.removeOnLayoutChangeListener(this);
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startPostponedEnterTransition();
                    }
                }
            }
        });

        mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.padding_mini));
        mViewPager.setPageMarginDrawable(R.drawable.page_margin);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showDetail(Pet pet, List<Pet> pets) {
        // set the initial item
        mInitialItem = pets.indexOf(pet);
        // setup viewpager
        setUpViewPager(pets);
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @TargetApi(21)
    private void setActivityResult() {
        if (mInitialItem == mViewPager.getCurrentItem()) {
            setResult(RESULT_OK);
            return;
        }
//        getWindow().setExitTransition(new Fade());
        Intent intent = new Intent();
        intent.putExtra(IntentUtil.SELECTED_ITEM_POSITION, mViewPager.getCurrentItem());
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onBackPressed() {
        setActivityResult();
        super.onBackPressed();
    }

    @Override
    public void finishAfterTransition() {
        setActivityResult();
        super.finishAfterTransition();
    }
}
