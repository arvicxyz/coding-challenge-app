package com.codingchallenge.app.models.local;

import com.codingchallenge.app.models.interfaces.IBaseLocalModel;

public class BaseLocalModel implements IBaseLocalModel {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
