package com.example.narayan.simpletodo;

import android.app.Dialog;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.util.TimeUtils;
import android.widget.CalendarView;
import android.widget.TimePicker;

import android.text.format.DateFormat;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by narayan on 8/20/2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    private DialongDataTransport transport;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c= Calendar.getInstance();
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int min=c.get(Calendar.MINUTE);
        transport=(DialongDataTransport)getTargetFragment();

        // Crete new instance of time picker and return
        return new TimePickerDialog(getActivity(),R.style.Picker,this,hour,min, DateFormat.is24HourFormat(getActivity()));

    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        int hour;
        int min;
        String format;
        if (Build.VERSION.SDK_INT >= 23 )
        {
            hour=view.getHour();
            min=view.getMinute();
        }

        else
        {
            hour=view.getCurrentHour();
            min=view.getCurrentMinute();

        }

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

        transport.setTime(hour+":"+min+" "+format);
    }
}
