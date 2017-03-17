package com.example.developer.test.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.developer.test.R;
import com.example.developer.test.databinding.TitleFragmentBinding;



public class FragmentTitle extends Fragment {

    int position;
    TitleFragmentBinding binding;
    FragmentHome fragmentHome;
    FragmentRecyclerView fragmentRecyclerView;


    public static Fragment newInstance(int position){
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        Fragment fragmentTitle = new FragmentTitle();
        fragmentTitle.setArguments(bundle);
        return fragmentTitle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        position = getArguments().getInt("position");
        binding = DataBindingUtil.inflate(inflater, R.layout.title_fragment, container, false);

        fragmentHome = new FragmentHome().newInstance(position);
        fragmentRecyclerView = new FragmentRecyclerView().newInstance(position);

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        if(tabLayout.getSelectedTabPosition() == 0){
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container, fragmentHome).commit();
        } else {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container, fragmentRecyclerView).commit();
        }

        return binding.getRoot();

    }

    public void changeFragment(boolean isHome){
        if(!isHome){
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container, fragmentRecyclerView).commit();
        } else {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container, fragmentHome).commit();
        }

    }
}
