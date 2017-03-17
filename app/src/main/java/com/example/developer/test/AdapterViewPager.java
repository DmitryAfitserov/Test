package com.example.developer.test;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.developer.test.application.DaggerApp;
import com.example.developer.test.modulescomponents.AppComponent;
import com.example.developer.test.fragments.FragmentTitle;

import javax.inject.Inject;

/**
 * Created by Dmitry on 07.03.2017.
 */

public class AdapterViewPager extends FragmentStatePagerAdapter {

    SparseArray<FragmentTitle> registeredFragments = new SparseArray<>();

    @Inject
    Preferences preferences;

    public AdapterViewPager(FragmentManager fm) {
        super(fm);
        AppComponent component = DaggerApp.dataComponent();
        component.inject(this);
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentTitle.newInstance(position);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int getCount() {
        return preferences.getSet().size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        FragmentTitle fragment = (FragmentTitle) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        Log.d("EEE", "put frag pos= " + position);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        Log.d("EEE", "remove frag pos= " + position);
        super.destroyItem(container, position, object);
    }

    public FragmentTitle getFragment(int position){
        return registeredFragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
