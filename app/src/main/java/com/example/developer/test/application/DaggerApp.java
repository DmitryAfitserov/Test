package com.example.developer.test.application;

import android.app.Application;


import com.example.developer.test.modulescomponents.AppComponent;
import com.example.developer.test.modulescomponents.DaggerAppComponent;
import com.example.developer.test.modulescomponents.DaggerModule;


public class DaggerApp extends Application {
    private static AppComponent component;

    public static AppComponent dataComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder().daggerModule(new DaggerModule(this)).build();

    }

}
