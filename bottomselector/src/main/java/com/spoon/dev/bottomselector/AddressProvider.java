package com.spoon.dev.bottomselector;

import com.spoon.dev.bottomselector.model.City;
import com.spoon.dev.bottomselector.model.County;
import com.spoon.dev.bottomselector.model.Province;
import com.spoon.dev.bottomselector.model.Street;

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