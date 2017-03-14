package com.frames.spoon.tinker.addressselector;

import com.frames.spoon.tinker.addressselector.model.City;
import com.frames.spoon.tinker.addressselector.model.County;
import com.frames.spoon.tinker.addressselector.model.Province;
import com.frames.spoon.tinker.addressselector.model.Street;

public interface OnAddressSelectedListener {
    void onAddressSelected(Province province, City city, County county, Street street);
}
