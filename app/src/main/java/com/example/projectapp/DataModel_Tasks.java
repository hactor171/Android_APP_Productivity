package com.example.projectapp;


public class DataModel_Tasks {

    int icon;
    int icon2;
    int icon3;
    String name;
    String date;
    String priority;
    int id;
    int done;


    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public DataModel_Tasks(int id, int icon, int icon2, int icon3, String name, String date, String priority, int done) {
        this.icon=icon;
        this.icon2=icon2;
        this.icon3=icon3;
        this.name=name;
        this.id=id;
        this.date=date;
        this.done=done;
        this.priority=priority;
    }

    public int getIcon3() {
        return icon3;
    }

    public void setIcon3(int icon3) {
        this.icon3 = icon3;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() { return id; }
    public int getIcon() { return icon; }
    public int getIcon2() {
        return icon2;
    }
    public String getName() {
        return name;
    }

    public String getPriority() {
        return priority;
    }

    public void setName(String name) {
        this.name = name;
    }
}
