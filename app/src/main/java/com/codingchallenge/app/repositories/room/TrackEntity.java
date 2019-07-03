package com.codingchallenge.app.repositories.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tracks")
public class TrackEntity {

    @PrimaryKey
    @NonNull
    private int id;
    private String trackName;
    private float trackPrice;
    private String trackGenre;
    private String trackDescription;
    private String trackArtworkUrl;

    public TrackEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public float getTrackPrice() {
        return trackPrice;
    }

    public void setTrackPrice(float trackPrice) {
        this.trackPrice = trackPrice;
    }

    public String getTrackGenre() {
        return trackGenre;
    }

    public void setTrackGenre(String trackGenre) {
        this.trackGenre = trackGenre;
    }

    public String getTrackDescription() {
        return trackDescription;
    }

    public void setTrackDescription(String trackDescription) {
        this.trackDescription = trackDescription;
    }

    public String getTrackArtworkUrl() {
        return trackArtworkUrl;
    }

    public void setTrackArtworkUrl(String trackArtworkUrl) {
        this.trackArtworkUrl = trackArtworkUrl;
    }
}
