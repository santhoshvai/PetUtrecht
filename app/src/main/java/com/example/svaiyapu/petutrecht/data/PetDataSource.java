package com.example.svaiyapu.petutrecht.data;

import android.support.annotation.NonNull;

import com.example.svaiyapu.petutrecht.data.Model.Pet;

import java.util.List;

/**
 * Created by svaiyapu on 8/25/16.
 */
public interface PetDataSource {

    interface LoadPetsCallback {

        void onPetsLoaded(List<Pet> pets);

        void onDataNotAvailable();
    }

    void getPets(@NonNull LoadPetsCallback callback);
}
