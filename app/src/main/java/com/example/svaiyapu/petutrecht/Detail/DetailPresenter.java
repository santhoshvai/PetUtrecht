package com.example.svaiyapu.petutrecht.Detail;

import android.support.annotation.NonNull;

import com.example.svaiyapu.petutrecht.data.Model.Pet;
import com.example.svaiyapu.petutrecht.data.Remote.PetRemoteDataSource;

/**
 * Created by svaiyapu on 8/29/16.
 */
public class DetailPresenter implements  DetailContract.Presenter{
    private static final String LOG_TAG = "GridPresenter";
    private static DetailContract.View mDetailView;

    public DetailPresenter(@NonNull DetailContract.View detailView) {
        mDetailView = detailView;
        mDetailView.setPresenter(this);
    }

    @Override
    public void loadPet(String petName) {
        Pet pet = PetRemoteDataSource.getInstance().getPet(petName);
        mDetailView.showDetail(pet);
    }

    @Override
    public void start() {

    }
}
