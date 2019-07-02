package com.codingchallenge.app.repositories.base;

import android.content.Context;

import com.codingchallenge.app.repositories.room.ApplicationDatabase;

public class BaseRepository {

    private ApplicationDatabase appDb;

    protected ApplicationDatabase getAppDb() {
        return appDb;
    }

    public BaseRepository(Context context) {
        appDb = ApplicationDatabase.getDatabase(context);
    }
}
