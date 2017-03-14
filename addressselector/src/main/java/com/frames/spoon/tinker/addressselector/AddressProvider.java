package com.frames.spoon.tinker.addressselector;

import com.frames.spoon.tinker.addressselector.model.City;
import com.frames.spoon.tinker.addressselector.model.County;
import com.frames.spoon.tinker.addressselector.model.Province;
import com.frames.spoon.tinker.addressselector.model.Street;

import java.util.List;

public interface AddressProvider {
    void provideProvinces(AddressReceiver<Province> addressReceiver);

    void provideCitiesWith(int provinceId, AddressReceiver<City> addressReceiver);

    void provideCountiesWith(int cityId, AddressReceiver<County> addressReceiver);

    void provideStreetsWith(int countyId, AddressReceiver<Street> addressReceiver);

    interface AddressReceiver<T> {
        void send(List<T> data);
    }
}