package com.example.narayan.simpletodo;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.text.format.DateFormat;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by narayan on 8/21/2017.
 */

public class DatePickerFragment  extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    private DialongDataTransport transport;
    // Implmentation inspired from android documentation https://developer.android.com/guide/topics/ui/controls/pickers.html
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        transport=(DialongDataTransport)getTargetFragment();

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), R.style.Picker, this, year, month, day); //PASS CUSTOM THEME
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,monthOfYear,dayOfMonth);
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        transport.setDate(dateFormat.format(calendar.getTime()));

    }
}
