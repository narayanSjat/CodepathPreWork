package com.example.narayan.simpletodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import database.Item;


/**
 * Created by narayan on 8/19/2017.
 */

public class ItemDialogFragment extends DialogFragment implements View.OnClickListener{

    private Item currItem;
    private EditText etItemName;
    private Button btSave, btDelete;
    private EditItemDialogListener listener;
    public ItemDialogFragment()
    {
        //Empty Constructor
    }

    public static ItemDialogFragment newInstance(String title, Item item)
    {
        ItemDialogFragment frag=new ItemDialogFragment();
        Bundle args= new Bundle();
        args.putString("title",title);
        frag.setArguments(args);
        frag.currItem=item;
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view= inflater.inflate(R.layout.custom_edit_view,container);
        setCancelable(false);
        listener = (EditItemDialogListener) getActivity();
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
        // Set up button and textview elements
        etItemName=(EditText) view.findViewById(R.id.editItemName);
        etItemName.setText(currItem.getName());
        btSave=(Button) view.findViewById(R.id.saveEdits);
        btSave.setOnClickListener(this);
        btDelete=(Button)view.findViewById(R.id.deleteItem);
        btDelete.setOnClickListener(this);
    }




    @Override
    public void onClick(View v)
    {

        if(v.getId()==R.id.saveEdits)
        {
            Item updatedItem=new Item();
            updatedItem.setName(etItemName.getText().toString());
            if (!currItem.equals(updatedItem)){
                listener.updateItem(updatedItem,currItem);
            }
        }
        else if (v.getId()==R.id.deleteItem)
        {
            listener.deleteItem(currItem);
        }
        dismiss();

    }

    public interface EditItemDialogListener
    {
        void updateItem(Item upItem, Item oldItem);
        void deleteItem (Item item);
    }
}
