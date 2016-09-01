package com.example.svaiyapu.petutrecht.Detail;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.svaiyapu.petutrecht.Contact.ContactActivity;
import com.example.svaiyapu.petutrecht.R;
import com.example.svaiyapu.petutrecht.data.Model.Pet;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements DetailContract.View {

    private DetailContract.Presenter mPresenter;
    private TextView mBreedTextView;
    private TextView mAgeTextView;
    private TextView mDescriptionTextView;
    private TextView mGenderTextView;
    private TextView mTitleTextView;
    private FloatingActionButton mFab;
    private AppCompatActivity mAppCompatActivity;

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
            transitions.addTransition(slide);
            transitions.addTransition(new Fade());
            getWindow().setEnterTransition(transitions);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(navigationOnClickListener);

        Intent intent = getIntent();
        final String message_pet_name = intent.getStringExtra(getResources()
                .getString(R.string.detail_Activity_pet_name));

        mPresenter = new DetailPresenter(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startContactActivity(message_pet_name);
            }
        });

        mTitleTextView = (TextView) findViewById(R.id.main_title);
        mBreedTextView = (TextView) findViewById(R.id.breed_detail);
        mAgeTextView = (TextView) findViewById(R.id.age_detail);
        mDescriptionTextView = (TextView) findViewById(R.id.description_detail);
        mGenderTextView = (TextView) findViewById(R.id.gender_detail);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mPresenter.loadPet(message_pet_name);
        super.onCreate(savedInstanceState);
    }

    private void startContactActivity(String petName) {
        final Intent intent = new Intent(mAppCompatActivity, ContactActivity.class);
        String pet_message_id = mAppCompatActivity.getResources().getString(R.string.detail_Activity_pet_name);
        intent.putExtra(pet_message_id, petName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(this, mFab, mFab.getTransitionName());
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
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
    public void showDetail(Pet pet) {
        int image_vibrant_color = Color.parseColor(pet.getImg_colour());

        // change the status bar color - only for lollipop and above this is possible
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(image_vibrant_color);
        }
        mTitleTextView.setText(pet.getName());
        mBreedTextView.setText(pet.getBreed());
        mAgeTextView.setText(pet.getAge());
        mGenderTextView.setText(pet.getGender());
        mDescriptionTextView.setText(pet.getDescription());
        Picasso.with(this).
                load(pet.getImg_primary()).
                error(R.drawable.placeholder).
                into((ImageView) findViewById(R.id.main_backdrop),
                        new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                //Success image already loaded into the view
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    startPostponedEnterTransition();
                                }
                            }

                            @Override
                            public void onError() {
                                // Error placeholder image already loaded into the view, do further handling of this situation here

                            }
                        });
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
