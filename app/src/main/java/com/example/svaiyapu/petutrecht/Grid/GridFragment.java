package com.example.svaiyapu.petutrecht.Grid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.svaiyapu.petutrecht.R;
import com.example.svaiyapu.petutrecht.Util.PetUtil;
import com.example.svaiyapu.petutrecht.data.Model.Pet;

import java.util.ArrayList;
import java.util.List;

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


    public void setPetType(String petType) {
        mPetType = petType;
    }

    public GridFragment() {
        // Requires empty public constructor
    }

    public static GridFragment newInstance(String type) {
        GridFragment g = new GridFragment();
        g.setPetType(type);
        return g;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_TYPE_TAG, mPetType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //Restore the fragment's state here
            mPetType = savedInstanceState.getString(SAVE_TYPE_TAG);
        }

        mPresenter = new GridPresenter(this);

        View v = inflater.inflate(R.layout.fragment_grid, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.pet_grid_recyclerview);

        // Create adapter passing in the sample user data
        mGridAdapter = new GridAdapter(getActivity(), new ArrayList<Pet>());
        // Attach the adapter to the recyclerview to populate items
        mRecyclerView.setAdapter(mGridAdapter);
        // Lookup the swipe container view
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
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

        return v;
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
        if(PetUtil.isDog(mPetType)) {
            mPresenter.loadDogs();
        } else {
            mPresenter.loadCats();
        }
    }

    @Override
    public void setPresenter(GridContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
