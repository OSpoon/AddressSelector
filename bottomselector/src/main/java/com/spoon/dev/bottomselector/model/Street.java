package com.spoon.dev.bottomselector.model;

public class Street extends AddressBase {
    private int county_id;

    public Street() {
    }

    public int getCounty_id() {
        return county_id;
    }

    public void setCounty_id(int county_id) {
        this.county_id = county_id;
    }

}