package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by narayan on 8/19/2017.
 */

public class ItemsDatabaseHelper extends SQLiteOpenHelper{

    private static ItemsDatabaseHelper sInstance; // Maintain one instance of the database class
    private static final String DATABASE_NAME = "ListItDatabase";
    private static final int DATABASE_VERSION = 4;

    // Table Names
    private static String TABLE_LISTS= "ALL_LISTS";
    private static String TABLE_ITEMS = "ALL_ITEMS";

    //List Table Column
    private static final String KEY_LIST_ID = "listId";
    private static final String KEY_LIST_TABLE_NAME = "listName";

    // Item Table Columns Item
    private static final String KEY_ITEM_ID = "itemId";
    private static final String KEY_ITEM_NAME = "itemName";
    private static final String KEY_ITEM_MEMO = "itemMemo";
    private static final String KEY_ITEM_DATE = "itemDate";
    private static final String KEY_ITEM_TIME = "itemTime";
    private static final String KEY_ITEM_PRIORITY = "itemPriority";
    private static final String KEY_ITEM_STATUS = "itemStatus";
    private static final String KEY_ITEM_LIST_NAME="itemList";

    private static String currentListName=null;


    public static synchronized ItemsDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new ItemsDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private ItemsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    /*
    * Create tables here, called when database is created
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LISTS_TABLE = "CREATE TABLE " + TABLE_LISTS +
                "(" +
                KEY_LIST_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_LIST_TABLE_NAME + " TEXT" +
                ")";
        db.execSQL(CREATE_LISTS_TABLE);
        db.execSQL(getCreateItemTableQuery(TABLE_ITEMS));
    }

    /*
    * Returns the Query for crete a new ITEM Table
     */
    private String getCreateItemTableQuery(String tableName)
    {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + tableName +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY, " + // Define a primary key
                KEY_ITEM_NAME + " TEXT," +
                KEY_ITEM_DATE + " TEXT," +
                KEY_ITEM_TIME + " TEXT," +
                KEY_ITEM_MEMO + " TEXT,"  +
                KEY_ITEM_PRIORITY +" INTEGER," +
                KEY_ITEM_STATUS + " BOOLEAN," +
                KEY_ITEM_LIST_NAME+ " TEXT" +
        ")";
        return CREATE_ITEMS_TABLE;
    }
    /*
    * Current Implementation simply deletes and recreates the tables
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion!=newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS);
            onCreate(db);
        }
    }


    /*
    * In current Implementation, this needs to be set before any add, update or delete operations
    */
    public void setCurrentList(String list)
    {
        currentListName=list;
    }


