package com.example.narayan.simpletodo;

import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

import database.Item;
import database.ItemsDatabaseHelper;

public class MainActivity extends AppCompatActivity implements DialogEditListener {
    private final String dataFileName="toDo_ItemsData.txt";
    private ArrayList<Item> toDoList;
    private ToDoListArrayAdapter toDoAdapter;
    private ListView lvItems;
    private TextView currListLabel;
    private TextView emptyListView;
    protected ItemsDatabaseHelper dbInstance;
    private Toolbar primToolbar;
    private String currList=null;
    private HashSet<String> allCurrLists=null;
    private static String noListsMessage="No Lists Added";
    private static String addItemMessage="Add a new Item";
    private static String addListMessage="Add a new List";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set toolbar as the action bar
        primToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(primToolbar);

        // Initialize objects and variables
        lvItems=(ListView)findViewById(R.id.lvItems);
        currListLabel=(TextView)findViewById(R.id.currListLabel);
        emptyListView=(TextView)findViewById(R.id.emptyView) ;
        dbInstance=ItemsDatabaseHelper.getInstance(this);
        dbInstance.addList(currList);
        dbInstance.setCurrentList(currList);
        populateItems();

        // Attach listeners and other set up tasks
        lvItems.setAdapter(toDoAdapter);
        lvItems.setEmptyView(emptyListView);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                showEditDailog(toDoList.get(position), ItemDialogFragment.MODE.EDIT);
                view.setSelected(false);
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
    * Instantiate the ToDOArrayList and Adapter
    * Populates the list with previously saved values
    */
    protected void populateItems()
    {
        allCurrLists=new HashSet<String>(dbInstance.getAllLists());
        for (String s: allCurrLists){
            currList=s;
            dbInstance.setCurrentList(s);
        }
        if (currList==null)
        {
            toDoList=new ArrayList<Item>();
            currListLabel.setText(noListsMessage);
            emptyListView.setText(addListMessage);
        }
        else {
            toDoList= (ArrayList)dbInstance.getAllItems();
            currListLabel.setText(currList);
            emptyListView.setText(addItemMessage);
        }
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
            showEditDailog(newItem, ItemDialogFragment.MODE.ADD);
        }
        else if(item.getItemId()==R.id.displayLists)
        {
            ArrayList<String> allLists=new ArrayList<String>(allCurrLists);
            FragmentManager fm = getSupportFragmentManager();
            ListSelectorDailogFragment frag=ListSelectorDailogFragment.newInstance("All Lists",allLists);
            frag.show(fm,"fragment_show_lists");
        }
        else if (item.getItemId()==R.id.addNewList)
        {
            FragmentManager fm = getSupportFragmentManager();
            AddListDialog frag=AddListDialog.newInstance();
            frag.show(fm,"fragment_add_list");
        }
        return true;
    }


  // <----- Method Implementations for the ItemEditLister Interface ---->
    @Override
    public void addItem(Item item) {
        item.setListName(currList);
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
        upItem.setListName(currList);
        long dbIndex= dbInstance.updateItem(upItem,oldItem);
        if (dbIndex<0)
        {
            Toast.makeText(this,"Error occurred while updating the Item",Toast.LENGTH_LONG).show();
        }
        else
        {
            int index=toDoList.indexOf(oldItem);
            toDoList.set(index,upItem);
            int viewPriority=upItem.getPriority();
            if (upItem.getComplete()){viewPriority=-1;}
            toDoAdapter.notifyDataSetChanged();
            int pos =toDoAdapter.getPosition(upItem);
            ImageView imgView=(ImageView)lvItems.getChildAt(pos).findViewById(R.id.itemStatus);
            imgView.setImageResource(getResStatusImage(viewPriority));

        }

    }

    @Override
    public void deleteItem(Item item)
    {
        toDoList.remove(item);
        dbInstance.deleteItem(item);
        toDoAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateDeletedLists(ArrayList<String> deletedLists)
    {
        boolean currListDeleted=false;
        for (String listToDelete: deletedLists)
        {
            allCurrLists.remove(listToDelete);
            dbInstance.deleteList(listToDelete);
            if (listToDelete.equals(currList))
                currListDeleted=true;
        }
        if (currListDeleted)
        {
            currList=null;
            for (String list :allCurrLists)
            {
                currList=list;
                currListLabel.setText(currList);
                dbInstance.setCurrentList(currList);
                allCurrLists.add(currList);
                refreshAllItems();
                return;
            }
        }
        if (currList==null)
        {
            currListLabel.setText(noListsMessage);
            emptyListView.setText(addListMessage);
            dbInstance.setCurrentList("");
            toDoList.clear();
            toDoAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void updateSelectedList(String list) {
        if (allCurrLists.contains(list))
        {
            currListLabel.setText(list);
            currList=list;
            dbInstance.setCurrentList(list);
            refreshAllItems();
        }
        else
        {
            Toast.makeText(this,"A List with this name does not exits! ",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void addList(String list) {
        if (allCurrLists.contains(list))
        {
            Toast.makeText(this,"A List with this name already Exists!",Toast.LENGTH_LONG).show();
        }
        else
        {
            if (currList==null)
            {
                emptyListView.setText(addItemMessage);
            }
            currListLabel.setText(list);
            currList=list;
            dbInstance.setCurrentList(list);
            dbInstance.addList(list);
            allCurrLists.add(list);
            refreshAllItems();
        }

    }

    /*
    * Updates the data in the Listview. Refreshes all the items with a new list from database
     */
    private void refreshAllItems()
    {
        toDoList.clear();
        toDoList.addAll(dbInstance.getAllItems());
        toDoAdapter.notifyDataSetChanged();
    }
    /*
    * Returns Image resource id based on priority
     */
    @DrawableRes int getResStatusImage(int priority)
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
