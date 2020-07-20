package com.example.projectapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class HttpOperations {
    public AsyncResponse delegate = null;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    protected boolean ServerStatus=false;
    private DatabaseHelper db;

    public void T(String URL, HashMap<String, String> params, int Code, String ST, View ProgressView, View FormView, Context con, Activity act, AsyncResponse response, Boolean isAlreadyexist, int id)
    {
        db = new DatabaseHelper(con);
        if(ST.equals("InsTask") && isAlreadyexist == false)
        {
            saveTaskToLocalStorage(params.get("name"), Integer.parseInt(params.get("done")), Integer.parseInt(params.get("tasklist_id")), Integer.parseInt(params.get("user_id")), params.get("date"), params.get("priority"),params.get("createtime"), ST,NAME_NOT_SYNCED_WITH_SERVER, response);
            Cursor cursor = db.getLastTaskid();
            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt((cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                } while (cursor.moveToNext());
            }
        }

        else if (ST.equals("UpdateTaskStatus") && isAlreadyexist == false)
        {
            updateTaskStatusInLocalStorage(Integer.parseInt(params.get("id")),Integer.parseInt(params.get("done")), ST,NAME_NOT_SYNCED_WITH_SERVER, response);
        }

        else if (ST.equals("UpdateTaskName") && isAlreadyexist == false)
        {
            updateTaskNameInLocalStorage(Integer.parseInt(params.get("id")),params.get("name"), ST,NAME_NOT_SYNCED_WITH_SERVER, response);
        }

        else if (ST.equals("UpdateTask") && isAlreadyexist == false)
        {
            updateTaskInLocalStorage(Integer.parseInt(params.get("id")),params.get("name"), params.get("date"), params.get("priority"), ST,NAME_NOT_SYNCED_WITH_SERVER, response);
        }

        else if (ST.equals("DeleteTask") && isAlreadyexist == false)
        {
            deleteTaskInLocalStorage(Integer.parseInt(params.get("id")), response);
        }

        else if (ST.equals("InsTaskList") && isAlreadyexist == false)
        {
            saveTaskListToLocalStorage(params.get("name"), Integer.parseInt(params.get("done")),Integer.parseInt(params.get("user_id")), params.get("createtime"), ST,NAME_NOT_SYNCED_WITH_SERVER, response);
            Cursor cursor = db.getLastTaskListid();
            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt((cursor.getColumnIndex(DatabaseHelper.COLUMN_ID2)));
                } while (cursor.moveToNext());
            }
        }
        else if (ST.equals("UpdateTaskList") && isAlreadyexist == false)
        {
            updateTaskListInLocalStorage(Integer.parseInt(params.get("id")),params.get("name"), ST,NAME_NOT_SYNCED_WITH_SERVER, response);
        }

        else if (ST.equals("DeleteTaskList") && isAlreadyexist == false)
        {
            deleteTaskListInLocalStorage(Integer.parseInt(params.get("id")), response);
        }
        ServerResponse request = new ServerResponse(URL, params, Code, ST,ProgressView,FormView,con,act, response, id);
        request.execute();
    }

    protected class ServerResponse extends AsyncTask<String, String,String>
    {
        String url;
        HashMap<String, String> params;
        int requestCode;
        String ST;
        View ProgressView;
        View FormView;
        Context con;
        Activity act;
        AsyncResponse response;
        int id;

        ServerResponse(String url, HashMap<String, String> params, int requestCode, String ST,View ProgressView,View FormView, Context con, Activity act, AsyncResponse response, int id)
        {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
            this.ST = ST;
            this.ProgressView=ProgressView;
            this.FormView = FormView;
            this.con =con;
            this.act = act;
            this.response = response;
            this.id = id;
        }
        protected String doInBackground(String... urls)
        {

            exists(url,params,requestCode,ST,ProgressView,FormView, response, id);
            return "null";
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast toast = Toast.makeText(con, "OK", Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            if(!ServerStatus)
            {
                Toast toast = Toast.makeText(con, "Sorry, servers are current unreachable or your Internet connection is too low. Please,try again", Toast.LENGTH_SHORT);
                toast.show();

            }
            this.cancel(true);
            ServerStatus = false;


        }
    }

    public void exists(String URLName, HashMap<String, String> params, int requestCode, String ST,View ProgressView,View FormView, AsyncResponse response, int id)
    {
        try
        {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestMethod("HEAD");

            if(con.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                ServerStatus=true;
                PerformNetworkRequest request = new PerformNetworkRequest(URLName, params, requestCode, ST,ProgressView,FormView, response, id);
                request.execute();
            }
            else
            {
                ServerStatus=false;

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private class PerformNetworkRequest extends AsyncTask<Void, Void, String>
    {
        public AsyncResponse delegate = null;
        String url;
        HashMap<String, String> params;
        int requestCode;
        String ST;
        View ProgressView;
        View FormVIew;
        AsyncResponse response;
        int id;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode, String ST,View ProgressView,View FormVIew, AsyncResponse response, int id)
        {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
            this.ST = ST;
            this.ProgressView = ProgressView;
            this.FormVIew = FormVIew;
            this.response = response;
            this.id = id;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            try
            {
                JSONObject object = new JSONObject(s);

                if(ST.equals("InsTask"))
                {
                    if(!object.getBoolean(("error")))
                    {
                        db.updateTaskStatus(id, NAME_SYNCED_WITH_SERVER);
                     }
                }

                else if(ST.equals("InsTaskList"))
                {
                    if(!object.getBoolean(("error")))
                    {
                        db.updateTaskListStatus(id, NAME_SYNCED_WITH_SERVER);
                    }
                }

                else if(ST.equals("UpdateTaskList"))
                {
                    if(!object.getBoolean(("error")))
                    {
                        db.updateTaskListStatus(id, NAME_SYNCED_WITH_SERVER);
                    }
                }

                else if(ST.equals("UpdateTaskStatus"))
                {
                    if(!object.getBoolean(("error")))
                    {
                        db.updateTaskStatus(id, NAME_SYNCED_WITH_SERVER);
                    }
                }

                else if(ST.equals("UpdateTaskName"))
                {
                    if(!object.getBoolean(("error")))
                    {
                        db.updateTaskStatus(id, NAME_SYNCED_WITH_SERVER);
                    }
                }

                else if(ST.equals("UpdateTask"))
                {
                    if(!object.getBoolean(("error")))
                    {
                        db.updateTaskStatus(id, NAME_SYNCED_WITH_SERVER);
                    }
                }

                else if(ST.equals("DeleteTaskList"))
                {
                    if(!object.getBoolean(("error")))
                    {
                        db.droptmpId(id);
                    }
                }

                else if(ST.equals("DeleteTask"))
                {
                    if(!object.getBoolean(("error")))
                    {
                        db.droptmpId(id);
                    }
                }

                else if(ST.equals("ServerUpdatesList"))
                {
                    if(!object.getBoolean(("error")))
                    {
                        int id = Integer.parseInt(params.get("id"));
                        db.updateTaskListStatus(id, NAME_SYNCED_WITH_SERVER);
                    }
                }

                else if(ST.equals("ServerUpdatesTask"))
                {
                    if(!object.getBoolean(("error")))
                    {
                        int id = Integer.parseInt(params.get("id"));
                        db.updateTaskStatus(id, NAME_SYNCED_WITH_SERVER);
                    }
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids)
        {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    private void saveTaskToLocalStorage(String name, int done, int tasklist_id,int user_id, String date, String priority, String createtime, String optype,int status, AsyncResponse response) {
        System.out.println(optype);
        db.addTask(name, done, tasklist_id, user_id, date, priority, createtime,optype,status);
        //response.refresh();
    }

    private void saveTaskListToLocalStorage(String name, int done, int user_id, String createtime,String optype,int status, AsyncResponse response) {
        db.addTaskList(name, done, user_id, createtime, optype, status);
        response.refresh();
    }

    private void updateTaskListInLocalStorage(int id, String name, String optype,int status, AsyncResponse response) {
        db.updateTaskList(id,name, status, optype);
    }

    private void updateTaskNameInLocalStorage(int id, String name, String optype,int status, AsyncResponse response) {
        db.updateTaskName(id,name, status, optype);
    }

    private void updateTaskStatusInLocalStorage(int id, int done, String optype,int status, AsyncResponse response) {
        db.updateTaskC(id,done, status, optype);
    }

    private void updateTaskInLocalStorage(int id, String name, String date, String priority, String optype,int status, AsyncResponse response) {
        db.updateTask(id,name, date, priority, status,optype);
    }

    private void deleteTaskListInLocalStorage(int id, AsyncResponse response) {
        db.addtmpId(id, 0);
        db.dropTaskList(id);
        db.dropTasks(id);
    }

    private void deleteTaskInLocalStorage(int id, AsyncResponse response) {
        db.addtmpId(id, 1);
        db.dropTask(id);
    }
}
