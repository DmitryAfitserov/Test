package com.example.developer.test;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.ArraySet;
import android.util.Log;

import java.util.Iterator;
import java.util.Set;



public class Preferences {

    String defaultPage;
    SharedPreferences preferences;
    String PREFS_NAME = "MyPrefer";
    String KEY_DEFAULT_PAGE = "dp";
    String KEY_SET = "set";
    String MINSK = "Minsk";
    String MOSCOW = "Moscow";

    Set<String> newSet;


    @TargetApi(Build.VERSION_CODES.M)
    public Preferences(Context context){
        preferences = context.getSharedPreferences(PREFS_NAME, 0);
        newSet = new ArraySet<>();
        newSet.addAll(getSet());

    }

    public int getDefaultPage(){
        defaultPage = preferences.getString(KEY_DEFAULT_PAGE, MINSK);
        Set<String> set = getSet();
        Iterator<String> iterator = set.iterator();
        for(int i = 0; i < set.size(); i++){
            if(defaultPage.equals(iterator.next())){
                return i;
            }
        }
        return 0;
    }

    public void setDefaultPage(String nameCity){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_DEFAULT_PAGE, nameCity);
        editor.commit();

    }


    public Set getSet(){
        Set<String> set = preferences.getStringSet(KEY_SET, null);
        if(set == null){
            set = createSet();
        }
        return set;
    }

    public Set getNewSet(){
        return newSet;
    }

    public void setNewSet(Set set){
        newSet = set;
    }


    @TargetApi(Build.VERSION_CODES.M)
    private Set createSet(){
        Set<String> tempSet = new ArraySet<>();
        tempSet.add(MINSK);
        tempSet.add(MOSCOW);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(KEY_SET, tempSet);
        editor.commit();
        return  tempSet;
    }

    public void newSetToNow(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(KEY_SET, newSet);
        editor.commit();
    }





}
