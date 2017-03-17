package com.example.developer.test.modulescomponents;

import android.content.Context;

import com.example.developer.test.LoadWeather;
import com.example.developer.test.WeatherLab;
import com.example.developer.test.Preferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class DaggerModule {
    Context context;

    public DaggerModule(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
     public Preferences providesPreferences(){
        return new Preferences(context);
    }

    @Provides
    @Singleton
    public LoadWeather providesLoadWeather(){
        return new LoadWeather();
    }

    @Provides
    @Singleton
    public WeatherLab providesWeatherLab(){
        return new WeatherLab();
    }

    @Provides
    @Singleton
    public Context providesContext(){
        return context;
    }
}
