package com.greedymap.yogi.mmutils;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.android.gms.maps.model.LatLng;
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


    public static DatabaseReader reader = null;

    public MMDB(Context context){

        InputStream inputStream = context.getResources().openRawResource(
                context.getResources().getIdentifier("geolite2city",
                        "raw", context.getPackageName()));
        File database = new File(context.getCacheDir(), "geolite2city.mmdb");

        try {

            OutputStream output = new FileOutputStream(database);
            try {
                try {
                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
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
            reader = new DatabaseReader.Builder(database).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LatLng getLocation(String Ip) {
        InetAddress ipAddress;
        Location location = null;
        try {
            ipAddress = InetAddress.getByName(Ip);
            CityResponse response = reader.city(ipAddress);
            location = response.getLocation();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (GeoIp2Exception e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
