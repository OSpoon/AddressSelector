package com.frames.spoon.tinker.addressselector.model;


import org.litepal.crud.DataSupport;

public class Street extends DataSupport {
    private int id;
    private int county_id;
    private String name;

    public Street() {
    }

    public Street(int id, int county_id, String name) {
        this.id = id;
        this.county_id = county_id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCounty_id() {
        return county_id;
    }

    public void setCounty_id(int county_id) {
        this.county_id = county_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}