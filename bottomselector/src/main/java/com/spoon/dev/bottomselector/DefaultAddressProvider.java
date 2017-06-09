package com.spoon.dev.bottomselector;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.spoon.dev.bottomselector.model.City;
import com.spoon.dev.bottomselector.model.County;
import com.spoon.dev.bottomselector.model.Province;
import com.spoon.dev.bottomselector.model.Street;
import com.spoon.dev.bottomselector.util.DBMove;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认数据适配器
 */
public class DefaultAddressProvider implements AddressProvider {

    SQLiteDatabase database;

    public DefaultAddressProvider(Context context) {
        database = DBMove.move(context, DBMove.DBNAME);
    }

    @Override
    public void provideProvinces(final AddressReceiver<Province> addressReceiver) {
        List<Province> provinceAll = new ArrayList<>();
        Cursor provinceCursor = database.query("Province", null, null, null, null, null, null);
        if (provinceCursor.moveToFirst()) {
            do {
                Province province = new Province();
                //Cursor中取值
                province.setId(provinceCursor.getInt(provinceCursor.getColumnIndex("id")));
                province.setName(provinceCursor.getString(provinceCursor.getColumnIndex("name")));
                //添加集合
                provinceAll.add(province);
            } while (provinceCursor.moveToNext());
        }
        provinceCursor.close();
        addressReceiver.send(new ArrayList<Province>(provinceAll));
    }

    @Override
    public void provideCitiesWith(int provinceId, final AddressReceiver<City> addressReceiver) {
        List<City> cities = new ArrayList<>();
        Cursor cityCursor = database.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cityCursor.moveToFirst()) {
            do {
                City city = new City();
                //Cursor中取值
                city.setId(cityCursor.getInt(cityCursor.getColumnIndex("id")));
                city.setProvince_id(cityCursor.getInt(cityCursor.getColumnIndex("province_id")));
                city.setName(cityCursor.getString(cityCursor.getColumnIndex("name")));
                //添加集合
                cities.add(city);
            } while (cityCursor.moveToNext());
        }
        cityCursor.close();
        addressReceiver.send(new ArrayList<City>(cities));
    }

    @Override
    public void provideCountiesWith(int cityId, final AddressReceiver<County> addressReceiver) {
        List<County> counties = new ArrayList<>();
        Cursor countyCursor = database.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
        if (countyCursor.moveToFirst()) {
            do {
                County county = new County();
                //Cursor中取值
                county.setId(countyCursor.getInt(countyCursor.getColumnIndex("id")));
                county.setCity_id(countyCursor.getInt(countyCursor.getColumnIndex("city_id")));
                county.setName(countyCursor.getString(countyCursor.getColumnIndex("name")));
                //添加集合
                counties.add(county);
            } while (countyCursor.moveToNext());
        }
        countyCursor.close();
        addressReceiver.send(new ArrayList<County>(counties));
    }

    @Override
    public void provideStreetsWith(int countyId, final AddressReceiver<Street> addressReceiver) {
        List<Street> streets = new ArrayList<>();
        Cursor streetsCursor = database.query("Street", null, "county_id = ?", new String[]{String.valueOf(countyId)}, null, null, null);
        if (streetsCursor.moveToFirst()) {
            do {
                Street street = new Street();
                //Cursor中取值
                street.setId(streetsCursor.getInt(streetsCursor.getColumnIndex("id")));
                street.setCounty_id(streetsCursor.getInt(streetsCursor.getColumnIndex("county_id")));
                street.setName(streetsCursor.getString(streetsCursor.getColumnIndex("name")));
                //添加集合
                streets.add(street);
            } while (streetsCursor.moveToNext());
        }
        streetsCursor.close();
        addressReceiver.send(new ArrayList<Street>(streets));
    }
}
