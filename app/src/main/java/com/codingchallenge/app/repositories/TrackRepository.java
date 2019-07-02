package com.codingchallenge.app.repositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.codingchallenge.app.repositories.base.BaseRepository;
import com.codingchallenge.app.repositories.room.TrackDao;
import com.codingchallenge.app.repositories.room.TrackEntity;

import java.util.List;

public class TrackRepository extends BaseRepository {

    private TrackDao _trackDao;
    private LiveData<List<TrackEntity>> _trackList;

    public TrackRepository(Context context) {
        super(context);
        _trackDao = getAppDb().trackDao();
        _trackList = _trackDao.getAllTracks();
    }

    // Tracks

    public LiveData<List<TrackEntity>> getAllTracks() {
        return _trackList;
    }

    public LiveData<TrackEntity> getTrack(String trackId) {
        return _trackDao.getTrack(trackId);
    }

    public void insert(TrackEntity track) {
        new InsertAsyncTask(_trackDao).execute(track);
    }

    // Async Tasks

    private class OperationsAsyncTask extends AsyncTask<TrackEntity, Void, Void> {

        TrackDao _trackDao;

        OperationsAsyncTask(TrackDao trackDao) {
            _trackDao = trackDao;
        }

        @Override
        protected Void doInBackground(TrackEntity... trackEntities) {
            return null;
        }
    }

    private class InsertAsyncTask extends OperationsAsyncTask {

        InsertAsyncTask(TrackDao trackDao) {
            super(trackDao);
        }

        @Override
        protected Void doInBackground(TrackEntity... trackEntities) {
            _trackDao.insert(trackEntities[0]);
            return null;
        }
    }
}
