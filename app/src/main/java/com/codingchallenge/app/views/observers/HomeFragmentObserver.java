package com.codingchallenge.app.views.observers;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import timber.log.Timber;

public class HomeFragmentObserver implements LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreateEvent() {
        Timber.i("HomeFragment ON_CREATE");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStartEvent() {
        Timber.i("HomeFragment ON_START");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResumeEvent() {
        Timber.i("HomeFragment ON_RESUME");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPauseEvent() {
        Timber.i("HomeFragment ON_PAUSE");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStopEvent() {
        Timber.i("HomeFragment ON_STOP");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroyEvent() {
        Timber.i("HomeFragment ON_DESTROY");
    }
}
