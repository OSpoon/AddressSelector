package com.spoon.dev.bottomselector;

import com.spoon.dev.bottomselector.model.City;
import com.spoon.dev.bottomselector.model.County;
import com.spoon.dev.bottomselector.model.Province;
import com.spoon.dev.bottomselector.model.Street;

public interface OnAddressSelectedListener {
    void onAddressSelected(Province province, City city, County county, Street street);
}
