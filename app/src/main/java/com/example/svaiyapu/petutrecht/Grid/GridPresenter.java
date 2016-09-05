package com.example.svaiyapu.petutrecht.Grid;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.svaiyapu.petutrecht.Map.MapContract;
import com.example.svaiyapu.petutrecht.Util.PetUtil;
import com.example.svaiyapu.petutrecht.data.Model.Pet;
import com.example.svaiyapu.petutrecht.data.PetDataSource;
import com.example.svaiyapu.petutrecht.data.Remote.PetRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by svaiyapu on 8/26/16.
 */
public class GridPresenter implements GridContract.Presenter {

    GridContract.View mGridView;
    private static final String LOG_TAG = "GridPresenter";

    public GridPresenter(@NonNull GridContract.View gridView) {
        mGridView = gridView;
        mGridView.setPresenter(this);
    }

    @Override
    public void loadDogs() {
        mGridView.showProgressbar();
        PetRemoteDataSource.getInstance().getPets(new PetDataSource.LoadPetsCallback() {
            @Override
            public void onPetsLoaded(List<Pet> pets) {
                List<Pet> dogs = PetUtil.filterDogs(pets);
                mGridView.showGrid(dogs);
                mGridView.hideProgressbar();
            }
            @Override
            public void onDataNotAvailable() {
                Log.e(LOG_TAG, "onDataNotAvailable");
                mGridView.hideProgressbar();
            }
        });
    }

    @Override
    public void loadCats() {
        mGridView.showProgressbar();
        PetRemoteDataSource.getInstance().getPets(new PetDataSource.LoadPetsCallback() {
            @Override
            public void onPetsLoaded(List<Pet> pets) {
                List<Pet> cats = PetUtil.filterCats(pets);
                mGridView.showGrid(cats);
                mGridView.hideProgressbar();
            }
            @Override
            public void onDataNotAvailable() {
                Log.e(LOG_TAG, "onDataNotAvailable");
                mGridView.hideProgressbar();
            }
        });

    }

    @Override
    public void start() {
    }

    @Override
    public void forceUpdate() {
        PetRemoteDataSource.getInstance().invalidateCache();
    }
}
