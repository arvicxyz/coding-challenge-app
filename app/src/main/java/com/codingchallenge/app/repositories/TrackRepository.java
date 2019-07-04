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

    public LiveData<TrackEntity> getTrack(int trackId) {
        return _trackDao.getTrack(trackId);
    }

    public LiveData<List<TrackEntity>> getAllTracks() {
        return _trackList;
    }

    public void insert(TrackEntity track) {
        new InsertAsyncTask(_trackDao).execute(track);
    }

    public void insertAll(List<TrackEntity> tracks) {
        new InsertAllAsyncTask(_trackDao).execute(tracks);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(_trackDao).execute();
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

    private class InsertAllAsyncTask extends AsyncTask<List<TrackEntity>, Void, Void> {

        TrackDao _trackDao;

        InsertAllAsyncTask(TrackDao trackDao) {
            _trackDao = trackDao;
        }

        @Override
        protected Void doInBackground(List<TrackEntity>... trackEntities) {
            _trackDao.insertAll(trackEntities[0]);
            return null;
        }
    }

    private class DeleteAllAsyncTask extends OperationsAsyncTask {

        DeleteAllAsyncTask(TrackDao trackDao) {
            super(trackDao);
        }

        @Override
        protected Void doInBackground(TrackEntity... trackEntities) {
            _trackDao.deleteAll();
            return null;
        }
    }
}
