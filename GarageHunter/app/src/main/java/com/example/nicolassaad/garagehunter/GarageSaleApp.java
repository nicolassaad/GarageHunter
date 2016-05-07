package com.example.nicolassaad.garagehunter;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by nicolassaad on 5/6/16.
 */
public class GarageSaleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}

