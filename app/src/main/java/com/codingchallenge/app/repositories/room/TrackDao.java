package com.codingchallenge.app.repositories.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TrackDao {

    @Query("SELECT * FROM tracks")
    LiveData<List<TrackEntity>> getAllTracks();

    @Query("SELECT * FROM tracks WHERE id=:id")
    LiveData<TrackEntity> getTrack(String id);

    @Insert
    void insert(TrackEntity track);
}
