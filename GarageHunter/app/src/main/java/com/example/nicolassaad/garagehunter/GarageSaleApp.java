package com.example.nicolassaad.garagehunter;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * This class sets the context that Firebase will work in
 */
public class GarageSaleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}

