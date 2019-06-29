package com.codingchallenge.app.models;

import java.util.List;

public class SearchResultModel {

    public int resultCount;
    public List<TrackModel> results;

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public List<TrackModel> getResults() {
        return results;
    }

    public void setResults(List<TrackModel> results) {
        this.results = results;
    }
}
