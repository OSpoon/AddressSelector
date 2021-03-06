package com.spoon.dev.bottomselector;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.spoon.dev.bottomselector.adapter.AddressAdapter;
import com.spoon.dev.bottomselector.model.City;
import com.spoon.dev.bottomselector.model.County;
import com.spoon.dev.bottomselector.model.Province;
import com.spoon.dev.bottomselector.model.Street;
import com.spoon.dev.bottomselector.util.Lists;

import java.util.List;

public class AddressSelector implements AdapterView.OnItemClickListener {
    //省市县镇选择标记
    private static final int INDEX_TAB_PROVINCE = 0;
    private static final int INDEX_TAB_CITY = 1;
    private static final int INDEX_TAB_COUNTY = 2;
    private static final int INDEX_TAB_STREET = 3;

    //无效标记
    public static final int INDEX_INVALID = -1;

    private static final int WHAT_PROVINCES_PROVIDED = 0;
    private static final int WHAT_CITIES_PROVIDED = 1;
    private static final int WHAT_COUNTIES_PROVIDED = 2;
    private static final int WHAT_STREETS_PROVIDED = 3;

    @SuppressWarnings("unchecked")
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_PROVINCES_PROVIDED:
                    provinces = (List<Province>) msg.obj;
                    provinceAdapter.setData(provinces);
                    listView.setAdapter(provinceAdapter);
                    break;

                case WHAT_CITIES_PROVIDED:
                    cities = (List<City>) msg.obj;
                    cityAdapter.setData(cities);
                    if (Lists.notEmpty(cities)) {
                        // 以次级内容更新列表
                        listView.setAdapter(cityAdapter);
                        // 更新索引为次级
                        tabIndex = INDEX_TAB_CITY;
                    } else {
                        // 次级无内容，回调
                        callbackInternal();
                    }
                    break;

                case WHAT_COUNTIES_PROVIDED:
                    counties = (List<County>) msg.obj;
                    countyAdapter.setData(counties);
                    if (Lists.notEmpty(counties)) {
                        listView.setAdapter(countyAdapter);
                        tabIndex = INDEX_TAB_COUNTY;
                    } else {
                        callbackInternal();
                    }
                    break;

