package com.greedymap.yogi.mmutils;

import android.content.Context;
import android.content.res.AssetManager;

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
        InetAddress ipAddress;
        try {
            ipAddress = InetAddress.getByName("49.33.194.3");

            CityResponse response = reader.city(ipAddress);
            Country country = response.getCountry();
            System.out.println(country.getIsoCode());            // 'US'
            System.out.println(country.getName());               // 'United States'
            System.out.println(country.getNames().get("zh-CN")); // '美国'


            City city = response.getCity();
            System.out.println(city.getName()); // 'Minneapolis'

            Postal postal = response.getPostal();
            System.out.println(postal.getCode()); // '55455'

            Location location = response.getLocation();
            System.out.println(location.getLatitude());  // 44.9733
            System.out.println(location.getLongitude()); // -93.2323
//            IspResponse response = reader.isp(ipAddress);

//            System.out.println(response.getAutonomousSystemNumber());       // 217
//            System.out.println(response.getAutonomousSystemOrganization()); // 'University of Minnesota'
//            System.out.println(response.getIsp());                          // 'University of Minnesota'
//            System.out.println(response.getOrganization());                 // 'University of Minnesota'
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (GeoIp2Exception e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e ){
            e.printStackTrace();
        }


    }
}
