package com.example.narayan.simpletodo;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import database.Item;
import database.ItemsDatabaseHelper;

public class MainActivity extends AppCompatActivity implements ItemEditListener {
    private final String dataFileName="toDo_ItemsData.txt";
    private ArrayList<Item> toDoList;
    private ToDoListArrayAdapter toDoAdapter;
    private ListView lvItems;
    protected ItemsDatabaseHelper dbInstance;
    private Toolbar primToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set toolbar as the action bar
        primToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(primToolbar);
        // Initialize objects and variables

        lvItems=(ListView)findViewById(R.id.lvItems);
        dbInstance=ItemsDatabaseHelper.getInstance(this);
        populateItems();
        // Attach listeners and other set up tasks
        lvItems.setAdapter(toDoAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showEditDailog(toDoList.get(position), ItemDialogFragment.MODE.EDIT);
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /*
     * Reads the List of previously added items from the file and adds them to toDoList
     */
    private void read_items()
    {

        toDoList= (ArrayList)dbInstance.getAllItems();

    }

    /*
    * Instantiate the ToDOArrayList and Adapter
    * Populates the list with previously saved values
    */
    protected void populateItems()
    {
        read_items();
        toDoAdapter= new ToDoListArrayAdapter(this,toDoList);
    }


    public void showEditDailog(Item item, ItemDialogFragment.MODE mode)
    {
        FragmentManager fm = getSupportFragmentManager();
        ItemDialogFragment frag= ItemDialogFragment.newInstance("EDIT ITEM", item, mode );
        frag.show(fm,"fragment_edit_item");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.addNewItem)
        {
            Item newItem=new Item();
            newItem.setName("DUMMY");
            showEditDailog(newItem, ItemDialogFragment.MODE.ADD);
        }
        return true;
    }


  // <----- Method Implementations for the ItemEditLister Interface ---->
    @Override
    public void addItem(Item item) {
        long index=dbInstance.addItem(item);
        if (index<=toDoList.size()&&index>0)
        {
            Toast.makeText(this,"Item already exists in the list",Toast.LENGTH_LONG).show();
        }
        else if (index<0)
        {
            Toast.makeText(this,"Error occured while adding the Item",Toast.LENGTH_LONG).show();
        }
        else
        {
            toDoAdapter.add(item);
        }
    }

    @Override
    public void updateItem(Item upItem, Item oldItem)
    {
        long dbIndex= dbInstance.updateItem(upItem,oldItem);
        if (dbIndex<0)
        {
            Toast.makeText(this,"Error occurred while updating the Item",Toast.LENGTH_LONG).show();
        }
        else
        {
            int index=toDoList.indexOf(oldItem);
            toDoList.set(index,upItem);
            toDoAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void deleteItem(Item item)
    {
        toDoList.remove(item);
        dbInstance.deleteItem(item);
        toDoAdapter.notifyDataSetChanged();
    }
}
