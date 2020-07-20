package com.example.projectapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkStateClass extends BroadcastReceiver{

    private Context context;
    AsyncResponse response;
    TaskListActivity ma = new TaskListActivity();
    TasksActivity ta = new TasksActivity();
    private DatabaseHelper db;

    public NetworkStateClass(AsyncResponse delegate) {
        response = delegate;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        db = new DatabaseHelper(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                Cursor cursor = db.getUnsyncedLists();

                if (cursor.moveToFirst()) {
                    do {
                            ma.updateListData(
                                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID2)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME2)),
                                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_DONE2)),
                                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_USERID2)),
                                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CREATETIME2)),
                                    context

                            );

                    } while (cursor.moveToNext());

                }

                Cursor cursor2 = db.gettmpList();

                if (cursor2.moveToFirst()) {
                    do {

                        int checklist = cursor2.getInt(cursor2.getColumnIndex(DatabaseHelper.COLUMN_CHECK3));
                        if(checklist == 0)
                        {
                            ma.deleteTaskList(
                                    cursor2.getInt(cursor2.getColumnIndex(DatabaseHelper.COLUMN_ID3)),
                                    context

                            );
                        }
                        else
                        {
                            ta.deleteTask(
                                    cursor2.getInt(cursor2.getColumnIndex(DatabaseHelper.COLUMN_ID3)),
                                    context

                            );
                        }
                    } while (cursor2.moveToNext());

                }

                Cursor cursor3 = db.getUnsyncedTasks();

                if (cursor3.moveToFirst()) {
                    do {

                            ta.updateTaskData(
                                    cursor3.getInt(cursor3.getColumnIndex(DatabaseHelper.COLUMN_ID)),
                                    cursor3.getString(cursor3.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
                                    cursor3.getInt(cursor3.getColumnIndex(DatabaseHelper.COLUMN_DONE)),
                                    cursor3.getInt(cursor3.getColumnIndex(DatabaseHelper.COLUMN_TASKLISTID)),
                                    cursor3.getInt(cursor3.getColumnIndex(DatabaseHelper.COLUMN_USERID)),
                                    cursor3.getString(cursor3.getColumnIndex(DatabaseHelper.COLUMN_DATE)),
                                    cursor3.getString(cursor3.getColumnIndex(DatabaseHelper.COLUMN_PRIORITY)),
                                    cursor3.getString(cursor3.getColumnIndex(DatabaseHelper.COLUMN_CREATETIME)),
                                    context

                            );
                    } while (cursor3.moveToNext());

                }


                response.refresh();
            }
        }
    }
}

