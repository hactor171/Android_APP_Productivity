package com.example.projectapp;


public class DataModel {

    String tasksCounter;
    int icon2;
    String name;
    String description;
    int id;

    public DataModel(int id, String tasksCounter, int icon2,  String name, String description) {
        this.tasksCounter=tasksCounter;
        this.icon2=icon2;
        this.name=name;
        this.description=description;
        this.id=id;
    }


    public int getId() { return id; }
    public String gettasksCounter() { return tasksCounter; }
    public int getIcon2() {
        return icon2;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public void setName(String name) {
        this.name = name;
    }
}
