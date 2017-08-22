package com.example.narayan.simpletodo;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.database.DatabaseUtilsCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private RadioGroup priorityRadioGroup;
    private TextView etItemMemo;
    private CheckBox isCompleteCheck;
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
        if (mode==MODE.EDIT)
        {
            toolbar.setTitle("EDIT");
            setToolbarEdit(toolbar);
        }
        else
        {
            toolbar.setTitle("ADD");
            setToolbarAdd(toolbar);
        }
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
                        if (updatedItem.getName().equals("")){
                            displayMessage("Name Cannot Be empty");
                            return false;
                        }
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
    * Display Passed method to user
     */
    void displayMessage(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
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
                    if (updatedItem.getName().equals("")){
                        displayMessage("Name Cannot Be empty");
                        return false;
                    }
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
        newItem.setComplete(isCompleteCheck.isChecked());
        newItem.setMemo(etItemMemo.getText().toString());
        newItem.setDate(etItemDate.getText().toString());
        newItem.setTime(etItemTime.getText().toString());
        switch (priorityRadioGroup.getCheckedRadioButtonId()){
            case R.id.radioLow:{
                newItem.setPriority(0);
                break;
            }
            case R.id.radioMedium: {
                newItem.setPriority(1);
                break;
            }
            default:{
                newItem.setPriority(2);
            }
        }
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
        priorityRadioGroup= (RadioGroup) view.findViewById(R.id.radioPriority);
        etItemMemo=(TextView)view.findViewById(R.id.memoText);
        isCompleteCheck=(CheckBox)view.findViewById(R.id.cbComplete);

        // Set up the screen and the listeners
        if (mode==MODE.EDIT)
            prepFragScreenEdit();
        else
            prepFragScreenAdd();
        setListeners();
    }

    /*
    * Get current Time
     */
     private String getCurrentTime (Calendar c)
     {
         String time;
         int hour =c.get(Calendar.HOUR_OF_DAY);
         int min = c.get(Calendar.MINUTE);
         String format;
         if (hour==0)
         {
             format="AM";
             hour=12;
         }
         else if(hour==12){
             format="PM";
         }
         else if (hour>12){
             format="PM";
             hour-=12;
         }
         else
             format="AM";

         return hour+":"+min+ " "+format;
     }
     /*
     * Get current Date
      */
     private String getCurrentDate (Calendar c)
     {
         Calendar calendar = Calendar.getInstance();
         SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
         return dateFormat.format(calendar.getTime());
     }

    /*
     Sets up the Dialog Screen for adding a new Item
     */
    void prepFragScreenAdd()
    {
        etItemName.setHint("Enter Item Name");
        etItemMemo.setHint("Enter Your Notes");
        final Calendar c= Calendar.getInstance();
        etItemDate.setText(getCurrentDate(c));
        etItemTime.setText(getCurrentTime(c));
        isCompleteCheck.setChecked(false);
    }

    /*
    * Sers up the Dialog Screen for Editing an Item
     */
    void prepFragScreenEdit()
    {
        etItemName.setText(currItem.getName());
        etItemMemo.setText(currItem.getMemo());
        etItemDate.setText(currItem.getDate());
        etItemTime.setText(currItem.getTime());
        isCompleteCheck.setChecked(currItem.getComplete());
        switch (currItem.getPriority()){
            case 0: {
                RadioButton rButton = (RadioButton) priorityRadioGroup.findViewById(R.id.radioLow);
                rButton.setChecked(true);
                break;

            }
            case 1: {
                RadioButton rButton = (RadioButton) priorityRadioGroup.findViewById(R.id.radioMedium);
                rButton.setChecked(true);
                break;
            }
            default:{
                RadioButton rButton = (RadioButton) priorityRadioGroup.findViewById(R.id.radioHigh);
                rButton.setChecked(true);
                break;
            }
        }
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
        /*priorityRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.radioLow:
                        newItem.setPriority(0);
                    case R.id.radioMedium :
                        newItem.setPriority(1);
                        break;
                    default:
                        currItem.setPriority(2);
                }
            }
        });
        isCompleteCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    currItem.setComplete(true);
                else
                    currItem.setComplete(false);
            }
        });*/
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
