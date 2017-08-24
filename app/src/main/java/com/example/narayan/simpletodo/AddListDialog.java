package com.example.narayan.simpletodo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by narayan on 8/22/2017.
 */

public class AddListDialog extends DialogFragment {
    private DialogEditListener listener;
    private EditText etListName;
    public static AddListDialog newInstance() {
        return (new AddListDialog());
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setCancelable(false);
        View view = inflater.inflate(R.layout.add_new_list, container);
        listener=(DialogEditListener) getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.add_new_list_toolbar);
        toolbar.setTitle("Add List");
        setToolbarListener(toolbar);
        toolbar.inflateMenu(R.menu.add_list_menu);
        etListName=(EditText)view.findViewById(R.id.listText);
        etListName.setHint("Enter List Name");

        return view;
    }
    private void setToolbarListener(Toolbar toolbar)
    {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.doneListAdd: {
                        String name=etListName.getText().toString();
                        if (name.equals(""))
                        {
                            Toast.makeText(getActivity(),"List name cannot be empty",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            listener.addList(etListName.getText().toString());
                            dismiss();
                        }
                        break;
                    }
                    case R.id.addListCancel: {
                        dismiss();
                        break;
                    }
                }
                return true;
            }
        });
    }
}
