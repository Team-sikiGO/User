package com.example.user;

import android.app.Application;

public class Privacy extends Application {

    private String Name;
    private String Number;

    @Override
    public void onCreate() {
        Name = null;
        Number = null;
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setNumber(String Number) {
        this.Number = Number;
    }

    public String getNumber() {
        return Number;
    }
}
