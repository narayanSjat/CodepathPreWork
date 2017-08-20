package com.example.narayan.simpletodo;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import database.Item;
import database.ItemsDatabaseHelper;

public class MainActivity extends AppCompatActivity implements ItemDialogFragment.EditItemDialogListener {
    private final String dataFileName="toDo_ItemsData.txt";
    private ArrayList<Item> toDoList;
    private ToDoListArrayAdapter toDoAdapter;
    private ListView lvItems;
    private EditText etEditText;
    protected ItemsDatabaseHelper dbInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize objects and variables
        lvItems=(ListView)findViewById(R.id.lvItems);
        etEditText=(EditText)findViewById(R.id.etEditText);
        dbInstance=ItemsDatabaseHelper.getInstance(this);
        populateItems();
        // Attach listeners and other set up tasks
        lvItems.setAdapter(toDoAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                /*Item removedItem= toDoList.remove(position);
                dbInstance.deleteItem(removedItem);
                toDoAdapter.notifyDataSetChanged();
                return true;*/
                showEditDailog(toDoList.get(position));
                return true;
            }
        });
    }

    /*
     * Reads the List of previously added items from the file and adds them to toDoList
     */
    private void read_items()
    {

        toDoList= (ArrayList)dbInstance.getAllItems();

        /*ArrayList<String> itemNames;
        File filesDir= getFilesDir();
        File dataFile= new File(filesDir,dataFileName);
        try {
            itemNames=new ArrayList<String>(FileUtils.readLines(dataFile));
            toDoList=new ArrayList<Item>();
            for (String s: itemNames){
                Item i=new Item();
                i.setName(s);
                toDoList.add(i);
            }
        } catch (IOException e) {
            toDoList=new ArrayList<Item>();//intialize to empty ArrayList
        }*/

    }
    /*
     * Saves the modified data to the file
     */
 /*   private void write_items()
    {
       /* File filesDir= getFilesDir();
        File dataFile= new File(filesDir,dataFileName);
        try {
            FileUtils.writeLines(dataFile,toDoList);
        } catch (IOException e) {
            //TODO:handle exception here
        }

    }*/
    /*
    * Instantiate the ToDOArrayList and Adapter
    * Populates the list with previously saved values
    */
    protected void populateItems()
    {
        read_items();
        toDoAdapter= new ToDoListArrayAdapter(this,toDoList);
    }

    /*
    * Event handler for when Add Item Button is pressed
    * Adds the Item to toDoList
    */
    public void addToDoItem(View view)
    {
        Item newItem= new Item();
        newItem.setName(etEditText.getText().toString());
        toDoAdapter.add(newItem);
        dbInstance.addItem(newItem);
        etEditText.setText("");
       // write_items();
    }

    public void showEditDailog(Item item)
    {
        FragmentManager fm = getSupportFragmentManager();
        ItemDialogFragment frag= ItemDialogFragment.newInstance("EDIT ITEM", item);
        frag.show(fm,"fragment_edit_item");
    }
    @Override
    public void updateItem(Item upItem, Item oldItem)
    {
        dbInstance.updateItem(upItem,oldItem);
        int index=toDoList.indexOf(oldItem);
        toDoList.set(index,upItem);
        toDoAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteItem(Item item)
    {
        toDoList.remove(item);
        dbInstance.deleteItem(item);
        toDoAdapter.notifyDataSetChanged();
    }
}
