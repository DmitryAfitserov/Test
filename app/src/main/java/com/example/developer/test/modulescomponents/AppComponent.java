package com.example.developer.test.modulescomponents;

import com.example.developer.test.AdapterViewPager;
import com.example.developer.test.GeoCoder;
import com.example.developer.test.LoadWeather;
import com.example.developer.test.MainActivity;
import com.example.developer.test.PositionControlActivity;
import com.example.developer.test.fragments.FragmentHome;
import com.example.developer.test.fragments.FragmentRecyclerView;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = DaggerModule.class)
public interface AppComponent {
    void inject(MainActivity mainActivity);
    void inject(LoadWeather loadWeather);
    void inject(PositionControlActivity positionControlActivity);
    void inject(GeoCoder geoCoder);
    void inject(AdapterViewPager adapterViewPager);
    void inject(FragmentRecyclerView fragmentRecyclerView);
    void inject(FragmentHome fragmentHome);

}
