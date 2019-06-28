package com.codingchallenge.app.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codingchallenge.app.R;
import com.codingchallenge.app.viewmodels.HomeFragmentViewModel;
import com.codingchallenge.app.views.MainActivity;
import com.codingchallenge.app.views.base.BaseFragment;
import com.codingchallenge.app.views.observers.HomeFragmentObserver;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class HomeFragment extends BaseFragment<HomeFragmentObserver, HomeFragmentViewModel> {

    private MainActivity _activity;
    private Unbinder _unbinder;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        _activity = ((MainActivity) getActivity());
        _unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        Timber.i("onDestroyView");
        if (_unbinder != null)
            _unbinder.unbind();
        super.onDestroyView();
    }
}
