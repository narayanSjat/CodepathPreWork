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
    private static final String DATABASE_NAME = "toToListDatabase2test";
    private static final int DATABASE_VERSION = 4;

    // Table Names
    private static final String TABLE_ITEMS = "ItemsTabletest";

    // Item Table Columns
    private static final String KEY_ITEM_ID = "itemId";
    private static final String KEY_ITEM_NAME = "itemName";
    private static final String KEY_ITEM_MEMO = "itemMemo";
    private static final String KEY_ITEM_DATE = "itemDate";
    private static final String KEY_ITEM_TIME = "itemTime";
    private static final String KEY_ITEM_PRIORITY = "itemPriority";
    private static final String KEY_ITEM_STATUS = "itemStatus";


    public static synchronized ItemsDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
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
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY, " + // Define a primary key
                KEY_ITEM_NAME + " TEXT," +
                KEY_ITEM_DATE + " TEXT," +
                KEY_ITEM_TIME + " TEXT," +
                KEY_ITEM_MEMO + " TEXT,"  +
                KEY_ITEM_PRIORITY +" INTEGER," +
                KEY_ITEM_STATUS + " BOOLEAN" +
                ")";
        db.execSQL(CREATE_ITEMS_TABLE);
    }

    /*
    * Current Implementation simply deletes and recreates the tables
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion!=newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }

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
            //values.put(KEY_ITEM_PRIORITY, newItem.getPriority());
            //values.put(KEY_ITEM_STATUS, newItem.getComplete());
            String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                    KEY_ITEM_ID, TABLE_ITEMS, KEY_ITEM_NAME);
            cursor=db.rawQuery(usersSelectQuery,new String[]{newItem.getName()});

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
        String POSTS_SELECT_QUERY = String.format("SELECT * FROM %s",
                        TABLE_ITEMS);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
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
            db.delete(TABLE_ITEMS, KEY_ITEM_NAME +"= ?",new String[]{item.getName()});
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error while deleting items from database");
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
