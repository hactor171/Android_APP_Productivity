package com.example.projectapp;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;


public class TaskListActivity extends AppCompatActivity implements AsyncResponse{

    long startTime;
    long launchTime;
    private static final int CODE_POST_REQUEST = 1025;
    public static final String DATA_SAVED_BROADCAST = "com.example.projectapp.datasaved";
    private BroadcastReceiver broadcastReceiver;
    private DatabaseHelper db;
    ProgressBar Prog;
    TextView textView;
    ScrollView storedOrders;
    Button add;
    String value;
    EditText get_name;
    CustomAdapter custom_adapter;
    ListView listView;
    TextView allTaskscounter, incompleteTasksCounter;

    HashMap<String, String> params = new HashMap<>();


    HttpOperations IO = new HttpOperations();
    ArrayList<DataModel> dataModels;
    Test test = new Test();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTime = System.currentTimeMillis();

        db = new DatabaseHelper(this);
        //db.addTaskList("Row", 0, 1, Calendar.getInstance().getTime().toString(), "InsTaskList", 0);
        listView = (ListView) findViewById(R.id.HListView);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(modeListener);
        add = (Button) findViewById(R.id.addList);
        textView = (TextView) findViewById(R.id.text);

        allTaskscounter = (TextView) findViewById(R.id.countAllTasks);
        incompleteTasksCounter = (TextView) findViewById(R.id.countIncTasks);

        dataModels = new ArrayList<>();

        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                showTaskDialog("", 1, -1);

            }
        });
        IO.delegate = this;
        //registerReceiver(new NetworkStateClass(IO.delegate), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        refreshTaskCount();
        loadTaskLists();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                DataModel dataModel = dataModels.get(position);
                Intent intent = new Intent(getApplicationContext(),TasksActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("TaskListId", dataModel.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

       registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));

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
                    DataModel dm=(DataModel)object;
                    params.put("id", Integer.toString(dm.getId()));

                    IO.T(Api.URL_DELETE_TASKLIST,params,CODE_POST_REQUEST,"DeleteTaskList",Prog,storedOrders,getApplicationContext(), TaskListActivity.this,  IO.delegate, false, dm.getId());
                    refresh();
                    refreshTaskCount();
                    actionMode.finish();
                    return true;

                case R.id.edit:

                    Object objec_edit = listView.getItemAtPosition(position);
                    DataModel dm_edit=(DataModel)objec_edit;
                    actionMode.finish();
                    System.out.println(dm_edit.id);
                    showTaskDialog(dm_edit.name, 0, dm_edit.id);

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
    protected void onResume() {
        super.onResume();
        //registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Quit", "onPause");
        //unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.d("Quit", "onDestroy");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Quit", "onStop");
        //unregisterReceiver(broadcastReceiver);
    }

    public void loadTaskLists() {
        db = new DatabaseHelper(this);
        dataModels = new ArrayList<>();
        Cursor cursor = db.getTaskLists();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt((cursor.getColumnIndex(DatabaseHelper.COLUMN_ID2)));
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME2));
                String status = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUS2));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CREATETIME2));
                Log.d("tag", Integer.toString(id));
                Log.d("status", status);
                Cursor tasks = db.getTasks(id);
                dataModels.add(0, new DataModel(id, String.valueOf(tasks.getCount()),R.drawable.ic_keyboard_arrow_right_black_24dp, name, date));
            } while (cursor.moveToNext());
        }

        setAdapter(dataModels, this);

    }

    public void refreshTaskCount()
    {
        Cursor cursor = db.getAllTasks();
        Cursor cursorInc= db.getAllIncompleteTasks();

        allTaskscounter.setText(String.valueOf(cursor.getCount()));
        incompleteTasksCounter.setText(String.valueOf(cursorInc.getCount()));
    }


    public void updateListData(int id, String name, int done, int user_id, String createtime, Context context)
    {
        params = new HashMap<>();
        params.put("id", Integer.toString(id));
        params.put("name",name);
        params.put("done", Integer.toString(done));
        params.put("user_id", Integer.toString(user_id));
        params.put("createtime", createtime);

        IO.T(Api.URL_SERVER_LIST_UPDATES,params,CODE_POST_REQUEST,"ServerUpdatesList",Prog,storedOrders,context, TaskListActivity.this,  IO.delegate, true, id);


    }

    public void deleteTaskList(int id, Context context)
    {
        params = new HashMap<>();
        params.put("id", Integer.toString(id));
        IO.T(Api.URL_DELETE_TASKLIST,params,CODE_POST_REQUEST,"DeleteTaskList",Prog,storedOrders,context, TaskListActivity.this,  IO.delegate, true, id);

    }

    @Override
    public void processFinish(String output) { }

    @Override
    public void refresh() {
        loadTaskLists();
    }

    public void setAdapter(ArrayList<DataModel> s, Context context)
    {
        custom_adapter = new CustomAdapter(s, context, IO.delegate);
        listView.setAdapter(custom_adapter);
    }


    private void showTaskDialog(String text, final int op, final int list_id) {

        ViewGroup viewGroup = (ViewGroup) this.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.edit_dialog, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText addList  = (EditText) dialogView.findViewById(R.id.userInputDialog);

        addList.setText(text);

        addList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                addList.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager= (InputMethodManager) TaskListActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(addList, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        addList.requestFocus();

        builder.setView(dialogView);

        builder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        if(op == 1) {
                            Date currentTime = Calendar.getInstance().getTime();
                            params = new HashMap<>();
                            value = addList.getText().toString();
                            if ((value = addList.getText().toString()).equals("")) {
                                dialogBox.cancel();
                            } else {
                                params.put("name", value);
                                params.put("done", "0");
                                params.put("user_id", "1");
                                params.put("createtime", currentTime.toString());

                                addList.setText("");
                                Context mC = getApplicationContext();
                                IO.T(Api.URL_POST_TASKLIST, params, CODE_POST_REQUEST, "InsTaskList", Prog, storedOrders, mC, TaskListActivity.this, IO.delegate, false, 0);
                            }
                        }
                        else
                        {
                            params = new HashMap<>();
                            value = addList.getText().toString();
                            if ((value = addList.getText().toString()).equals("")) {
                                dialogBox.cancel();
                            } else {
                                params.put("id", Integer.toString(list_id));
                                params.put("name", value);

                                addList.setText("");
                                Context mC = getApplicationContext();
                                IO.T(Api.URL_UPDATE_TASKLIST, params, CODE_POST_REQUEST, "UpdateTaskList", Prog, storedOrders, mC, TaskListActivity.this, IO.delegate, false, list_id);
                                refresh();
                            }
                        }
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
