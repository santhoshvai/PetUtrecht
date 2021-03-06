package com.example.svaiyapu.petutrecht.Grid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.svaiyapu.petutrecht.R;
import com.example.svaiyapu.petutrecht.Util.DynamicHeightImageView;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
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