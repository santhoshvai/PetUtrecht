package com.example.svaiyapu.petutrecht.Grid;

import android.content.Intent;

import com.example.svaiyapu.petutrecht.BaseContract;
import com.example.svaiyapu.petutrecht.data.Model.Pet;

import java.util.List;

/**
 * Created by svaiyapu on 8/26/16.
 */
public interface GridContract {

    interface View extends BaseContract.BaseView<Presenter> {
        void showGrid(List<Pet> pets);
        void showProgressbar();
        void hideProgressbar();
        android.view.View activityReenter(Intent data);
    }

    interface Presenter extends BaseContract.BasePresenter {
        void loadDogs();
        void loadCats();
        void forceUpdate();
    }
}