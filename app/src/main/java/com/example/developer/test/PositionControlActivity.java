package com.example.developer.test;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.developer.test.application.DaggerApp;
import com.example.developer.test.modulescomponents.AppComponent;
import com.example.developer.test.databinding.ActivityPositionControlBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

public class PositionControlActivity extends AppCompatActivity implements InterfaceChooseCity {

    ActivityPositionControlBinding binding;
    @Inject
    Preferences preferences;
    Button okButton;
    Button cancelButton;
    Button applyButton;
    List<String> listCity = new ArrayList<>();
    int RESULT_CANCEL = 0;
    int RESULT_OK = 1;
    ArrayAdapter<String> adapterEditText;
    AdapterRVPositionControl adapterRVPositionControl;
    List<String> listSearch = new ArrayList<>();
    InterfaceChooseCity interfaceChooseCity;
    AutoCompleteTextView searchView;
    String nameCity;
    List<Address> listAddress;

    RecyclerView recyclerView;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_position_control);
        binding.toolbar.setTitle(R.string.control_position);
        binding.toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(binding.toolbar);
        AppComponent component = DaggerApp.dataComponent();
        component.inject(this);

        fillingListCity();
        createSearchView();
        createRecyclerView();
        createPanelControl();
    }


    private void createSearchView(){
        searchView = binding.searchView;


        okButton = binding.addCity;

        adapterEditText = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, listSearch);
        searchView.setAdapter(adapterEditText);
        interfaceChooseCity = this;
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 3){
                    GeoCoder geoCoder = new GeoCoder(s.toString(), interfaceChooseCity);
                    geoCoder.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                    nameCity = s.toString();
                }
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdded = false;
                exitLabel:
                for(int i = 0; i < listSearch.size(); i++ ){
                    if(listSearch.get(i).toLowerCase().contains(nameCity.toLowerCase())){
                        for(String temp:listCity){
                            if(temp.toLowerCase().equals(listAddress.get(i).getLocality().toLowerCase())){
                                Toast.makeText(getApplicationContext(), R.string.city_is_exist,
                                        Toast.LENGTH_SHORT).show();
                                isAdded = true;

                                break exitLabel;
                            }

                        }
                        listCity.add(listAddress.get(i).getLocality());
                        searchView.setText("");
                        isAdded = true;
                        adapterRVPositionControl.notifyDataSetChanged();
                    }
                }
                if(!isAdded){
                    Toast.makeText(getApplicationContext(), R.string.choose_city_from_popup,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createRecyclerView(){
        recyclerView = binding.recyclerviewPositionControl;
        adapterRVPositionControl = new AdapterRVPositionControl(listCity);
        recyclerView.setAdapter(adapterRVPositionControl);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fillingListCity(){
        Set<String> set = preferences.getSet();
        for(String temp: set){
            listCity.add(temp);
        }
    }

    public void createPanelControl(){
        cancelButton = binding.cancelControlPosition;
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCEL);
                finish();
            }
        });
        applyButton = binding.applyControlPosition;
        applyButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Set<String> set = preferences.getSet();
                    if(listCity.size() != set.size()){
                        writeSet(listCity);
                        setResult(RESULT_OK);
                        finish();
                        return;
                    }
                    int count = 0;
                    for(String temp:set){
                        for(String tempC:listCity){
                            if(temp.equals(tempC)){
                                count++;
                                break;
                            }
                        }
                    }
                    if(count == listCity.size()){
                        setResult(RESULT_CANCEL);
                        finish();
                        return;
                    } else {
                        writeSet(listCity);
                        setResult(RESULT_OK);
                        finish();
                        return;
                    }
            }
        });
    }

    @Override
    public void setList(List<Address> list) {
        listAddress = list;
        if(list != null) {
            String result = "";
            adapterEditText.clear();
            listSearch.clear();
            for (Address address : list) {
                result = "";

                if(address.getLocality() != null){
                    result +=  " " + address.getLocality();
                }
                if(address.getLocality() != null){
                    result += " " + address.getCountryName();
                }

                if(result.length() > 0){
                    listSearch.add(result);
                }

            }
            adapterEditText = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, listSearch);
            searchView.setAdapter(null);
            searchView.setAdapter(adapterEditText);
        } else {
            Toast.makeText(getApplicationContext(), R.string.geocoder_service_not_availablet,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void writeSet(List<String> list){
        Set<String> set = preferences.getNewSet();
        set.clear();
        for(String temp: list){
            set.add(temp);
        }
        preferences.setNewSet(set);
    }
}
