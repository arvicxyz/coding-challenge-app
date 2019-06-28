package com.codingchallenge.app.models;

import com.codingchallenge.app.models.interfaces.IBaseModel;

public class BaseModel implements IBaseModel {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
