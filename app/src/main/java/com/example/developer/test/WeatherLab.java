package com.example.developer.test;

import android.util.Log;

import com.example.developer.test.model.current.Current;
import com.example.developer.test.model.several.SevenDays;

import java.util.ArrayList;
import java.util.List;


public class WeatherLab {
    private List<Current> currentWeather = new ArrayList<>();
    private List<Current> newCurrentWeather = new ArrayList<>();
    private List<SevenDays> sevenDaysWeather = new ArrayList<>();
    private List<SevenDays> newSevenDaysWeather = new ArrayList<>();

    public Current getCurrentWeatherByPos(int position) {
        return currentWeather.get(position);
    }
    public SevenDays getSevenDaysWeatherByPos(int position) {
        return sevenDaysWeather.get(position);
    }

    public void fillListsWeather(int count){
        for(int i = 0; i < count; i++){
            newCurrentWeather.add(i, null);
            newSevenDaysWeather.add(i, null);
        }
    }

    public void addNewCurrentWeather(Current current, int position){
        newCurrentWeather.remove(position);
        newCurrentWeather.add(position, current);
    }

    public void addNewSevenDaysWeather(SevenDays sevenDays, int position){
        newSevenDaysWeather.remove(position);
        newSevenDaysWeather.add(position, sevenDays);
    }

    public void synchronize(boolean isSynchronize){
        if(!isSynchronize){
            Log.d("EEE", "not synchronize");
            newCurrentWeather.clear();
            newSevenDaysWeather.clear();
        } else {
            Log.d("EEE", "synchronize " + currentWeather.size() + "  " + newCurrentWeather.size());
            currentWeather.clear();
            currentWeather.addAll(newCurrentWeather);
            newCurrentWeather.clear();
            sevenDaysWeather.clear();
            sevenDaysWeather.addAll(newSevenDaysWeather);
            newSevenDaysWeather.clear();
        }
    }
}
