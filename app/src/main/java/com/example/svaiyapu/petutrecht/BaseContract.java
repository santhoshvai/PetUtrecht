package com.example.svaiyapu.petutrecht;

/**
 * Created by svaiyapu on 8/25/16.
 */
public interface BaseContract {

    interface BaseView<T> {
        void setPresenter(T presenter);
    }

    interface BasePresenter {
        void start();
    }
}
