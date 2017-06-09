package com.spoon.dev.bottomselector.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spoon.dev.bottomselector.AddressSelector;
import com.spoon.dev.bottomselector.R;
import com.spoon.dev.bottomselector.model.AddressBase;
import com.spoon.dev.bottomselector.model.City;

import java.util.List;

/**
 * Created by zhanxiaolin-n22 on 2017/6/9.
 */

public class AddressAdapter<T extends AddressBase> extends BaseAdapter {

    public List<T> list;
    public int index;

    public void setData(List<T> list) {
        this.list = list;
    }

    public void upIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);

            holder = new Holder();
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        T item = getItem(position);
        holder.textView.setText(item.getName());
        boolean checked = index != AddressSelector.INDEX_INVALID && list.get(index).getId() == item.getId();
        holder.textView.setEnabled(!checked);
        holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);

        return convertView;
    }

    class Holder {
        TextView textView;
        ImageView imageViewCheckMark;
    }
}
