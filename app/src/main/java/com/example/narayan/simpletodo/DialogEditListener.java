package com.example.narayan.simpletodo;

import java.util.ArrayList;

import database.Item;

/**
 * Created by narayan on 8/20/2017.
 */

public interface DialogEditListener {
    void addItem(Item item);
    void updateItem(Item upItem, Item oldItem);
    void deleteItem (Item item);
    void updateDeletedLists(ArrayList<String> deletedLists);
    void updateSelectedList(String list);
    void addList(String list);
}
