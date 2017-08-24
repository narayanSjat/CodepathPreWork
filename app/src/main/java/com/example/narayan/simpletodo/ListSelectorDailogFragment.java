package com.example.narayan.simpletodo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by narayan on 8/22/2017.
 */

public class ListSelectorDailogFragment extends DialogFragment {

    private ArrayList<String > lists;
    private ArrayList<String> deleted;
    private ListView lvItems;
    private DialogEditListener listener;
    public static ListSelectorDailogFragment newInstance(String title, ArrayList<String> lists) {
        ListSelectorDailogFragment frag = new ListSelectorDailogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        frag.lists=lists;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_lists, container);
        setCancelable(false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.lists_toolbar);
        toolbar.setTitle("List Folder");
        toolbar.inflateMenu(R.menu.switch_list_menu);
        listener=(DialogEditListener)getActivity();
        deleted=new ArrayList<String>();
        lvItems=(ListView)view.findViewById(R.id.lvAllLists);
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,lists);
        lvItems.setAdapter(adapter);
        setToolbarListener(toolbar);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                String listName=lists.get(position);
                deleted.add(listName);
                lists.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.updateSelectedList(lists.get(position));
                dismiss();
            }
        });
        return view;
    }

    private void setToolbarListener(Toolbar toolbar)
    {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.doneListSelection: {
                        listener.updateDeletedLists(deleted);
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
