package com.codingchallenge.app.viewmodels.base;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import com.codingchallenge.app.models.interfaces.IBaseViewModel;

public class BaseViewModel<T extends AppCompatActivity> extends ViewModel implements IBaseViewModel {

    private AppCompatActivity _activity;

    public AppCompatActivity getActivity() {
        return _activity;
    }

    public void setActivity(AppCompatActivity activity) {
        _activity = activity;
    }

    protected BaseViewModel() {
    }
}
