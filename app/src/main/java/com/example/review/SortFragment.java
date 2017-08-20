package com.example.review;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SortFragment extends Fragment {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String[] item;
    private ArrayAdapter<String> mSortSettingsAdapter;

    public SortFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sort, container, false);

        item = new String[]{"Most recent", "Score", "Likes", "Votes"};
        final List<String> list = new ArrayList<String>(Arrays.asList(item));

        mSortSettingsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_sort,R.id.sort_list_text,list);

        final ListView listView = (ListView)rootView.findViewById(R.id.listview_sort);
        listView.setAdapter(mSortSettingsAdapter);

        pref = this.getActivity().getSharedPreferences("searchSettings", Context.MODE_PRIVATE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view.isSelected()){
                    view.setSelected(false);
                    editor = pref.edit();
                    editor.putString("sort", null);
                    editor.commit();
                }
                else {
                    editor = pref.edit();
                    editor.putString("sort", item[position]);
                    editor.commit();
                    view.setSelected(true);
                }
            }
        });


        return rootView;
    }
}