                case WHAT_STREETS_PROVIDED:
                    streets = (List<Street>) msg.obj;
                    streetAdapter.setData(streets);
                    if (Lists.notEmpty(streets)) {
                        listView.setAdapter(streetAdapter);
                        tabIndex = INDEX_TAB_STREET;
                    } else {
                        callbackInternal();
                    }
                    break;
            }

            updateTabsVisibility();
            updateProgressVisibility();
            updateIndicator();

            return true;
        }
    });

    private static AddressProvider DEFAULT_ADDRESS_PROVIDER;

    private final Context context;
    private OnAddressSelectedListener listener;
    private AddressProvider addressProvider;

    private BottomSelector selector;

    private View view;

    private View indicator;

    private ImageView ivClose;

    private TextView textViewProvince;
    private TextView textViewCity;
    private TextView textViewCounty;
    private TextView textViewStreet;

    private ProgressBar progressBar;

    private ListView listView;
    private AddressAdapter<Province> provinceAdapter;
    private AddressAdapter<City> cityAdapter;
    private AddressAdapter<County> countyAdapter;
    private AddressAdapter<Street> streetAdapter;

    private List<Province> provinces;
    private List<City> cities;
    private List<County> counties;
    private List<Street> streets;

    private int provinceIndex = INDEX_INVALID;
    private int cityIndex = INDEX_INVALID;
    private int countyIndex = INDEX_INVALID;
    private int streetIndex = INDEX_INVALID;

    private int tabIndex = INDEX_TAB_PROVINCE;

    public AddressSelector(Context context) {
        this.context = context;

        DEFAULT_ADDRESS_PROVIDER = new DefaultAddressProvider(context);
        addressProvider = DEFAULT_ADDRESS_PROVIDER;

        initViews();
        initAdapters();
        retrieveWith(0, INDEX_TAB_PROVINCE);
    }

    private void initAdapters() {

        provinceAdapter = new AddressAdapter();
        cityAdapter = new AddressAdapter();
        countyAdapter = new AddressAdapter();
        streetAdapter = new AddressAdapter();
    }

    private void initViews() {
        view = LayoutInflater.from(context).inflate(R.layout.address_selector, null);

        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        this.listView = (ListView) view.findViewById(R.id.listView);
        this.indicator = view.findViewById(R.id.indicator);

        this.ivClose = (ImageView) view.findViewById(R.id.iv_close);
        this.ivClose.setOnClickListener(new OnCloseDialogListener());

        this.textViewProvince = (TextView) view.findViewById(R.id.textViewProvince);
        this.textViewCity = (TextView) view.findViewById(R.id.textViewCity);
        this.textViewCounty = (TextView) view.findViewById(R.id.textViewCounty);
        this.textViewStreet = (TextView) view.findViewById(R.id.textViewStreet);

        this.textViewProvince.setOnClickListener(new OnTabClickListener(INDEX_TAB_PROVINCE));
        this.textViewCity.setOnClickListener(new OnTabClickListener(INDEX_TAB_CITY));
        this.textViewCounty.setOnClickListener(new OnTabClickListener(INDEX_TAB_COUNTY));
        this.textViewStreet.setOnClickListener(new OnTabClickListener(INDEX_TAB_STREET));

        this.listView.setOnItemClickListener(this);

        updateIndicator();
    }

    public View getView() {
        return view;
    }

    private void updateIndicator() {
        view.post(new Runnable() {
            @Override
            public void run() {
                switch (tabIndex) {
                    case INDEX_TAB_PROVINCE:
                        buildIndicatorAnimatorTowards(textViewProvince).start();
                        break;
                    case INDEX_TAB_CITY:
                        buildIndicatorAnimatorTowards(textViewCity).start();
                        break;
                    case INDEX_TAB_COUNTY:
                        buildIndicatorAnimatorTowards(textViewCounty).start();
                        break;
                    case INDEX_TAB_STREET:
                        buildIndicatorAnimatorTowards(textViewStreet).start();
                        break;
                }
            }
        });
    }

    private AnimatorSet buildIndicatorAnimatorTowards(TextView tab) {
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(indicator, "X", indicator.getX(), tab.getX());

        final ViewGroup.LayoutParams params = indicator.getLayoutParams();
        ValueAnimator widthAnimator = ValueAnimator.ofInt(params.width, tab.getMeasuredWidth());
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.width = (int) animation.getAnimatedValue();
                indicator.setLayoutParams(params);
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.playTogether(xAnimator, widthAnimator);

        return set;
    }

    public void setDialog(BottomSelector dialog) {
        this.selector = dialog;
    }

    private class OnCloseDialogListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (selector != null)
                selector.dismiss();
        }
    }

    private class OnTabClickListener implements View.OnClickListener {
        private int type;

        public OnTabClickListener(int type) {
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            tabIndex = type;
            if (type == INDEX_TAB_PROVINCE) {
                listView.setAdapter(provinceAdapter);
                if (provinceIndex != INDEX_INVALID) {
                    listView.setSelection(provinceIndex);
                    provinceAdapter.upIndex(provinceIndex);
                    cityAdapter.upIndex(0);
                }
            } else if (type == INDEX_TAB_CITY) {
                listView.setAdapter(cityAdapter);
                if (cityIndex != INDEX_INVALID) {
                    listView.setSelection(cityIndex);
                    cityAdapter.upIndex(cityIndex);
                    countyAdapter.upIndex(0);
                }
            } else if (type == INDEX_TAB_COUNTY) {
                listView.setAdapter(countyAdapter);
                if (countyIndex != INDEX_INVALID) {
                    listView.setSelection(countyIndex);
                    countyAdapter.upIndex(countyIndex);
                    streetAdapter.upIndex(0);
                }
            } else if (type == INDEX_TAB_STREET) {
                listView.setAdapter(streetAdapter);
                if (streetIndex != INDEX_INVALID) {
                    listView.setSelection(streetIndex);
                    streetAdapter.upIndex(streetIndex);
                }
            }
            updateTabsVisibility();
            updateIndicator();
        }
    }

    private void updateTabsVisibility() {
        textViewProvince.setVisibility(Lists.notEmpty(provinces) ? View.VISIBLE : View.GONE);
        textViewCity.setVisibility(Lists.notEmpty(cities) ? View.VISIBLE : View.GONE);
        textViewCounty.setVisibility(Lists.notEmpty(counties) ? View.VISIBLE : View.GONE);
        textViewStreet.setVisibility(Lists.notEmpty(streets) ? View.VISIBLE : View.GONE);

        textViewProvince.setEnabled(tabIndex != INDEX_TAB_PROVINCE);
        textViewCity.setEnabled(tabIndex != INDEX_TAB_CITY);
        textViewCounty.setEnabled(tabIndex != INDEX_TAB_COUNTY);
        textViewStreet.setEnabled(tabIndex != INDEX_TAB_STREET);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (tabIndex) {
            case INDEX_TAB_PROVINCE:
                Province province = provinceAdapter.getItem(position);

                // 更新当前级别及子级标签文本
                textViewProvince.setText(province.getName());
                textViewCity.setText("请选择");
                textViewCounty.setText("请选择");
                textViewStreet.setText("请选择");

                // 清空子级数据
                cities = null;
                counties = null;
                streets = null;
                cityAdapter.notifyDataSetChanged();
                countyAdapter.notifyDataSetChanged();
                streetAdapter.notifyDataSetChanged();

                // 更新已选中项
                this.provinceIndex = position;
                this.cityIndex = INDEX_INVALID;
                this.countyIndex = INDEX_INVALID;
                this.streetIndex = INDEX_INVALID;

                // 更新选中效果
                provinceAdapter.notifyDataSetChanged();
                retrieveWith(province.getId(), INDEX_TAB_CITY);
                break;

            case INDEX_TAB_CITY:
                City city = cityAdapter.getItem(position);

                textViewCity.setText(city.getName());
                textViewCounty.setText("请选择");
                textViewStreet.setText("请选择");

                counties = null;
                streets = null;
                countyAdapter.notifyDataSetChanged();
                streetAdapter.notifyDataSetChanged();

                this.cityIndex = position;
                this.countyIndex = INDEX_INVALID;
                this.streetIndex = INDEX_INVALID;

                cityAdapter.notifyDataSetChanged();

                retrieveWith(city.getId(), INDEX_TAB_COUNTY);
                break;

            case INDEX_TAB_COUNTY:
                County county = countyAdapter.getItem(position);

                textViewCounty.setText(county.getName());
                textViewStreet.setText("请选择");

                streets = null;
                streetAdapter.notifyDataSetChanged();

                this.countyIndex = position;
                this.streetIndex = INDEX_INVALID;

                countyAdapter.notifyDataSetChanged();

                retrieveWith(county.getId(), INDEX_TAB_STREET);
                break;

            case INDEX_TAB_STREET:
                Street street = streetAdapter.getItem(position);

                textViewStreet.setText(street.getName());

                this.streetIndex = position;

                streetAdapter.notifyDataSetChanged();

                callbackInternal();

                break;
        }

        updateTabsVisibility();
        updateIndicator();
    }

    private void callbackInternal() {
        if (listener != null) {
            Province province = provinces == null || provinceIndex == INDEX_INVALID ? null : provinces.get(provinceIndex);
            City city = cities == null || cityIndex == INDEX_INVALID ? null : cities.get(cityIndex);
            County county = counties == null || countyIndex == INDEX_INVALID ? null : counties.get(countyIndex);
            Street street = streets == null || streetIndex == INDEX_INVALID ? null : streets.get(streetIndex);

            listener.onAddressSelected(province, city, county, street);
            selector.dismiss();
        }
    }

    private void updateProgressVisibility() {
        ListAdapter adapter = listView.getAdapter();
        int itemCount = adapter.getCount();
        progressBar.setVisibility(itemCount > 0 ? View.GONE : View.VISIBLE);
    }

    private void retrieveWith(int id, int type) {
        progressBar.setVisibility(View.VISIBLE);
        if (id == 0 && type == INDEX_TAB_PROVINCE) {
            addressProvider.provideProvinces(new AddressProvider.AddressReceiver<Province>() {
                @Override
                public void send(List<Province> data) {
                    handler.sendMessage(Message.obtain(handler, WHAT_PROVINCES_PROVIDED, data));
                }
            });
        } else if (id != 0 && type == INDEX_TAB_CITY) {
            addressProvider.provideCitiesWith(id, new AddressProvider.AddressReceiver<City>() {
                @Override
                public void send(List<City> data) {
                    handler.sendMessage(Message.obtain(handler, WHAT_CITIES_PROVIDED, data));
                }
            });
        } else if (id != 0 && type == INDEX_TAB_COUNTY) {
            addressProvider.provideCountiesWith(id, new AddressProvider.AddressReceiver<County>() {
                @Override
                public void send(List<County> data) {
                    handler.sendMessage(Message.obtain(handler, WHAT_COUNTIES_PROVIDED, data));
                }
            });
        } else if (id != 0 && type == INDEX_TAB_STREET) {
            addressProvider.provideStreetsWith(id, new AddressProvider.AddressReceiver<Street>() {
                @Override
                public void send(List<Street> data) {
                    handler.sendMessage(Message.obtain(handler, WHAT_STREETS_PROVIDED, data));
                }
            });
        }
    }

    public OnAddressSelectedListener getOnAddressSelectedListener() {
        return listener;
    }

    public void setOnAddressSelectedListener(OnAddressSelectedListener listener) {
        this.listener = listener;
    }

    public void setAddressProvider(AddressProvider addressProvider) {
        this.addressProvider = addressProvider;
        if (addressProvider == null) {
            this.addressProvider = DEFAULT_ADDRESS_PROVIDER;
        }
        retrieveWith(0, INDEX_TAB_PROVINCE);
    }

}
