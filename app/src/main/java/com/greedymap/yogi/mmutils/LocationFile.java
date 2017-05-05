package com.greedymap.yogi.mmutils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

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

    ArrayList<LatLng> list = null;
    static MMDB mmdbObj = null;
    static BufferedReader reader = null;
    static BufferedReader[] readerArray = null;
    static Context context;
    static long[] ipAndTImeList = null;
    static loadingStatus isLoaded[] = null;
    private enum loadingStatus  {NOT_LOADED, LOADING, LOADED};

    public LocationFile(Context context) {
        this.context = context;
        mmdbObj = new MMDB(context);
        ipAndTImeList = new long[100];
        isLoaded = new loadingStatus[100];
        for (int i = 0; i < 100; i++) {
            ipAndTImeList[i] = 0;
            isLoaded[i] = loadingStatus.NOT_LOADED;
        }

        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("test_ip_ts.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Task1 extends AsyncTask<Integer , Void, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Log.d("started it -> ", "->>start");
//            Toast.makeText(getApplicationContext(), "Loading started...",Toast.LENGTH_LONG).show();
        }
        @Override
        protected String doInBackground(Integer... arg0)
        {
            //Record method
            readFile(0,61);
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
//            Log.d("started it -> ", "->>finfish");
//            Toast.makeText(getApplicationContext(), "Done",Toast.LENGTH_LONG).show();
//            addToMap();
//
        }
    }

//    BufferedReader[] readerArr = new BufferedReader[60];
//
//    private void getIPListAlreadyLoaded(int start, int index){
//        Log.d(getClass().getName(), "from bufferred list " + index);
//        ArrayList<IPAndTIme> currentList = ipAndTImeList.get(index);
//        int length = currentList.size();
//        LatLng longlat = null;
//        for(int i = 0 ; i < length; i++){
//
//            longlat = mmdbObj.getLocationNew(currentList.get(i).getIP());
//            if(longlat != null) {
//                list.add(longlat);
////                lines++;
//            }
//        }
////        return list;
//    }



    private void readFile( int start , int end) {

        Log.d("Location file", "started");
        BufferedReader curr_reader = null;
        try {
            curr_reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("test_ip_ts.txt")));
//            curr_reader.skip(41);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(isLoaded[start] == loadingStatus.LOADED ){
//            getIPListFromLoadedReader(start);
            Log.d("Location file", "started init there"+ start + " " + ipAndTImeList[start]);
            try {
                curr_reader.skip(ipAndTImeList[start]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Location File", "skipBytes");
        }else {
//            long skipBytes = 0;
//            for(int i = 0 ; i < start; i++){
//                if(isLoaded[i] == loadingStatus.LOADED){
//                    Log.d("Location File", ""+i);
//                    skipBytes = ipAndTImeList[i];
//                }
//            }
//            Log.d("Location File", ""+skipBytes);
//            try {
//                curr_reader.skip(skipBytes);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }


        try {

            String mLine;
//            int counterstart = 0;
            String[] ipTimeStringArr;
            String ip ;
            Date prevDate = null;
            int lines = 0 ;
            LatLng longlat = null;
            IPAndTIme ipAndTIme_curr = new IPAndTIme();
            DateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:sssssssss");
            Date currDate;
            long curr_length = ipAndTImeList[start] ;
//            ArrayList<IPAndTIme> listIpTime = new ArrayList<>();

            // do reading, usually loop until end of file reading
            while ((mLine = curr_reader.readLine()) != null) {
                curr_length += (mLine.length() +1);
                //process line
                ipTimeStringArr = mLine.split(",");


                currDate = sdf.parse(ipTimeStringArr[1]);
                ipAndTIme_curr.setAccesTime(currDate);
                ipAndTIme_curr.setIP(ipTimeStringArr[0]);


                if(prevDate == null || (currDate.getMinutes() != prevDate.getMinutes())) {

//                    Log.d("Location File", " -->" + counterstart);

                    if(prevDate == null){
                        isLoaded[currDate.getMinutes()] = loadingStatus.LOADING;
//                        listIpTime.add(ipAndTIme_curr);
                    } else {
                        Log.d("Counter", " " +currDate.getMinutes());
                        isLoaded[currDate.getMinutes()-1] = loadingStatus.LOADED;
//                        readerArray[currDate.getMinutes()] = new BufferedReader( curr_reader);
                        ipAndTImeList[currDate.getMinutes()] = curr_length-mLine.length()+1;
                    }
                    prevDate = currDate;
                    isLoaded[currDate.getMinutes()] = loadingStatus.LOADING;
                }
                if(currDate.getMinutes() == start){
//                    listIpTime.add(ipAndTIme_curr);
                    if(lines == 0) {
                        ipAndTImeList[currDate.getMinutes()] = curr_length-mLine.length()+1 ;
//                        readerArray[start] = new BufferedReader(curr_reader);
//                        int long  = curr_reader.
                        Log.d("Location File", " -->" + mLine);
                    }
                    ip = ipTimeStringArr[0];

                    longlat = mmdbObj.getLocationNew(ip);
                    if(longlat != null) {
                        list.add(longlat);
                        lines++;
                    }
                }else if(lines > 1 || currDate.getMinutes() > start){

                    isLoaded[currDate.getMinutes()-1] = loadingStatus.LOADED;
                    break;
                }
//                if(start!= 0)
//                Log.d("kya hua?", ""+currDate.getMinutes());
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
