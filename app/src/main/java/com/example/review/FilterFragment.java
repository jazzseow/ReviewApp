package com.example.review;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment implements OnItemSelectedListener {

    private ReviewDBHandler dbHandler;

    private Spinner catSpinner, attrSpinner;
    private ListView filterList;
    private List<String> filterItem;
    private FilterItemAdapter filterItemAdapter;
    private String oldItem;

    private List<Category> categoryList;
    private List<String> category;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public FilterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_filter, container, false);

        pref = this.getActivity().getSharedPreferences("searchSettings", Context.MODE_PRIVATE);
        editor = pref.edit();

        dbHandler = new ReviewDBHandler(getContext());

        //Filter Items
        filterList = (ListView) view.findViewById(R.id.filter_listview);

        Set<String> set = pref.getStringSet("attributes", null);

        if (set == null){
            filterItem = new ArrayList<String>();
        }
        else {
            filterItem = new ArrayList<String>(set);
        }

        filterItemAdapter = new FilterItemAdapter(getContext(), R.layout.list_item_filter, R.id.filter_list_text, filterItem);
        filterList.setAdapter(filterItemAdapter);

        //Dropdown Menu
        catSpinner = (Spinner) view.findViewById(R.id.spinner_category);
        attrSpinner = (Spinner) view.findViewById(R.id.spinner_attr);

        categoryList = dbHandler.getAllCategories();

        category = new ArrayList<String>();
        category.add("Any");

        for (Category c : categoryList) {
            category.add(c.getCategoryName());
        }

        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, category);
        catSpinner.setAdapter(catAdapter);

        int prefCat = pref.getInt("categorynum", 0);
        catSpinner.setSelection(prefCat);

        catSpinner.setOnItemSelectedListener(this);
        attrSpinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_category:
                HashMap<String, List<String>> attr = new HashMap<String, List<String>>();

                for (Category c : categoryList) {
                    List<String> catAttr = new ArrayList<String>();
                    catAttr.add(c.getAttr1Name());
                    catAttr.add(c.getAttr2Name());
                    catAttr.add(c.getAttr3Name());

                    attr.put(c.getCategoryName(), catAttr);
                }

                String selectedCat = catSpinner.getSelectedItem().toString();

                List<String> attribute = new ArrayList<String>();
                attribute.add("Attributes");

                if (selectedCat != "Any") {
                    attribute.addAll(attr.get(selectedCat));
                }

                ArrayAdapter<String> attrAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, attribute);
                attrSpinner.setAdapter(attrAdapter);
                attrAdapter.notifyDataSetChanged();

                //clear items if category is changed
                if (oldItem != null && oldItem != selectedCat) {
                    filterItem.clear();
                    filterItemAdapter.notifyDataSetChanged();
                }
                oldItem = selectedCat;

                editor.putString("category", category.get(position));
                editor.putInt("categorynum",position);
                editor.putInt("attr1", -1);
                editor.putInt("attr2", -1);
                editor.putInt("attr3", -1);
                editor.commit();
                break;
            case R.id.spinner_attr:
                String selectedAttr = attrSpinner.getSelectedItem().toString();

                if (selectedAttr != "Attributes" && !filterItem.contains(selectedAttr)) {
                    filterItem.add(selectedAttr);
                    editor.putInt("attr"+String.valueOf(filterItem.size()), attrSpinner.getSelectedItemPosition());
                    editor.commit();
                    filterItemAdapter.notifyDataSetChanged();
                }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        ;
    }

}