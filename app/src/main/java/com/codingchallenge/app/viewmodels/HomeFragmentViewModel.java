package com.codingchallenge.app.viewmodels;

import com.codingchallenge.app.viewmodels.base.BaseViewModel;

import timber.log.Timber;

public class HomeFragmentViewModel extends BaseViewModel {

    public HomeFragmentViewModel() {
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Timber.i("onCleared");
    }
}
