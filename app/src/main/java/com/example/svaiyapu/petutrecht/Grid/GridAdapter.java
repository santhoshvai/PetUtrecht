package com.example.svaiyapu.petutrecht.Grid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.svaiyapu.petutrecht.Detail.DetailActivity;
import com.example.svaiyapu.petutrecht.R;
import com.example.svaiyapu.petutrecht.Util.DynamicHeightImageView;
import com.example.svaiyapu.petutrecht.Util.IntentUtil;
import com.example.svaiyapu.petutrecht.Util.PetUtil;
import com.example.svaiyapu.petutrecht.data.Model.Pet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by svaiyapu on 8/26/16.
 */
public class GridAdapter extends
        RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private static final String LOG_TAG = "GridAdapter";
    // Store a member variable for the contacts
    private List<Pet> mPets;
    // Store the context for easy access
    private Activity mActivity;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titleTextView;
        public TextView breedTextView;
        public DynamicHeightImageView petImageView;

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.pet_title);
            breedTextView = (TextView) itemView.findViewById(R.id.pet_breed);
            petImageView = (DynamicHeightImageView) itemView.findViewById(R.id.pet_thumbnail);
            // Attach a click listener to the entire row view
            itemView.setOnClickListener(this);
        }

        // Handles the grid item being clicked
        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            Pet pet = mPets.get(position);
            Intent intent = new Intent(mActivity, DetailActivity.class);
            intent.putExtra(IntentUtil.GRID_TO_DETAIL_PET_NAME, pet.getName());
            intent.putExtra(IntentUtil.GRID_TO_DETAIL_PET_TYPE, pet.getType());
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Pair pet_photo_pair = Pair.create(
                        petImageView, // shared view
                        petImageView.getTransitionName() // identifier
                );
                // best practice - inlcude window decors during shared element transition
                // Video: a window into transitions - google IO 2016
                View navigationBar = mActivity.findViewById(android.R.id.navigationBarBackground);
                Pair nav_bar_pair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
                View statusBar = mActivity.findViewById(android.R.id.statusBarBackground);
                Pair status_bar_pair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(
                                mActivity, // launching activity
                                pet_photo_pair,
                                nav_bar_pair,
                                status_bar_pair
                        );
                mActivity.startActivityForResult(intent,
                        IntentUtil.REQUEST_CODE,
                        options.toBundle());
            } else {
                mActivity.startActivity(intent);
            }
        }

    }

    // Pass in the pet array into the constructor
    public GridAdapter(Activity activity, List<Pet> pets) {
        mPets = pets;
        mActivity = activity;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mActivity;
    }

    // inflate the item layout and create the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.grid_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Pet pet = mPets.get(position);
        int colorBackground = Color.parseColor(pet.getImg_colour());
        int colorText = PetUtil.getContrastColor(colorBackground);

        holder.titleTextView.setText(pet.getName());
        holder.titleTextView.setTextColor(colorText);

        holder.breedTextView.setText(pet.getBreed());
        holder.breedTextView.setTextColor(colorText);

        // set cardview background
        ((CardView)holder.itemView)
                .setCardBackgroundColor(colorBackground);

        // `holder.petImageView` should be of type `DynamicHeightImageView`
        // Set the height ratio before loading in image into Picasso
        holder.petImageView.setHeightRatio(((double)pet.getImg_height())
                /pet.getImg_width());

        Picasso.with(mActivity)
                .load(pet.getImg_primary()) // image url goes here
                .placeholder(R.drawable.placeholder)
                .into(holder.petImageView);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.petImageView.setTransitionName(pet.getImg_primary());
        }
    }

    // determine the number of items
    @Override
    public int getItemCount() {
        return mPets.size();
    }

    public List<Pet> getPets() {
        return mPets;
    }
}