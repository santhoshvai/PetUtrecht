package com.example.svaiyapu.petutrecht.Detail;

import android.support.annotation.NonNull;

import com.example.svaiyapu.petutrecht.Util.PetUtil;
import com.example.svaiyapu.petutrecht.data.Model.Pet;
import com.example.svaiyapu.petutrecht.data.PetDataSource;
import com.example.svaiyapu.petutrecht.data.Remote.PetRemoteDataSource;

import java.util.List;

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
    public void loadPet(final String petName, final String petType) {
        final PetRemoteDataSource petRemoteDataSource = PetRemoteDataSource.getInstance();
        final Pet pet = petRemoteDataSource.getPet(petName);

        petRemoteDataSource.getPets(new PetDataSource.LoadPetsCallback() {
            @Override
            public void onPetsLoaded(List<Pet> pets) {
                List<Pet> neededPetList = getNeededList(pets, petType);
                mDetailView.showDetail(pet, neededPetList);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });


    }

    private List<Pet> getNeededList(final List<Pet> pets, final String petType) {
        if(PetUtil.isDog(petType)) {
            return PetUtil.filterDogs(pets);
        }
        else if(PetUtil.isCat(petType)) {
            return PetUtil.filterCats(pets);
        }
        else  {
            return pets;
        }
    }

    @Override
    public void start() {

    }
}
