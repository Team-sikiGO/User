package com.example.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private ArrayList<FoodItem> m_oData = new ArrayList<FoodItem>();

    public ListAdapter() {
    }

    @Override
    public int getCount() {
        return m_oData.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.food_item, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.textTitle);
        TextView descTextView = (TextView) convertView.findViewById(R.id.textDate);

        titleTextView.setText(m_oData.get(position).food_name);
        descTextView.setText(m_oData.get(position).price);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return m_oData.get(position);
    }

    public void addItem(String title, String desc) {
        FoodItem item = new FoodItem();

        item.setFood_name(title);
        item.setPrice(desc);

        m_oData.add(item);
    }
}
