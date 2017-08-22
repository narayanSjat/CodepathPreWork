package com.example.narayan.simpletodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.database.DatabaseUtilsCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import database.Item;


/**
 * Created by narayan on 8/19/2017.
 */


public class ItemDialogFragment extends DialogFragment implements DialongDataTransport {

    private Item currItem;
    private EditText etItemName;
    private TextView  etItemTime;
    private TextView  etItemDate;
    private ItemEditListener listener;
    private MODE mode;

    public ItemDialogFragment() {
        //Empty Constructor
    }



    public static enum MODE {
        ADD, EDIT
    }

    public static ItemDialogFragment newInstance(String title, Item item, MODE mode) {
        ItemDialogFragment frag = new ItemDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        frag.currItem = item;
        frag.mode=mode;
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_edit_view, container);
        setCancelable(false);
        listener = (ItemEditListener) getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.frag_toolbar);
        if (mode==MODE.EDIT) {setToolbarEdit(toolbar);}
        else {setToolbarAdd(toolbar);}
        return view;

    }

    /*
    * Sets the toolbard to hold edit menu
     */
    private void setToolbarEdit(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.edit_frag_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_menu_save: {
                        Item updatedItem=getUpdatedItem();
                        if (!currItem.equals(updatedItem)) {
                            listener.updateItem(updatedItem, currItem);
                        }
                        break;
                    }
                    case R.id.edit_menu_delete: {
                        listener.deleteItem(currItem);
                        break;
                    }
                }
                dismiss();
                return true;
            }
        });

    }

    /*
    * Sets the tool bar to hold add meanu
     */
    private void setToolbarAdd(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.add_frag_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add_menu_save) {
                    Item updatedItem=getUpdatedItem();
                    listener.addItem(updatedItem);
                }
                dismiss();
                return true;
            }
        });

    }

    /*
    * Returns an Item storing all the edits made by the user
     */
    private Item getUpdatedItem()
    {
        Item newItem=new Item();
        newItem.setName(etItemName.getText().toString());
        return newItem;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
        // Set up button and textview elements
        etItemName=(EditText ) view.findViewById(R.id.nameText);
        etItemDate=(TextView ) view.findViewById(R.id.dateText);
        etItemTime=(TextView ) view.findViewById(R.id.timeText);
        etItemName.setText(currItem.getName());
        setListeners();
    }

    private void setListeners()
    {
        final ItemDialogFragment currFrag=this;
        etItemTime.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment= new TimePickerFragment();
                fragment.setTargetFragment(currFrag,0);
                fragment.show(getFragmentManager(),"timePicker");

            }
        });
        etItemDate.setOnClickListener(new TextView.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment fragment= new DatePickerFragment();
                fragment.setTargetFragment(currFrag,1);
                fragment.show(getFragmentManager(),"datePicker");
            }
        });
    }


    @Override
    public void setTime(String time) {
        etItemTime.setText(time);
    }

    @Override
    public void setDate(String date) {
        etItemDate.setText(date);
    }

}
