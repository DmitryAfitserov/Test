package com.example.developer.test;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.developer.test.databinding.ItemRwPositionControlBinding;

import java.util.List;

/**
 * Created by Dmitry on 05.03.2017.
 */

public class AdapterRVPositionControl extends RecyclerView.Adapter<AdapterRVPositionControl.MyViewHolder> {

    private List<String> listName;
    private String MINSK = "Minsk";
    private String MOSCOW = "Moscow";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemRwPositionControlBinding binding;

        public MyViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);


        }

    }

    public AdapterRVPositionControl(List list){
        listName = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemRwPositionControlBinding binding = ItemRwPositionControlBinding.inflate(
                layoutInflater, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(binding.getRoot());

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.binding.setNameCity(listName.get(position));
        if(listName.get(position).equals(MINSK) ||
                listName.get(position).equals(MOSCOW)){
            holder.binding.setIsVisible(false);
        } else{
            holder.binding.setIsVisible(true);
            holder.binding.imageDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemRemoved(position);
                    listName.remove(position);

                    notifyDataSetChanged();
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return listName.size();
    }
}