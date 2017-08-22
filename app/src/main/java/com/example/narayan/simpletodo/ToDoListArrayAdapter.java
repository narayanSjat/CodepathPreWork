package com.example.narayan.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import database.Item;

/**
 * Created by narayan on 8/19/2017.
 */

public class ToDoListArrayAdapter extends ArrayAdapter <Item> {

    public ToDoListArrayAdapter(Context context, ArrayList<Item> items){
        super (context,0,items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
       Item item = getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_items, parent, false);
        }
        TextView tvItem= (TextView) convertView.findViewById(R.id.lvRowText);
        tvItem.setText(item.getName());
        return convertView;
    }
}
