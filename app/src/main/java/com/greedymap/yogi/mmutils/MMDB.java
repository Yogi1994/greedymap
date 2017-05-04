package com.greedymap.yogi.mmutils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.android.gms.maps.model.LatLng;
import com.maxmind.db.CHMCache;
import com.maxmind.db.Reader;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.IspResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by yogi on 2/5/17.
 */

public class MMDB {


    public static Reader reader = null;

    public MMDB(Context context){

        InputStream inputStream = context.getResources().openRawResource(
                context.getResources().getIdentifier("geolite2city",
                        "raw", context.getPackageName()));
        File database = new File(context.getCacheDir(), "geolite2city.mmdb");

        try {

            OutputStream output = new FileOutputStream(database);
            try {
                try {
                    byte[] buffer = new byte[1024 * 1024];
                    int read;

                    while ((read = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }
                    output.flush();
                } finally {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace(); // handle exception, define IOException and others
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
             reader = new Reader(database,new CHMCache());
//            reader = new DatabaseReader.Builder(database, new CHMCache()).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LatLng getLocationNew(String Ip){

        InetAddress ipAddress;
//        Location location = null;
        double longitude = -3000.0;
        double latitude = -3000.0;
        try {
            ipAddress = InetAddress.getByName(Ip);
            JsonNode response = reader.get(ipAddress);
//            System.out.println(response);
//            System.out.println(response);
//            CityResponse response = reader.city(ipAddress);
            longitude = response.at("/location/longitude").asDouble();
            latitude = response.at("/location/latitude").asDouble();
//            location = response.getLocation();

//            City city = response.getCity();
//            Log.d("MMDB ->", response.toString());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(longitude == -3000.0){
            return null;
        }
        return new LatLng(latitude, longitude);

    }

    public LatLng getLocation(String Ip) {
        InetAddress ipAddress;
        Location location = null;
        try {
            ipAddress = InetAddress.getByName(Ip);
            JsonNode response = reader.get(ipAddress);
//            System.out.println(response);
//            CityResponse response = reader.city(ipAddress);

//            location = response.getLocation();
//            City city = response.getCity();
//            Log.d("MMDB ->", city.getName());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(location == null){
            return null;
        }
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
