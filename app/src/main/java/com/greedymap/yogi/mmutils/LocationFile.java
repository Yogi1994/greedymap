package com.greedymap.yogi.mmutils;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.greedymap.yogi.helper.IPAndTIme;
import com.greedymap.yogi.helper.IPwithTimeAndLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yogi on 3/5/17.
 */

public class LocationFile {
    List<IPAndTIme> ipTimeList = new ArrayList<>();
    ArrayList<LatLng> list = null;

//    BufferedReader[] readerArr = new BufferedReader[60];

    private void readFile(Context context, int start , int end) {
//        System.out.print("ok started");
        Log.d("Location file", "started");
        BufferedReader reader = null;

        IPAndTIme iPandTime = new IPAndTIme();
        MMDB mmdbObj = new MMDB(context);
        Date startDate = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("test_ip_ts.txt")));

            // do reading, usually loop until end of file reading
            String mLine;
            int counterstart = 0;
            String[] ipTimeStringArr;
            String ip ;
            Date prevDate = null;
            int lines = 0 ;
            while ((mLine = reader.readLine()) != null) {

                //process line
                ipTimeStringArr = mLine.split(",");

                DateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:sssssssss");
                Date currDate = sdf.parse(ipTimeStringArr[1]);
                if(counterstart == 0) {
                    startDate = currDate;
                }
                if(prevDate == null || prevDate.compareTo(currDate) < 0) {
//                    Log.d("Location File", " -->" + counterstart);
                    counterstart++;
                }
                if(counterstart>=start && counterstart< end){
                    lines++;
                    ip = ipTimeStringArr[0];


                    list.add(mmdbObj.getLocation(ip));
                    prevDate = currDate;

                }
            }
            Log.d("Location file", "end" + lines);
        } catch (IOException e) {
            //log the exception
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }

    public ArrayList<LatLng> getLongiTudeLatitude(Context context, int start , int end) {
        list = new ArrayList<>();
        readFile(context, start, end);
        return list;
    }
}
