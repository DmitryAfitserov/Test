package com.example.developer.test.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.developer.test.R;
import com.example.developer.test.WeatherLab;
import com.example.developer.test.application.DaggerApp;
import com.example.developer.test.modulescomponents.AppComponent;
import com.example.developer.test.databinding.RecyclerviewFragmentBinding;

import javax.inject.Inject;


public class FragmentRecyclerView extends Fragment {

    RecyclerviewFragmentBinding binding;
    int position;
    RecyclerView recyclerview;
    AdapterRWFragment adapterRW;

    @Inject
    WeatherLab weatherLab;

    public static FragmentRecyclerView newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);

        FragmentRecyclerView fragment = new FragmentRecyclerView();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.recyclerview_fragment, container, false);
        position = getArguments().getInt("position");

        AppComponent component = DaggerApp.dataComponent();
        component.inject(this);

        recyclerview = binding.recyclerView;
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(container.getContext()));
        adapterRW = new AdapterRWFragment(weatherLab.getSevenDaysWeatherByPos(position));
        recyclerview.setAdapter(adapterRW);


        return binding.getRoot();
    }
}
