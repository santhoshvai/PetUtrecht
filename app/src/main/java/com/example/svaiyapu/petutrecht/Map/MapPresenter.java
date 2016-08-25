package com.example.svaiyapu.petutrecht.Map;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.svaiyapu.petutrecht.data.Model.Pet;
import com.example.svaiyapu.petutrecht.data.PetDataSource;
import com.example.svaiyapu.petutrecht.data.Remote.PetRemoteDataSource;

import java.util.List;

/**
 * Created by svaiyapu on 8/25/16.
 */
public class MapPresenter implements MapContract.Presenter {

    private static final String LOG_TAG = "MapPresenter";

    private final MapContract.View mMapsView;

    private boolean mFirstLoad = true;

    public MapPresenter(@NonNull MapContract.View mapsView) {
        mMapsView = mapsView;
        mMapsView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadPets() {
        PetRemoteDataSource.getInstance().getPets(new PetDataSource.LoadPetsCallback() {
            @Override
            public void onPetsLoaded(List<Pet> pets) {
                mMapsView.showMapMarkers(pets);
            }
            @Override
            public void onDataNotAvailable() {
                Log.e(LOG_TAG, "onDataNotAvailable");
            }
        });
    }

}
