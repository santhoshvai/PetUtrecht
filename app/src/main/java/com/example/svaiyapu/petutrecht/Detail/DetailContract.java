package com.example.svaiyapu.petutrecht.Detail;

import com.example.svaiyapu.petutrecht.BaseContract;
import com.example.svaiyapu.petutrecht.data.Model.Pet;

import java.util.List;

/**
 * Created by svaiyapu on 8/29/16.
 */
public class DetailContract {
    interface View extends BaseContract.BaseView<Presenter> {
        void showDetail(Pet pet, List<Pet> pets);
    }

    interface Presenter extends BaseContract.BasePresenter {
        void loadPet(String petName, String petType);
    }
}
