package com.example.svaiyapu.petutrecht.Detail;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.svaiyapu.petutrecht.Contact.ContactActivity;
import com.example.svaiyapu.petutrecht.R;
import com.example.svaiyapu.petutrecht.Util.IntentUtil;
import com.example.svaiyapu.petutrecht.data.Model.Pet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by svaiyapu on 9/5/16.
 */
public class DetailViewPagerAdapter extends PagerAdapter {

    private final List<Pet> allPets;
    private final Activity host;
    private final LayoutInflater mLayoutInflater;

    public DetailViewPagerAdapter(Activity activity, List<Pet> pets) {
        host = activity;
        allPets = pets;
        mLayoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // inflate XML layout
        ViewGroup layout = (ViewGroup) mLayoutInflater.inflate(R.layout.detail_view,container,false);
        // set the props of the view
        onViewBound(layout, position);
        // dd the newly inflated layout to the collection of Views maintained by the PagerAdapter
        container.addView(layout);
        // return the newly inflated layout
        return layout;
    }

    private void onViewBound(ViewGroup layout, int position) {
        final Pet pet = allPets.get(position);
        final int image_vibrant_color = Color.parseColor(pet.getImg_colour());

        // change the status bar color - only for lollipop and above this is possible
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = host.getWindow();
            window.setStatusBarColor(image_vibrant_color);
        }

        final TextView titleTextView = (TextView) layout.findViewById(R.id.main_title);
        final TextView breedTextView = (TextView) layout.findViewById(R.id.breed_detail);
        final TextView ageTextView = (TextView) layout.findViewById(R.id.age_detail);
        final TextView descriptionTextView = (TextView) layout.findViewById(R.id.description_detail);
        final TextView genderTextView = (TextView) layout.findViewById(R.id.gender_detail);
        final FloatingActionButton fab = (FloatingActionButton) layout.findViewById(R.id.fab);

        titleTextView.setText(pet.getName());
        breedTextView.setText(pet.getBreed());
        ageTextView.setText(pet.getAge());
        genderTextView.setText(pet.getGender());
        descriptionTextView.setText(pet.getDescription());
        Picasso.with(host).
                load(pet.getImg_primary()).
                error(R.drawable.placeholder).
                into((ImageView) layout.findViewById(R.id.main_backdrop));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startContactActivity(pet.getName(), fab);
            }
        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (layout.findViewById(R.id.main_backdrop))
                    .setTransitionName(pet.getImg_primary());
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    // This method checks whether a particular object belongs to a given position
    @Override
    public boolean isViewFromObject(View view, Object object) {
        // the second parameter is of type Object and is the same as the return value from the instantiateItem method.
        return view == object;
    }

    @Override
    public int getCount() {
        return allPets.size();
    }

    private void startContactActivity(String petName, FloatingActionButton fab) {
        final Intent intent = new Intent(host, ContactActivity.class);
        intent.putExtra(IntentUtil.DETAIL_TO_CONTACT_PET_NAME, petName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(host, fab, fab.getTransitionName());
            host.startActivity(intent, options.toBundle());
        } else {
            host.startActivity(intent);
        }
    }
}
