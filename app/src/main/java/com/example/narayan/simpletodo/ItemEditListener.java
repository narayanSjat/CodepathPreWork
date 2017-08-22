package com.example.narayan.simpletodo;

import database.Item;

/**
 * Created by narayan on 8/20/2017.
 */

public interface ItemEditListener {
    void addItem(Item item);
    void updateItem(Item upItem, Item oldItem);
    void deleteItem (Item item);
}
