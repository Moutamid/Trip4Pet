package com.moutamid.trip4pet;

import android.app.Application;

import com.moutamid.trip4pet.Stash;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stash.init(this);
    }
}
