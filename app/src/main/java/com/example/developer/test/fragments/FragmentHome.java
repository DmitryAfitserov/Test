package com.example.developer.test.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.developer.test.R;
import com.example.developer.test.WeatherLab;
import com.example.developer.test.application.DaggerApp;
import com.example.developer.test.modulescomponents.AppComponent;
import com.example.developer.test.databinding.HomeFragmentBinding;
import com.example.developer.test.model.current.Current;

import javax.inject.Inject;


public class FragmentHome extends Fragment {

    private static String KEY_BUNDLE = "position";
    HomeFragmentBinding binding;
    int position;

    @Inject
    WeatherLab weatherLab;

    public static FragmentHome newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(KEY_BUNDLE, position);
        FragmentHome fragment = new FragmentHome();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("EEE", "create view home_page");
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        AppComponent component = DaggerApp.dataComponent();
        component.inject(this);

        position = getArguments().getInt(KEY_BUNDLE);

        binding.setFragment(this);
        Current current = weatherLab.getCurrentWeatherByPos(position);
        binding.setCurrent(current);


        return binding.getRoot();
    }




}
