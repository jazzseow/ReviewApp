package com.example.review;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jesmond on 18/9/2016.
 */
public class FilterItemAdapter extends ArrayAdapter {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private int layout;
    private List<String> mObjects;


    public FilterItemAdapter(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
        layout = resource;
        mObjects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        pref = getContext().getSharedPreferences("searchSettings", Context.MODE_PRIVATE);
        editor = pref.edit();

        ViewHolder mainViewholder = null;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.attr = (TextView) convertView.findViewById(R.id.filter_list_text);
            viewHolder.min = (EditText) convertView.findViewById(R.id.min_attr);
            viewHolder.max = (EditText) convertView.findViewById(R.id.max_attr);
            viewHolder.clear = (Button) convertView.findViewById(R.id.clear_button);
            convertView.setTag(viewHolder);
        }
        mainViewholder = (ViewHolder) convertView.getTag();
        mainViewholder.min.setText("0");
        mainViewholder.max.setText("10");

        mainViewholder.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObjects.remove(position);
                notifyDataSetChanged();
            }
        });

        mainViewholder.min.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    editor.putFloat("min"+String.valueOf(position+1), Float.parseFloat(((EditText)v).getText().toString()));
                    editor.commit();
                }
            }
        });


        mainViewholder.max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    editor.putFloat("max"+String.valueOf(position+1), Float.parseFloat(((EditText)v).getText().toString()));
                    editor.commit();
                }
            }
        });

        return super.getView(position, convertView, parent);
    }

    public class ViewHolder{
        TextView attr;
        EditText min;
        EditText max;
        Button clear;
    }
}