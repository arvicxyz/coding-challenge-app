package com.codingchallenge.app.repositories.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = TrackEntity.class, version = 1, exportSchema = false)
public abstract class ApplicationDatabase extends RoomDatabase {

    public abstract TrackDao trackDao();

    private static volatile ApplicationDatabase instance;

    public static ApplicationDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (ApplicationDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ApplicationDatabase.class,
                            "notflix_database")
                            .build();
                }
            }
        }
        return instance;
    }
}
