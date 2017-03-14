package com.frames.spoon.tinker.addressselector;

import android.content.Context;

import com.frames.spoon.tinker.addressselector.model.City;
import com.frames.spoon.tinker.addressselector.model.County;
import com.frames.spoon.tinker.addressselector.model.Province;
import com.frames.spoon.tinker.addressselector.model.Street;
import com.frames.spoon.tinker.addressselector.util.DBUtil;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认数据适配器
 */
public class DefaultAddressProvider implements AddressProvider {
    public DefaultAddressProvider(Context context) {
        DBUtil.initDB(context);
        LitePal.initialize(context);
    }

    @Override
    public void provideProvinces(final AddressReceiver<Province> addressReceiver) {
        List<Province> provinceAll = DataSupport.findAll(Province.class);
        addressReceiver.send(new ArrayList<Province>(provinceAll));
    }

    @Override
    public void provideCitiesWith(int provinceId, final AddressReceiver<City> addressReceiver) {
        List<City> cities = DataSupport.where("province_id = ?", String.valueOf(provinceId)).find(City.class);
        addressReceiver.send(new ArrayList<City>(cities));
    }

    @Override
    public void provideCountiesWith(int cityId, final AddressReceiver<County> addressReceiver) {
        List<County> counties = DataSupport.where("city_id = ?", String.valueOf(cityId)).find(County.class);
        addressReceiver.send(new ArrayList<County>(counties));
    }

    @Override
    public void provideStreetsWith(int countyId, final AddressReceiver<Street> addressReceiver) {
        List<Street> streets = DataSupport.where("county_id = ?", String.valueOf(countyId)).find(Street.class);
        addressReceiver.send(new ArrayList<Street>(streets));
    }
}
