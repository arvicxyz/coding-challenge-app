package com.codingchallenge.app.services.api;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.codingchallenge.app.models.SearchResultModel;
import com.codingchallenge.app.models.TrackModel;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.lang.reflect.Type;
import java.util.List;

import timber.log.Timber;

public class TrackClientService extends ApiClient {

    public TrackClientService() {
    }

    public static class GetTracks extends AsyncTask<String, Void, String> {

        private ApiClient _apiClient;
        private MutableLiveData<List<TrackModel>> _trackList;

        GetTracks(ApiClient apiClient, MutableLiveData<List<TrackModel>> trackList) {
            _apiClient = apiClient;
            _trackList = trackList;
        }

        @Override
        protected void onPreExecute() {
            _apiClient.setController("search?");
        }

        @Override
        protected String doInBackground(String... params) {

            // Fetch response base on the resource url
            String response = "";
            String resource = _apiClient.getBaseUrl() + _apiClient.getController()
                    + "term=star&amp;country=au&amp;media=movie";
            try {
                Timber.i("ApiClient Request URL: %s", resource);
                response = _apiClient.executeGet(resource);
            } catch (Exception ex) {
                Timber.e(ex.toString());
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {

            // Show the result obtained from doInBackground
            Moshi moshi = new Moshi.Builder().build();
            Type type = Types.newParameterizedType(SearchResultModel.class);
            JsonAdapter<SearchResultModel> jsonAdapter = moshi.adapter(type);
            try {
                SearchResultModel searchResult = jsonAdapter.fromJson(s);
                if (searchResult != null)
                    _trackList.setValue(searchResult.getResults());
            } catch (Exception ex) {
                Timber.e(ex.toString());
                cancel(true);
            }
        }
    }

    public LiveData<List<TrackModel>> getTracks() {
        MutableLiveData<List<TrackModel>> trackList = new MutableLiveData<>();
        try {
            new GetTracks(this, trackList).execute();
        } catch (Exception ex) {
            Timber.e(ex.toString());
        }
        return trackList;
    }
}
