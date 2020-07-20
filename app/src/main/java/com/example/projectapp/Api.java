package com.example.projectapp;


public class Api {


    private static final String ROOT_URL = "http://192.168.64.2/ConApi/v1/Api.php?apicall";//dane lokalne

    public static final String URL_POST_TASK = ROOT_URL + "=inserttask";
    public static final String URL_DELETE_TASK = ROOT_URL + "=deletetask";
    public static final String URL_UPDATEST_TASK = ROOT_URL + "=updatetaskstatus";
    public static final String URL_UPDATENAME_TASK = ROOT_URL + "=updatetaskname";
    public static final String URL_POST_TASKLIST = ROOT_URL + "=create_t_list";
    public static final String URL_UPDATE_TASKLIST = ROOT_URL + "=update_t_list";
    public static final String URL_DELETE_TASKLIST = ROOT_URL + "=delete_t_list";
    public static final String URL_SERVER_LIST_UPDATES = ROOT_URL + "=tasklistchanges";
    public static final String URL_SERVER_TASK_UPDATES = ROOT_URL + "=taskchanges";

}
