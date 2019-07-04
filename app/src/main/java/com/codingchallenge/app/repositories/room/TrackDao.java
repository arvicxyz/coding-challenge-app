package com.codingchallenge.app.repositories.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TrackDao {

    @Query("SELECT * FROM tracks WHERE id=:id")
    LiveData<TrackEntity> getTrack(int id);

    @Query("SELECT * FROM tracks")
    LiveData<List<TrackEntity>> getAllTracks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TrackEntity track);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TrackEntity> tracks);

    @Query("DELETE FROM tracks")
    void deleteAll();
}
