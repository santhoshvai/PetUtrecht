package com.example.svaiyapu.petutrecht.Map;

import com.example.svaiyapu.petutrecht.BaseContract;
import com.example.svaiyapu.petutrecht.data.Model.Pet;

import java.util.List;

/**
 * Created by svaiyapu on 8/25/16.
 */
public interface MapContract {

    interface View extends BaseContract.BaseView<Presenter> {
        void showMapMarkers(List<Pet> pets);
    }

    interface Presenter extends BaseContract.BasePresenter {
        void loadPets();
    }
}
