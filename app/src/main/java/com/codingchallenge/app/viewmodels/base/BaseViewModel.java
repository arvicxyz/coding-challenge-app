package com.codingchallenge.app.viewmodels.base;

import android.app.Activity;

import androidx.lifecycle.ViewModel;

import com.codingchallenge.app.models.interfaces.IBaseViewModel;

public class BaseViewModel<T extends Activity> extends ViewModel implements IBaseViewModel {

    private Activity _activity;

    protected BaseViewModel() {
    }

    @Override
    public Activity getActivity() {
        return _activity;
    }

    @Override
    public void setActivity(Activity activity) {
        _activity = activity;
    }
}
