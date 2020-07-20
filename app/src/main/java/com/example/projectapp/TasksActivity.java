package com.example.projectapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import java.util.ArrayList;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class TasksActivity extends AppCompatActivity implements AsyncResponse {

    ProgressBar Prog;
    HashMap<String, String> params = new HashMap<>();
    ScrollView storedOrders;
    int project_id;
    ListView listView;
    CustomAdapter_Tasks custom_adapter;
    MenuItem add_item;
    DataModel_Tasks dataModel;

    HttpOperations IO = new HttpOperations();
    ArrayList<DataModel_Tasks> dataModels;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final String DATA_SAVED_BROADCAST = "com.example.sportapconnection.datasaved";
    private DatabaseHelper db;
    private androidx.appcompat.view.ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);

        IO.delegate = this;

        Intent intent = getIntent();

        project_id = intent.getIntExtra("TaskListId", -1);
        listView = (ListView) findViewById(R.id.TListView);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(modeListener);

//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
//                if (mActionMode != null) {
//                    return false;
//                }
//                mActionMode = startSupportActionMode(mActionModeCallback);
//                return true;
//            }
//        });

        loadTasks();
    }

    AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {


        boolean check = false;
        int position = -1;

        @Override
        public boolean onCreateActionMode(android.view.ActionMode actionMode, Menu menu) {
            MenuInflater menuInflater = actionMode.getMenuInflater();
            menuInflater.inflate(R.menu.longpressmenu, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode actionMode, MenuItem menuItem) {


            switch (menuItem.getItemId())
            {
                case R.id.delete:

                    params = new HashMap<>();
                    Object object = listView.getItemAtPosition(position);
                    DataModel_Tasks dm=(DataModel_Tasks)object;
                    params.put("id", Integer.toString(dm.getId()));

                    IO.T(Api.URL_DELETE_TASK,params,CODE_POST_REQUEST,"DeleteTask",Prog,storedOrders,getApplicationContext(), TasksActivity.this,  IO.delegate, false, dm.getId());
                    refresh();
                    actionMode.finish();
                    return true;

                case R.id.edit:

                    Object object_Edit = listView.getItemAtPosition(position);
                    DataModel_Tasks dm_Edit=(DataModel_Tasks)object_Edit;

                    Intent intent = new Intent(getApplicationContext(),NewTaskActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("TaskListId", project_id);
                    bundle.putInt("TaskId", dm_Edit.id);
                    bundle.putString("TaskName", dm_Edit.name);
                    bundle.putString("TaskDate", dm_Edit.date);
                    bundle.putString("TaskPriority", dm_Edit.priority);
                    bundle.putInt("OpType",0);
                    intent.putExtras(bundle);

                    startActivity(intent);

                    return true;

                default:
                    return false;
            }

        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode actionMode) {

            check = false;
            position = -1;
        }

        @Override
        public void onItemCheckedStateChanged(android.view.ActionMode actionMode, int i, long l, boolean b) {
           if(!check)
           {
               check = true;
               position = i;
           }
            System.out.println(position);
        }
    };



    @Override
    public void processFinish(String output) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    @Override
    public void refresh() {
        loadTasks();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        add_item = menu.findItem(R.id.add);
        add_item.setVisible(true);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(getApplicationContext(),NewTaskActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("TaskListId", project_id);
                bundle.putInt("OpType", 1);
                intent.putExtras(bundle);

                startActivity(intent);
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }


    public void loadTasks() {
        dataModels = new ArrayList<>();
        Cursor cursor = db.getTasks(project_id);
        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt((cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE));
                String priority = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRIORITY));
                String status = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUS));
                int done = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_DONE));
                if (done == 0)
                {
                    dataModels.add(0, new DataModel_Tasks(id, 0,R.drawable.ic_panorama_fish_eye_black_24dp,0, name, date, priority, done));
                }
                else
                {
                    dataModels.add(0, new DataModel_Tasks(id, 0,R.drawable.ic_check_circle_black_24dp,0, name, date, priority, done));
                }

            } while (cursor.moveToNext());
        }

        setAdapter(dataModels, this);

    }

    public void changeAdapter(int icon , int icon2, int icon3) {
        dataModels = new ArrayList<>();
        Cursor cursor = db.getTasks(project_id);
        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt((cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE));
                String status = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUS));
                String priority = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRIORITY));
                int done = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_DONE));
                Log.d("tag", Integer.toString(id));
                Log.d("status", status);
                dataModels.add(0, new DataModel_Tasks(id, icon,icon2,icon3, name, date, priority, done));
            } while (cursor.moveToNext());
        }

        setAdapter(dataModels, this);

    }
    public void setAdapter(ArrayList<DataModel_Tasks> s, Context context)
    {
        custom_adapter = new CustomAdapter_Tasks(s, context, IO.delegate);
        listView.setAdapter(custom_adapter);
    }



    public void updateTaskData(int id, String name, int done, int tasklistid,int user_id, String date, String priority,String createtime, Context context)
    {
        params = new HashMap<>();
        params.put("id",Integer.toString(id));
        params.put("name",name);
        params.put("done", Integer.toString(done));
        params.put("tasklist_id", Integer.toString(tasklistid));
        params.put("user_id", Integer.toString(user_id));
        params.put("date", date);
        params.put("priority", priority);
        params.put("createtime", createtime);


        IO.T(Api.URL_SERVER_TASK_UPDATES,params,CODE_POST_REQUEST,"ServerUpdatesTask",Prog,storedOrders,context,TasksActivity.this,  IO.delegate, false, id);



    }

    public void deleteTask(int id, Context context)
    {
        params = new HashMap<>();
        params.put("id", Integer.toString(id));
        IO.T(Api.URL_DELETE_TASK,params,CODE_POST_REQUEST,"DeleteTask",Prog,storedOrders,context,TasksActivity.this,  IO.delegate, true, id);

    }

}
