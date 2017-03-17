package com.example.developer.test;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.developer.test.application.DaggerApp;
import com.example.developer.test.modulescomponents.AppComponent;
import com.example.developer.test.model.current.Current;
import com.example.developer.test.model.several.SevenDays;

import java.util.Iterator;
import java.util.Set;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.subjects.PublishSubject;



public class LoadWeather {

    @Inject
    WeatherLab weatherLab;
    @Inject
    Preferences preferences;
    private Retrofit retrofit;
    private WeatherAPI weatherAPI;
    private int count = 0;
    private Set<String> set;
    private String APPID =  "fc199427e9a8ee2bee5dc1222759d908";
    private String TYPE_UNIT = "metric";
    private String URL = "http://api.openweathermap.org";

    private PublishSubject<String> finishedLoadSubject;

    public LoadWeather(){
        AppComponent component = DaggerApp.dataComponent();
        component.inject(this);
    }

    private WeatherAPI createRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherAPI = retrofit.create(WeatherAPI.class);
        return weatherAPI;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Observable<String> startLoad(){
        finishedLoadSubject = PublishSubject.create();
        WeatherAPI weatherAPI = createRetrofit();
        set = preferences.getNewSet();
        String info;
        Iterator<String> iterator = set.iterator();

        weatherLab.fillListsWeather(set.size());
        for(int i = 0; i < set.size(); i ++){
            info = iterator.next();

            final int finalI = i;
            weatherAPI.getCurrentWeather(info, TYPE_UNIT,APPID)
                    .enqueue(new Callback< Current>(){

                        @Override
                        public void onResponse(Call<Current> call, Response<Current> response) {
                            if(response == null){
                                Log.d("EEE", "error response");
                                return;
                            }
                            weatherLab.addNewCurrentWeather(response.body(), finalI);
                            loadFinished();
                        }

                        @Override
                        public void onFailure(Call<Current> call, Throwable t) {
                            Log.d("EEE", "error " + t);
                            loadFinished(t);
                        }
                    });
            }

        Iterator<String> iteratorTwo = set.iterator();
        for(int i = 0; i < set.size(); i ++){
            info = iteratorTwo.next();
            final int finalI = i;
            weatherAPI.getSevenDaysWeather(info, TYPE_UNIT, APPID)
                    .enqueue(new Callback<SevenDays>(){

                        @Override
                        public void onResponse(Call<SevenDays> call, Response<SevenDays> response) {
                            weatherLab.addNewSevenDaysWeather(response.body(), finalI);
                            loadFinished();
                        }

                        @Override
                        public void onFailure(Call<SevenDays> call, Throwable t) {
                            Log.d("EEE", "error 2 " + t);
                            loadFinished(t);
                        }
                    });

        }
        return finishedLoadSubject.asObservable();
    }
    private void loadFinished(){
        count++;
        if(count == set.size() * 2){
            weatherLab.synchronize(true);
            preferences.newSetToNow();
            finishedLoadSubject.onNext("");
            Log.d("EEE", "ok");
            count = 0;
        }
    }
    private void loadFinished(Throwable t){
            weatherLab.synchronize(false);
            finishedLoadSubject.onError(t);
    }

}
