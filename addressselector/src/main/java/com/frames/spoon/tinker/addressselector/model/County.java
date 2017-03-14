package com.frames.spoon.tinker.addressselector.model;

import org.litepal.crud.DataSupport;

public class County extends DataSupport {
    private int id;
    private int city_id;
    private String name;

    public County() {
    }

    public County(int id, int city_id, String name) {
        this.id = id;
        this.city_id = city_id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}