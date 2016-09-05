package com.example.svaiyapu.petutrecht.Grid;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.svaiyapu.petutrecht.R;
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
    public View activityReenter(Intent data) {
        if (data == null) {
            return null;
        }
        Log.d(LOG_TAG, "activityReenter - start");
        // Start the postponed transition when the recycler view is ready to be drawn.
//        mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                Log.d(LOG_TAG, "activityReenter - preDraw");
//                mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
////                mRecyclerView.setVisibility(View.VISIBLE);
//                return true;
//            }
//        });
        final int selectedItem = data.getIntExtra(IntentUtil.SELECTED_ITEM_POSITION, 0);
        mRecyclerView.invalidate();
        mRecyclerView.scrollToPosition(selectedItem);

        final GridAdapter.ViewHolder holder = (GridAdapter.ViewHolder) mRecyclerView.
                findViewHolderForAdapterPosition(selectedItem);
        if (holder == null) {
            Log.w(LOG_TAG, "activityReenter: Holder is null, remapping cancelled.");
            return null;
        }
        return holder.petImageView;

//        setExitSharedElementCallback(new SharedElementCallback() {
//            @Override
//            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
//                Log.d(LOG_TAG, "activityReenter - onMapSharedElements");
//                String transitionName = holder.petImageView.getTransitionName();
//                names.clear();
//                sharedElements.clear();
//                names.add(transitionName);
//                sharedElements.put(transitionName, holder.petImageView);
//            }
//        });

    }


}
