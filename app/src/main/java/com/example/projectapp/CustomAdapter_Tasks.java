package com.example.projectapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapter_Tasks extends ArrayAdapter<DataModel_Tasks> implements View.OnClickListener{

    AsyncResponse response;
    TasksActivity ta = new TasksActivity();

    private ArrayList<DataModel_Tasks> dataSet;
    Context mContext;
    int check = 0;


    HttpOperations IO = new HttpOperations();
    ProgressBar Prog;
    ScrollView storedOrders;
    private static final int CODE_POST_REQUEST = 1025;
    HashMap<String, String> params = new HashMap<>();

    private static class ViewHolder {
        EditText txtName;
        TextView txtDate;
        ImageView icon;
        ImageView icon2;
        ImageView icon3;
    }



    public CustomAdapter_Tasks(ArrayList<DataModel_Tasks> data, Context context, AsyncResponse delegate) {
        super(context, R.layout.row_item_tasks, data);
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

        final DataModel_Tasks dataModel = getItem(position);
        final ViewHolder viewHolder;

        final View result;


        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_tasks, parent, false);
            viewHolder.txtName = (EditText) convertView.findViewById(R.id.ttextName);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.titem_info);
            viewHolder.icon2 = (ImageView) convertView.findViewById(R.id.titem_info2);
            viewHolder.icon3 = (ImageView) convertView.findViewById(R.id.titem_info3);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.tdate);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.icon.setImageResource(dataModel.getIcon());
        viewHolder.icon.setAdjustViewBounds(true);
        viewHolder.icon.setOnClickListener(this);
        viewHolder.icon.setTag(position);
        viewHolder.icon2.setImageResource(dataModel.getIcon2());
        viewHolder.icon2.setAdjustViewBounds(true);
        viewHolder.icon3.setImageResource(dataModel.getIcon3());
        viewHolder.icon3.setAdjustViewBounds(true);
        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtDate.setText(dataModel.getDate());


        viewHolder.icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataModel.getDone() == 0){
                    //viewHolder.icon2.setImageResource(R.drawable.ic_check_circle_black_24dp);
                    //viewHolder.txtName.setPaintFlags(viewHolder.txtName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    params = new HashMap<>();
                    params.put("id", Integer.toString(dataModel.getId()));
                    params.put("done",Integer.toString(1));
                    Log.d("id", Integer.toString(dataModel.getId()));
                    IO.T(Api.URL_UPDATEST_TASK,params,CODE_POST_REQUEST,"UpdateTaskStatus",Prog,storedOrders,mContext,ta,  IO.delegate, false, dataModel.getId());
                    response.refresh();
                }
                else if(dataModel.getDone() == 1)
                {
                    //viewHolder.icon2.setImageResource(R.drawable.ic_panorama_fish_eye_black_24dp);
                    //viewHolder.txtName.setPaintFlags(0);
                    params = new HashMap<>();
                    params.put("id", Integer.toString(dataModel.getId()));
                    Log.d("id", Integer.toString(dataModel.getId()));
                    params.put("done",Integer.toString(0));

                    IO.T(Api.URL_UPDATEST_TASK,params,CODE_POST_REQUEST,"UpdateTaskStatus",Prog,storedOrders,mContext,ta,  IO.delegate, false, dataModel.getId());
                    response.refresh();
                    check = 0;
                }


            }
        });

        return convertView;
    }
}
