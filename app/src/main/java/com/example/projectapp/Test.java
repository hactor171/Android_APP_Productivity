package com.example.projectapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Test {


    private DatabaseHelper db;
    Date currentTime;
    long starttime;
    long endtime;


    public void TestRowsInsert(Context con)
    {
        db = new DatabaseHelper(con);

        Log.d("Czas testu", "Start");
        starttime = System.currentTimeMillis();

        ArrayList dataModels = new ArrayList<>();
        Cursor cursor = db.getTaskLists();

        endtime = System.currentTimeMillis();
        Log.d("Czas testu", "End");
        Log.d("Czas testu", String.valueOf((endtime - starttime)));


        Log.d("Czas testu", "Start");
        starttime = System.currentTimeMillis();
        for(int i = 0; i<1000; i++)
        {
            db.updateTaskList(i, "name" + i, 0, "Update");
        }
        endtime = System.currentTimeMillis();
        Log.d("Czas testu", "End");
        Log.d("Czas testu", String.valueOf((endtime - starttime)));


    }


}
