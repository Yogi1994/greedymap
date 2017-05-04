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


/**
 * Disaster :o
 *
 *  * 05-05 00:58:52.418 9256-9256/com.greedymap.yogi.greedymap D/started it ->: ->>start
 * 05-05 00:58:52.438 9256-12394/com.greedymap.yogi.greedymap D/Location file: started
 * 05-05 00:59:14.721 9256-12394/com.greedymap.yogi.greedymap D/Location file: end0
 * 05-05 00:59:14.722 9256-9256/com.greedymap.yogi.greedymap D/started it ->: ->>finfish
 */
public class LocationFile {
    List<IPAndTIme> ipTimeList = new ArrayList<>();
    ArrayList<LatLng> list = null;
    static MMDB mmdbObj = null;
    static BufferedReader reader = null;
    static Context context;
    public LocationFile(Context context) {
        this.context = context;
        mmdbObj = new MMDB(context);

    }
//    BufferedReader[] readerArr = new BufferedReader[60];

    private void readFile( int start , int end) {
//        System.out.print("ok started");
        Log.d("Location file", "started");

//        mmdbObj.getLocationNew("49.33.6.143");

        IPAndTIme iPandTime = new IPAndTIme();

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
            LatLng longlat = null;

            while ((mLine = reader.readLine()) != null) {

                //process line
                ipTimeStringArr = mLine.split(",");

                DateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:sssssssss");
                Date currDate = sdf.parse(ipTimeStringArr[1]);
                if(counterstart == 0) {
                    startDate = currDate;
                }
                if(prevDate == null || (currDate.getTime()- prevDate.getTime())>=60000) {
                    prevDate = currDate;
//                    Log.d("Location File", " -->" + counterstart);
                    counterstart++;
                }
                if(counterstart>start && counterstart<= end){

                    ip = ipTimeStringArr[0];
                    longlat = mmdbObj.getLocationNew(ip);
                    if(longlat != null) {
                        list.add(longlat);
                        lines++;
                    }
                }else if(lines > 1 ){
                    break;
                }
            }
            Log.d("Location file", "end" + lines);
        } catch (IOException e) {
            //log the exception
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LatLng> getLongiTudeLatitude( int start , int end) {
        list = new ArrayList<>();
        readFile( start, end);
        return list;
    }
}
