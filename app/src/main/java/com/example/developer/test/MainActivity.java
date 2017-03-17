package com.example.developer.test;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.developer.test.application.DaggerApp;
import com.example.developer.test.modulescomponents.AppComponent;
import com.example.developer.test.databinding.ActivityMainBinding;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private int REQUEST_CODE_ACTIVITY_CONTROL = 1;
    private int RESULT_OK = 1;
    private ViewPager viewPager;
    private AdapterViewPager adapterViewPager;
    private Observable<String> observable;

    @Inject Preferences preferences;

    @Inject LoadWeather loadWeather;

    @Inject WeatherLab weatherLab;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        AppComponent component = DaggerApp.dataComponent();
        component.inject(this);

        tabLayout = binding.tabLayout;
        binding.setIsVisibleProgressBar(true);
        binding.setIsVisibleTabLayout(false);

        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);


        if(isOnline()) {

            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home));
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.clock));
            binding.setIsVisibleProgressBar(true);
            createTabLayoutListener();

            loadWeather();
        } else {
            Toast.makeText(getApplicationContext(), R.string.error_internet_connection, Toast.LENGTH_SHORT)
                    .show();
            binding.setIsVisibleProgressBar(false);
        }
    }

    private void createViewPager(){
        viewPager = binding.viewPager;
        adapterViewPager = new AdapterViewPager(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setOffscreenPageLimit(preferences.getSet().size());
        viewPager.setCurrentItem(preferences.getDefaultPage());
        binding.setIsVisibleTabLayout(true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                int currentPage = viewPager.getCurrentItem();
                String nameCity = weatherLab.getCurrentWeatherByPos(currentPage).getName();
                binding.setTitleText(nameCity);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void loadWeather(){
            observable = loadWeather.startLoad();
            observable.subscribe(new Action1<String>() {

                @Override
                public void call(String s) {
                        if(viewPager == null){
                            createViewPager();
                        } else {
                            adapterViewPager.notifyDataSetChanged();
                            viewPager.setOffscreenPageLimit(preferences.getSet().size());
                            adapterViewPager.notifyDataSetChanged();
                        }
                        binding.setIsVisibleProgressBar(false);
                        int currentPage = viewPager.getCurrentItem();
                        toolbar.setTitle(weatherLab.getCurrentWeatherByPos(currentPage).getName());
                        Log.d("EEE", "startView");
                }
            }, new Action1<Throwable>(){
                @Override
                public void call(Throwable throwable) {
                    binding.setIsVisibleProgressBar(false);
                    Toast.makeText(getApplicationContext(), R.string.error_loading_data
                            , Toast.LENGTH_SHORT).show();
                }
            });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu , menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.position_controln : {
                Intent intent = new Intent(this, PositionControlActivity.class);
                startActivityForResult(intent , REQUEST_CODE_ACTIVITY_CONTROL);
                return true;
            }
            case R.id.default_page :{
                int currentPage = viewPager.getCurrentItem();
                String nameCity = weatherLab.getCurrentWeatherByPos(currentPage).getName();
                preferences.setDefaultPage(nameCity);
                return true;
            }
            case R.id.update : {
                binding.setIsVisibleProgressBar(true);
                loadWeather();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_ACTIVITY_CONTROL){
            if(resultCode == RESULT_OK){
                binding.setIsVisibleProgressBar(true);
                viewPager.setCurrentItem(0);
                loadWeather();
            }
        }
    }

    private void createTabLayoutListener(){

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int numberTab = tab.getPosition();
                for(int i = 0; i < adapterViewPager.getCount(); i++){
                    adapterViewPager.getFragment(i).changeFragment(numberTab == 0? true: false);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
