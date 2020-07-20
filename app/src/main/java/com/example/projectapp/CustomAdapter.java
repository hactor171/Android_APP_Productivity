package com.example.projectapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener, AsyncResponse{


    public AsyncResponse response;

    private ArrayList<DataModel> dataSet;
    Context mContext;

    @Override
    public void processFinish(String output) {

    }

    @Override
    public void refresh() {

    }


    private static class ViewHolder {
        EditText txtName;
        EditText txtDescription;
        TextView tasksCounter;
        ImageView icon2;
    }



    public CustomAdapter(ArrayList<DataModel> data, Context context, AsyncResponse delegate) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;
        this.response=delegate;

        //settings = mContext.getSharedPreferences(PREFS_FILE, mContext.MODE_PRIVATE);
        //ID_User = settings.getInt(PREF_ID, -1);
    }


    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;


        switch (v.getId()) { }
    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final DataModel dataModel = getItem(position);
        final ViewHolder viewHolder;

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (EditText) convertView.findViewById(R.id.textName);
            viewHolder.txtDescription = (EditText) convertView.findViewById(R.id.descriptionName);
            viewHolder.tasksCounter = (TextView) convertView.findViewById(R.id.textViewTasks);
            viewHolder.icon2 = (ImageView) convertView.findViewById(R.id.item_info2);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;


        viewHolder.tasksCounter.setText(dataModel.gettasksCounter());
        viewHolder.icon2.setImageResource(dataModel.getIcon2());
        viewHolder.icon2.setAdjustViewBounds(true);
        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtDescription.setText(dataModel.getDescription());

        return convertView;
    }

}
