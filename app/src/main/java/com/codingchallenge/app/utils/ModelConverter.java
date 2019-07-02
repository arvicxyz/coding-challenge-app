package com.codingchallenge.app.utils;

import com.codingchallenge.app.models.TrackModel;
import com.codingchallenge.app.repositories.room.TrackEntity;

import java.util.ArrayList;
import java.util.List;

public class ModelConverter {

    public static TrackModel convert(TrackEntity entity) {
        TrackModel model = new TrackModel();
        model.setId(entity.getId());
        model.setTrackName(entity.getTrackName());
        model.setTrackPrice(entity.getTrackPrice());
        model.setTrackGenre(entity.getTrackGenre());
        model.setTrackDescription(entity.getTrackDescription());
        model.setTrackArtworkUrl(entity.getTrackArtworkUrl());
        return model;
    }

    public static TrackEntity convert(TrackModel model) {
        TrackEntity entity = new TrackEntity();
        entity.setId(model.getId());
        entity.setTrackName(model.getTrackName());
        entity.setTrackPrice(model.getTrackPrice());
        entity.setTrackGenre(model.getTrackGenre());
        entity.setTrackDescription(model.getTrackDescription());
        entity.setTrackArtworkUrl(model.getTrackArtworkUrl());
        return entity;
    }

    public static List<TrackModel> convertEntityToModel(List<TrackEntity> entities) {
        List<TrackModel> models = new ArrayList<>();
        for (TrackEntity entity : entities) {
            models.add(convert(entity));
        }
        return models;
    }

    public static List<TrackEntity> convertModelToEntity(List<TrackModel> models) {
        List<TrackEntity> entities = new ArrayList<>();
        for (TrackModel model : models) {
            entities.add(convert(model));
        }
        return entities;
    }
}
