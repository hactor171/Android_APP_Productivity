package com.example.projectapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "NamesDB";
    public static final String TABLE_NAME = "tasks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DONE = "done";
    public static final String COLUMN_TASKLISTID = "tasklist_id";
    public static final String COLUMN_USERID = "user_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_CREATETIME = "createtime";
    public static final String COLUMN_OPTYPE = "optype";
    public static final String COLUMN_STATUS = "status";


    public static final String TABLE_NAME2 = "tasklist";
    public static final String COLUMN_ID2 = "id";
    public static final String COLUMN_NAME2 = "name";
    public static final String COLUMN_DONE2 = "done";
    public static final String COLUMN_USERID2 = "user_id";
    public static final String COLUMN_CREATETIME2 = "createtime";
    public static final String COLUMN_OPTYPE2 = "optype";
    public static final String COLUMN_STATUS2 = "status";

    public static final String TABLE_NAME3 = "tmpid";
    public static final String COLUMN_ID3 = "id";
    public static final String COLUMN_CHECK3 = "checklist";

    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME
                + "(" + COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME +
                " VARCHAR, " + COLUMN_DONE +
                " TINYINT, " + COLUMN_TASKLISTID +
                " INTEGER, " + COLUMN_USERID +
                " INTEGER, " + COLUMN_DATE +
                " VARCHAR, " + COLUMN_PRIORITY +
                " VARCHAR, " + COLUMN_CREATETIME +
                " VARCHAR, " + COLUMN_OPTYPE +
                " VARCHAR, " + COLUMN_STATUS +
                " TINYINT);";
        String sql2 = "CREATE TABLE " + TABLE_NAME2
                + "(" + COLUMN_ID2 +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME2 +
                " VARCHAR, " + COLUMN_DONE2 + " TINYINT, " +
                COLUMN_USERID2 + " INTEGER, " +
                COLUMN_CREATETIME2 + " VARCHAR, " +
                COLUMN_OPTYPE2 + " VARCHAR, " +
                COLUMN_STATUS2 + " TINYINT);";

        String sql3 = "CREATE TABLE " + TABLE_NAME3
                + "(" + COLUMN_ID3 + " INTEGER, " + COLUMN_CHECK3 + " TINYINT);";
        System.out.println(sql);
        db.execSQL(sql);
        db.execSQL(sql2);
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Persons";
        db.execSQL(sql);
        onCreate(db);
    }

    public boolean addTask(String name, int done, int tasklist_id,int user_id, String date,
                           String priority, String createtime, String optype,int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DONE, done);
        contentValues.put(COLUMN_TASKLISTID, tasklist_id);
        contentValues.put(COLUMN_USERID, user_id);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_PRIORITY, priority);
        contentValues.put(COLUMN_CREATETIME, createtime);
        contentValues.put(COLUMN_OPTYPE, optype);
        contentValues.put(COLUMN_STATUS, status);

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public boolean addTaskList(String name, int done, int user_id, String createtime, String optype,int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME2, name);
        contentValues.put(COLUMN_DONE2, done);
        contentValues.put(COLUMN_USERID2, user_id);
        contentValues.put(COLUMN_CREATETIME2, createtime);
        contentValues.put(COLUMN_OPTYPE2, optype);
        contentValues.put(COLUMN_STATUS2, status);

        db.insert(TABLE_NAME2, null, contentValues);

        db.close();
        return true;
    }

    public boolean addtmpId(int id, int checklist) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ID3, id);
        contentValues.put(COLUMN_CHECK3, checklist);

        db.insert(TABLE_NAME3, null, contentValues);
        db.close();
        return true;
    }

    public boolean droptmpId(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ID3, id);

        db.delete(TABLE_NAME3, COLUMN_ID3 + "=" + id, null);
        db.close();
        return true;
    }


    public boolean dropTaskList(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ID2, id);

        db.delete(TABLE_NAME2, COLUMN_ID2 + "=" + id, null);
        db.close();
        return true;
    }

    public boolean dropTasks(int tasklist_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TASKLISTID, tasklist_id);

        db.delete(TABLE_NAME, COLUMN_TASKLISTID + "=" + tasklist_id, null);
        db.close();
        return true;
    }

    public boolean dropTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ID, id);

        db.delete(TABLE_NAME, COLUMN_ID + "=" + id, null);
        db.close();
        return true;
    }

    public boolean updateTaskListStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS2, status);
        db.update(TABLE_NAME2, contentValues, COLUMN_ID2 + "=" + id, null);
        db.close();
        return true;
    }

    public boolean updateTaskList(int id, String name,int status, String optype) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS2, status);
        contentValues.put(COLUMN_NAME2, name);
        contentValues.put(COLUMN_OPTYPE2, optype);
        db.update(TABLE_NAME2, contentValues, COLUMN_ID2 + "=" + id, null);
        db.close();
        return true;
    }

    public boolean updateTaskName(int id, String name,int status, String optype) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_OPTYPE, optype);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + "=" + id, null);
        db.close();
        return true;
    }

    public boolean updateTaskC(int id, int done,int status, String optype) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        contentValues.put(COLUMN_DONE, done);
        contentValues.put(COLUMN_OPTYPE, optype);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + "=" + id, null);
        db.close();
        return true;
    }

    public boolean updateTaskDate(int id, String date,int status, String optype) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_OPTYPE, optype);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + "=" + id, null);
        db.close();
        return true;
    }


    public boolean updateTask(int id, String name,String date, String priority,int status, String optype) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_PRIORITY, priority);
        contentValues.put(COLUMN_OPTYPE, optype);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + "=" + id, null);
        db.close();
        return true;
    }

    public boolean updateTaskStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + "=" + id, null);
        db.close();
        return true;
    }

    public Cursor getTasks(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "+ COLUMN_TASKLISTID +" = " + id + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getAllIncompleteTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "+ COLUMN_DONE +" = " + 0 + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getTaskid(String createtime) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CREATETIME + " = " + "'"+createtime+"'"+ ";";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getLastTaskid() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT id FROM " + TABLE_NAME + " WHERE id = (SELECT MIN(id) FROM " + TABLE_NAME +" WHERE status = 0)" + ";";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getLastTaskListid() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT id FROM " + TABLE_NAME2 + " WHERE id = (SELECT MIN(id) FROM " + TABLE_NAME2 +" WHERE status = 0)" + ";";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public Cursor getTaskListid(String createtime) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME2 + " WHERE " + COLUMN_CREATETIME2 + " = " +"'"+createtime+"'"+ ";";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getTaskLists() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME2 + " ORDER BY " + COLUMN_ID2 + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public Cursor getUnsyncedLists() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME2 + " WHERE " + COLUMN_STATUS2 + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor gettmpList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME3 + " ;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
}
