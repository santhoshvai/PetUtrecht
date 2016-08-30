package com.example.svaiyapu.petutrecht.data.Remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.svaiyapu.petutrecht.data.Model.Pet;
import com.example.svaiyapu.petutrecht.data.Model.RemoteResponse;
import com.example.svaiyapu.petutrecht.data.PetDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by svaiyapu on 8/25/16.
 */
public class PetRemoteDataSource implements PetDataSource {

    private static PetRemoteDataSource INSTANCE = null;
    private static final String LOG_TAG = "PetRemoteDataSource";

    private Map<String, Pet> mCachedPets;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested.
     */
    boolean mCacheIsDirty = false;

    public static PetRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PetRemoteDataSource();
        }
        return INSTANCE;
    }


    // Prevent direct instantiation.
    private PetRemoteDataSource() {}

    /**
     * Contact server here for data
     * when data is found, send to {@link LoadPetsCallback#onPetsLoaded(List)} nPetsLoaded()}
     * if not, send the network error to {@link LoadPetsCallback#onDataNotAvailable()}
     */
    @Override
    public void getPets(final @NonNull LoadPetsCallback callback) {
        // Respond immediately with cache if available and not dirty
        if (mCachedPets != null && !mCacheIsDirty) {
            callback.onPetsLoaded(
                    new ArrayList<>(mCachedPets.values()));
            return;
        }
        Call<RemoteResponse> call = PetRetrofit.getInstance().getCallInstance();
        call.enqueue(new Callback<RemoteResponse>() {
            @Override
            public void onResponse(Call<RemoteResponse> call, Response<RemoteResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, "getPets : Pet data fetched from json");
                    final List<Pet> pets = response.body().getData();
                    refreshCache(pets);
                    callback.onPetsLoaded(
                            new ArrayList<>(mCachedPets.values()));
                } else {
                    // an http error code from server like 401
                    Log.e(LOG_TAG, response.errorBody().toString());
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<RemoteResponse> call, Throwable t) {
                Log.e(LOG_TAG, "something went wrong, like no internet connection\n" + t.getMessage());
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Pet> pets) {
        if (mCachedPets == null) {
            mCachedPets = new LinkedHashMap<>();
        }
        mCachedPets.clear();
        for (Pet pet : pets) {
            mCachedPets.put(pet.getName(), pet);
        }
        mCacheIsDirty = false;
    }

    @Override
    public void invalidateCache() {
        mCacheIsDirty = true;
    }

    @Override
    public Pet getPet(@NonNull String petName) {
        return mCachedPets.get(petName);
    }
}
