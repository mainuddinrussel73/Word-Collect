package com.example.mainuddin.myapplication;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class MyListAdapter extends BaseAdapter {

    private final Activity context;
    List<word> contactList;
    // private final Integer[] imgid;

    public MyListAdapter(Activity context) {

        this.context=context;
        this.contactList = new ArrayList<word>();
        this.contactList.addAll(MainActivity.contactList);


    }

    @Override
    public int getCount() {
        return MainActivity.contactList.size();
    }

    @Override
    public word getItem(int i) {
        return MainActivity.contactList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View view, ViewGroup parent) {


        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView number = (TextView) rowView.findViewById(R.id.num);

        //System.out.println("klkl"+MainActivity.contactList.size());
        titleText.setText(MainActivity.contactList.get(position).WORD);
        number.setText(String.valueOf(position));


        return rowView;

    };


    public void filter(String charText) {
        charText = charText.toLowerCase();
        MainActivity.contactList.clear();
        System.out.println(this.contactList.size());
        if (charText.length() == 0) {
            MainActivity.contactList.addAll(this.contactList);
        } else {
            for (word wp : this.contactList) {
                if (wp.WORD.toLowerCase().contains(charText)) {
                    MainActivity.contactList.add(wp);
                }
            }
        }
        System.out.println(MainActivity.contactList.size());
        notifyDataSetChanged();
    }
}