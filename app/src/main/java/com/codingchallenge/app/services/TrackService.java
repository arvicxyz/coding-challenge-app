package com.codingchallenge.app.services;

import androidx.lifecycle.MutableLiveData;

import com.codingchallenge.app.models.TrackModel;
import com.codingchallenge.app.services.api.TrackClientService;

import java.util.List;

public class TrackService {

    private TrackClientService _clientService;

    public TrackService() {
        _clientService = new TrackClientService();
    }

    public MutableLiveData<List<TrackModel>> getTracks() {
        return _clientService.getTracks();
    }
}
