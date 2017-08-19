package com.example.narayan.simpletodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final String dataFileName="toDo_ItemsData.txt";
    ArrayList<String> toDoList;
    ArrayAdapter<String> toDoAdapter;
    ListView lvItems;
    EditText etEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateItems();
        lvItems=(ListView)findViewById(R.id.lvItems);
        etEditText=(EditText)findViewById(R.id.etEditText);
        lvItems.setAdapter(toDoAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                toDoList.remove(position);
                toDoAdapter.notifyDataSetChanged();
                write_items();
                return true;
            }
        });
    }

    /*
     * Reads the List of previously added items from the file and adds them to toDoList
     */
    private void read_items()
    {
        File filesDir= getFilesDir();
        File dataFile= new File(filesDir,dataFileName);
        try {
            toDoList=new ArrayList<String>(FileUtils.readLines(dataFile));
        } catch (IOException e) {
            toDoList=new ArrayList<String>();//intialize to empty ArrayList
        }
    }
    /*
     * Saves the modified data to the file
     */
    private void write_items()
    {
        File filesDir= getFilesDir();
        File dataFile= new File(filesDir,dataFileName);
        try {
            FileUtils.writeLines(dataFile,toDoList);
        } catch (IOException e) {
            //TODO:handle exception here
        }

    }
    /*
    * Instantiate the ToDOArrayList and Adapter
    * Populates the list with previously saved values
    */
    protected void populateItems()
    {
        read_items();
        toDoAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,toDoList);
    }

    /*
    * Event handler for when Add Item Button is pressed
    * Adds the Item to toDoList
    */
    public void addToDoItem(View view)
    {
       toDoAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        write_items();
    }
}
