package com.example.developer.test.fragments;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.developer.test.databinding.ItemRwWeatherBinding;
import com.example.developer.test.model.several.SevenDays;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AdapterRWFragment extends RecyclerView.Adapter<AdapterRWFragment.MyHolder> {

    private SevenDays sevenDays;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM/yy");
    private int MILLIS = 1000;


    public AdapterRWFragment(SevenDays sevenDays){
        this.sevenDays = sevenDays;
    }

    @Override
    public AdapterRWFragment.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemRwWeatherBinding binding = ItemRwWeatherBinding.inflate(layoutInflater, parent, false);

        MyHolder myHolder = new MyHolder(binding.getRoot());
        return myHolder;
    }

    @Override
    public void onBindViewHolder(AdapterRWFragment.MyHolder holder, int position) {
            holder.binding.setList(sevenDays.getList().get(position));
            holder.binding.setFragment(this);

    }

    public String getDt(long dt){
        long millis = dt * MILLIS;
        String date =  sdf.format(new Date(millis));
        return date;
    }



    @Override
    public int getItemCount() {
        return sevenDays.getList().size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ItemRwWeatherBinding binding;
        public MyHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
