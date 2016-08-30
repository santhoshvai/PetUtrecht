package com.example.svaiyapu.petutrecht.Detail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.svaiyapu.petutrecht.MainActivity;
import com.example.svaiyapu.petutrecht.R;
import com.example.svaiyapu.petutrecht.Util.PetUtil;
import com.example.svaiyapu.petutrecht.data.Model.Pet;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements DetailContract.View {

    private DetailContract.Presenter mPresenter;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView mBreedTextView;
    private TextView mAgeTextView;
    private TextView mDescriptionTextView;
    private TextView mGenderTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String message_pet_name = intent.getStringExtra(getResources()
                .getString(R.string.detail_Activity_pet_name));

        mPresenter = new DetailPresenter(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action: " + message_pet_name, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // display the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        mCollapsingToolbarLayout.setTitle(message_pet_name);

        mBreedTextView = (TextView) findViewById(R.id.breed_detail);
        mAgeTextView = (TextView) findViewById(R.id.age_detail);
        mDescriptionTextView = (TextView) findViewById(R.id.description_detail);
        mGenderTextView = (TextView) findViewById(R.id.gender_detail);

        mPresenter.loadPet(message_pet_name);
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
        Picasso.with(this).load(pet.getImg_primary()).into( (ImageView) findViewById(R.id.main_backdrop));

        int image_vibrant_color = Color.parseColor(pet.getImg_colour());

        // change the status bar color - only for lollipop and above this is possible
        if(android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.setStatusBarColor(image_vibrant_color);
            mCollapsingToolbarLayout.setContentScrimColor(image_vibrant_color);
            mCollapsingToolbarLayout.setCollapsedTitleTextColor(
                    PetUtil.getContrastColor(image_vibrant_color));
        }

        mBreedTextView.setText(pet.getBreed());
        mAgeTextView.setText(pet.getAge());
        mGenderTextView.setText(pet.getGender());
        mDescriptionTextView.setText(pet.getDescription());
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
