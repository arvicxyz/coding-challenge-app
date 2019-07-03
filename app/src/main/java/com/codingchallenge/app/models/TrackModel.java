package com.codingchallenge.app.models;

import com.squareup.moshi.Json;

public class TrackModel extends BaseModel {

    private int trackId;
    private String trackName;
    private float trackPrice;
    @Json(name = "primaryGenreName")
    private String trackGenre;
    @Json(name = "longDescription")
    private String trackDescription;
    @Json(name = "artworkUrl100")
    private String trackArtworkUrl;

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
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

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(this.getClass()))
            return ((TrackModel) obj).getTrackId() == this.trackId;
        return false;
    }
}