    //<--- Methods for adding and updating TABLE_ALL_LISTS ----------------->
	 /*
      Returns a list of Lists stored in the database
     */
    public List<String> getAllLists()
    {
        List<String> lists=new ArrayList<String>();
        String POSTS_SELECT_QUERY = String.format("SELECT * FROM %s",
                TABLE_LISTS);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    lists.add(cursor.getString(cursor.getColumnIndex(KEY_LIST_TABLE_NAME)));
                }while (cursor.moveToNext());
            }

        }catch(Exception e)
        {
            Log.d(TAG, "Error while reading lists from the database");
        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return lists;
    }
    /*
    * Gets all list name from the table

    public ArrayList<String> getAllLists ()
    {
        ArrayList<String> listNames= new ArrayList<String>();
        String POSTS_SELECT_QUERY = String.format("SELECT DISTINCT %s FROM %s",
                KEY_ITEM_LIST_NAME,TABLE_ITEMS);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY,null);
        try
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    String listName= cursor.getString(cursor.getColumnIndex(KEY_ITEM_LIST_NAME));
                    listNames.add(listName);
                }while (cursor.moveToNext());
            }

        }catch(Exception e)
        {
            Log.d(TAG, "Error while reading all lists from database");
        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return listNames;
    }

*/
        /*
* Add new List to the database
* @Return returns item id of the newly added List or of an existing List that matches the name.
*  Returns -1 if unsuccessful
*/
    public long addList(String newList)
    {
        long listId=-1;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor=null;
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put(KEY_LIST_TABLE_NAME, newList);
            String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                    KEY_LIST_ID, TABLE_LISTS, KEY_LIST_TABLE_NAME);
            cursor=db.rawQuery(usersSelectQuery,new String[]{newList});

            // check if any enteries already exist, and return item id
            if (cursor.moveToFirst())
            {
                listId=cursor.getInt(0);
                db.setTransactionSuccessful();
            }
            // no existing enteries, add a new entry
            else
            {
                listId=db.insertOrThrow(TABLE_LISTS,null,values);
                db.setTransactionSuccessful();
            }

        }catch (Exception e)
        {
            Log.d(TAG, "Error while trying to add new List");

        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.endTransaction();
        }
        return listId;
    }

    /*
    * Deletes a given list from the database
    */
    public void deleteList (String list)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try
        {
            db.delete(TABLE_LISTS, KEY_LIST_TABLE_NAME +"= ?",new String[]{list});
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error while deleting List from database");
        }finally {
            db.endTransaction();
            removeListItems(list);
        }
    }

    //----------------------------------- Item Methods ----------------------------------//
    /*
    * Add new Items to database
    * @Return returns item id of the newly added item or of an existing item that matches the item name.
    *  Returns -1 if unsuccessful
     */
    public long addItem(Item newItem)
    {
        long itemId=-1;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor=null;
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_NAME, newItem.getName());
            values.put(KEY_ITEM_DATE, newItem.getDate());
            values.put(KEY_ITEM_TIME, newItem.getTime());
            values.put(KEY_ITEM_MEMO, newItem.getMemo());
            values.put(KEY_ITEM_PRIORITY, newItem.getPriority());
            values.put(KEY_ITEM_STATUS, newItem.getComplete());
            values.put(KEY_ITEM_LIST_NAME,newItem.getListName());
            String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ? AND %s = ?",
                    KEY_ITEM_ID, TABLE_ITEMS, KEY_ITEM_NAME, KEY_ITEM_LIST_NAME);
            cursor=db.rawQuery(usersSelectQuery,new String[]{newItem.getName(),newItem.getListName()});

            // check if any enteries already exist, and return item id
            if (cursor.moveToFirst())
            {
                itemId=cursor.getInt(0);
                db.setTransactionSuccessful();
            }
            // no existing enteries, add a new entry
            else
            {
                itemId=db.insertOrThrow(TABLE_ITEMS,null,values);
                db.setTransactionSuccessful();
            }

        }catch (Exception e)
        {
            Log.d(TAG, "Error while trying to add new Item");

        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.endTransaction();
        }
        return itemId;
    }

    /*
    * Update an existing Item in the database
    * Returns the Item id of the entry that is modified
     */
    public long updateItem(Item newItem, Item oldItem)
    {
        long itemId=-1;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_NAME, newItem.getName());
            values.put(KEY_ITEM_DATE, newItem.getDate());
            values.put(KEY_ITEM_TIME, newItem.getTime());
            values.put(KEY_ITEM_MEMO, newItem.getMemo());
            values.put(KEY_ITEM_PRIORITY, newItem.getPriority());
            values.put(KEY_ITEM_STATUS, newItem.getComplete());
            values.put(KEY_ITEM_LIST_NAME,newItem.getListName()); //TODO in future need to tweek this to handle item migration to another list
            itemId=db.update(TABLE_ITEMS,values,KEY_ITEM_NAME+ " = ?",new String[]{oldItem.getName()});
            db.setTransactionSuccessful();
        }catch(Exception e)
        {
            Log.d(TAG, "Error while trying to update the Item");
        }finally {
            db.endTransaction();
        }
        return itemId;
    }

    /*
      Returns a list of Items stored in the database
     */
    public List<Item> getAllItems()
    {
        List<Item> toDoList=new ArrayList<Item>();
        String POSTS_SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = ?",
                TABLE_ITEMS,KEY_ITEM_LIST_NAME);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, new String[]{currentListName});
        try
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    Item item=new Item();
                    item.setName(cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME)));
                    item.setTime(cursor.getString(cursor.getColumnIndex(KEY_ITEM_TIME)));
                    item.setDate(cursor.getString(cursor.getColumnIndex(KEY_ITEM_DATE)));
                    item.setMemo(cursor.getString(cursor.getColumnIndex(KEY_ITEM_MEMO)));
                    item.setPriority(cursor.getInt(cursor.getColumnIndex(KEY_ITEM_PRIORITY)));
                    item.setComplete(cursor.getInt(cursor.getColumnIndex(KEY_ITEM_STATUS))>0);
                    toDoList.add(item);
                }while (cursor.moveToNext());
            }

        }catch(Exception e)
        {
            Log.d(TAG, "Error while reading Items from the database");
        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return toDoList;
    }

    /*
      Deletes an Item from the table
     */
    public void deleteItem (Item item)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try
        {
            //TODO: Need to also check for list in future when item migration is possible
            db.delete(TABLE_ITEMS, KEY_ITEM_NAME +"= ?",new String[]{item.getName()});
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error while deleting items from database");
        }finally {
            db.endTransaction();
        }
    }

    /*
    * Deletes all enteries for a specific list in the Table
     */
    public void removeListItems(String list)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try
        {
            //TODO: Need to also check for list in future when item migration is possible
            db.delete(TABLE_ITEMS, KEY_ITEM_LIST_NAME +"= ?",new String[]{list});
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error while deleting list from the database");
        }finally {
            db.endTransaction();

        }

    }
    /*
       Clears all enteries
     */

    public void deleteAllItems ()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try
        {
            db.delete(TABLE_ITEMS,null,null);
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error while deleting items from database");
        }finally {
            db.endTransaction();
        }
    }
}
