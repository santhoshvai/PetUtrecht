package com.example.svaiyapu.petutrecht.Grid;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.example.svaiyapu.petutrecht.Detail.DetailActivity;
import com.example.svaiyapu.petutrecht.R;
import com.example.svaiyapu.petutrecht.Util.DynamicHeightImageView;
import com.example.svaiyapu.petutrecht.Util.IntentUtil;
import com.example.svaiyapu.petutrecht.Util.PetUtil;
import com.example.svaiyapu.petutrecht.data.Model.Pet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by svaiyapu on 8/26/16.
 */
public class GridFragment extends Fragment implements GridContract.View {

    private RecyclerView mRecyclerView;
    private GridContract.Presenter mPresenter;
    private GridAdapter mGridAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final String LOG_TAG = "GridFragment";
    private static final String SAVE_TYPE_TAG = "mPetType";
    private String mPetType;


    public GridFragment() {
        // Requires empty public constructor
    }

    public static GridFragment newInstance(String type) {
        GridFragment gridFragment = new GridFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SAVE_TYPE_TAG, type);
        gridFragment.setArguments(bundle);
        return gridFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPetType = getArguments().getString(SAVE_TYPE_TAG);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SAVE_TYPE_TAG, mPetType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_grid, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            //Restore the fragment's state
            mPetType = savedInstanceState.getString(SAVE_TYPE_TAG);
        }

        mPresenter = new GridPresenter(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.pet_grid_recyclerview);

        // Create adapter passing in the sample user data
        mGridAdapter = new GridAdapter(getActivity(), new ArrayList<Pet>());
        // Attach the adapter to the recyclerview to populate items
        mRecyclerView.setAdapter(mGridAdapter);
        // Lookup the swipe container view
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.forceUpdate();
                loadData();
            }
        });
        loadData();
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        int noOfCols = getResources().getInteger(R.integer.grid_column_count);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(noOfCols, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addOnItemTouchListener(new OnItemSelectedListener(getActivity()) {
            @Override
            public void onItemSelected(RecyclerView.ViewHolder holder, int position) {
                String petName = ((GridAdapter.ViewHolder)holder).titleTextView.getText().toString();
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(IntentUtil.GRID_TO_DETAIL_PET_NAME, petName);
                intent.putExtra(IntentUtil.GRID_TO_DETAIL_PET_TYPE, mPetType);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    DynamicHeightImageView petImageView = ((GridAdapter.ViewHolder)holder).petImageView;
                    Pair pet_photo_pair = Pair.create(
                            petImageView, // shared view
                            petImageView.getTransitionName() // identifier
                    );
                    ActivityOptions options;
                    // best practice - include window decors during shared element transition
                    // Video: a window into transitions - google IO 2016
                    View statusBar = getActivity().findViewById(android.R.id.statusBarBackground);
                    Pair status_bar_pair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
                    View navigationBar = getActivity().findViewById(android.R.id.navigationBarBackground);
                    if(navigationBar != null) {
                        Pair nav_bar_pair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
                         options = ActivityOptions.makeSceneTransitionAnimation(
                                getActivity(), // launching activity
                                pet_photo_pair,
                                nav_bar_pair,
                                status_bar_pair
                        );
                    } else {
                        // nav bar is not present in some devices. Example: Samsung Galaxy S7
                        options = ActivityOptions.makeSceneTransitionAnimation(
                                getActivity(), // launching activity
                                pet_photo_pair,
                                status_bar_pair
                        );
                    }
                    startActivityForResult(intent,
                            IntentUtil.REQUEST_CODE,
                            options.toBundle());
                } else {
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void showGrid(List<Pet> pets) {
        List<Pet> gridPets = mGridAdapter.getPets();
        if(gridPets.size() > 0) {
            gridPets.clear();
        }
        gridPets.addAll(pets);
        mGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressbar() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressbar() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void loadData() {
        if(mPetType != null) {
            if(PetUtil.isDog(mPetType)) {
                mPresenter.loadDogs();
            } else {
                mPresenter.loadCats();
            }
        }
        else {
            Log.e(LOG_TAG, "mPetType is null");
        }
    }

    @Override
    public void setPresenter(GridContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @TargetApi(21)
    @Override
    /**
     * Return the new shared element to the host activity.
     * Uses the intent selected position data to find that element
     */
    public View activityReenter(Intent data) {
        final String petType = data.getStringExtra(IntentUtil.DETAIL_TO_GRID_PET_TYPE);
        // only act on the correct fragment type, see that using the pet type
        if(PetUtil.petTypeEqual(petType, mPetType)){
            if(mRecyclerView != null) { // on rotation mRecyclerView is null
                getActivity().postponeEnterTransition();
                // Start the postponed transition when the recycler view is ready to be drawn.
                mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                        getActivity().startPostponedEnterTransition();
                        return true;
                    }
                });
                final int selectedItem = data.getIntExtra(IntentUtil.SELECTED_ITEM_POSITION, 0);
                mRecyclerView.scrollToPosition(selectedItem);

                final GridAdapter.ViewHolder holder = (GridAdapter.ViewHolder) mRecyclerView.
                        findViewHolderForAdapterPosition(selectedItem);
                if (holder == null) {
                    Log.e(LOG_TAG, "activityReenter: Holder is null, remapping cancelled.");
                    return null;
                }
                return holder.petImageView;
            }
        }
        return null;
    }

}
