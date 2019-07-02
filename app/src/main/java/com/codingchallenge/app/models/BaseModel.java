package com.codingchallenge.app.models;

import com.codingchallenge.app.models.interfaces.IBaseModel;

public class BaseModel implements IBaseModel {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
