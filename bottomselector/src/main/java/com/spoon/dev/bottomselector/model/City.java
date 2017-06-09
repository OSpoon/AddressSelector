package com.spoon.dev.bottomselector.model;

public class City extends AddressBase{
    private int province_id;

    public City() {
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }
}