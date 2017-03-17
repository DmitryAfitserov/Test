package com.example.developer.test;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.developer.test.application.DaggerApp;
import com.example.developer.test.modulescomponents.AppComponent;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;


public class GeoCoder extends AsyncTask<Void, Void, List> {

    String request;
    @Inject
    Context context;
    InterfaceChooseCity myInterface;

    public GeoCoder(String request, InterfaceChooseCity myInterface) {
        this.myInterface = myInterface;
        this.request = request;
        AppComponent component = DaggerApp.dataComponent();
        component.inject(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected List doInBackground(Void... params) {
        List<Address> addresses = null;

        if (Geocoder.isPresent()) {
            try {
                if (addresses != null) {
                    addresses.clear();
                }
                Geocoder gc = new Geocoder(context, new Locale.Builder()
                        .setLanguage("en").setRegion("RU").build());
                addresses = gc.getFromLocationName(request, 5);

                for (int i = 0; i < addresses.size(); i++) {
                    String a = addresses.get(i).getCountryName();
                    String c = addresses.get(i).getLocality();
                    String v = addresses.get(i).getSubAdminArea();
                }
            } catch (IOException e) {
                Log.d("EEE", "GeoCoder " + e.toString());
                return null;
            }
            return addresses;
        }
        return null;
    }

    @Override
    protected void onPostExecute(List list) {
        myInterface.setList(list);
    }
}
