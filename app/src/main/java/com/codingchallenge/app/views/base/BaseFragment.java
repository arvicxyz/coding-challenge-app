package com.codingchallenge.app.views.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProviders;

import com.codingchallenge.app.viewmodels.base.BaseViewModel;

import java.lang.reflect.ParameterizedType;

import timber.log.Timber;

public class BaseFragment<T1 extends LifecycleObserver, T2 extends BaseViewModel> extends Fragment {

    private LifecycleObserver _observer;
    private BaseViewModel _viewModel;

    public BaseFragment() {
        _observer = createObserver();
        _viewModel = createViewModel();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLifecycle().addObserver(_observer);
        _viewModel = ViewModelProviders.of(this).get(_viewModel.getClass());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _viewModel.setActivity((AppCompatActivity) getActivity());
    }

    public T1 getObserver() {
        Class<T1> lifeCycleObserver = getObserverClass();
        if (lifeCycleObserver != null) {
            return lifeCycleObserver.cast(_observer);
        } else {
            Timber.e("Error on casting Observer");
            throw new NullPointerException();
        }
    }

    public T2 getViewModel() {
        Class<T2> baseViewModel = getViewModelClass();
        if (baseViewModel != null) {
            return baseViewModel.cast(_viewModel);
        } else {
            Timber.e("Error on casting ViewModel");
            throw new NullPointerException();
        }
    }

    private T1 createObserver() {
        try {
            if (getObserverClass() != null)
                return getObserverClass().newInstance();
        } catch (IllegalAccessException e) {
            Timber.e("IllegalAccessException %s", e.toString());
        } catch (java.lang.InstantiationException e) {
            Timber.e("InstantiationException %s", e.toString());
        } catch (NullPointerException e) {
            Timber.e("NullPointerException %s", e.toString());
        }
        return null;
    }

    private T2 createViewModel() {
        try {
            if (getViewModelClass() != null)
                return getViewModelClass().newInstance();
        } catch (IllegalAccessException e) {
            Timber.e("IllegalAccessException %s", e.toString());
        } catch (java.lang.InstantiationException e) {
            Timber.e("InstantiationException %s", e.toString());
        } catch (NullPointerException e) {
            Timber.e("NullPointerException %s", e.toString());
        }
        return null;
    }

    private Class<T1> getObserverClass() {
        try {
            return (Class<T1>) getSuperClass().getActualTypeArguments()[0];
        } catch (ClassCastException e) {
            Timber.e("ClassCastException %s", e.toString());
        }
        return null;
    }

    private Class<T2> getViewModelClass() {
        try {
            return (Class<T2>) getSuperClass().getActualTypeArguments()[1];
        } catch (ClassCastException e) {
            Timber.e("ClassCastException %s", e.toString());
        }
        return null;
    }

    private ParameterizedType getSuperClass() {
        return (ParameterizedType) getClass().getGenericSuperclass();
    }
}
