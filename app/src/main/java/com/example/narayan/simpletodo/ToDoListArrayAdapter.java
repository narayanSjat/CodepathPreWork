package com.example.narayan.simpletodo;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
        ImageView imgView= (ImageView)convertView.findViewById(R.id.itemStatus);
        int priority=item.getPriority();
        if (item.getComplete()){priority=-1;}
        imgView.setImageResource(getResStatusImage(priority));
        tvItem.setText(item.getName());
        return convertView;
    }

    @DrawableRes
    int getResStatusImage(int priority)
    {
        switch (priority){
            case 0:
                return R.drawable.icons_low_priority;
            case 1:
                return R.drawable.icon_med_priority;
            case 2:
                return R.drawable.icon_high_priority;
            default:
                return R.drawable.icons_done;
        }
    }

}
