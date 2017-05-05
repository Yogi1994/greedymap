package com.greedymap.yogi.greedymap;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.greedymap.yogi.mmutils.LocationFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class HeatMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay = null;
    SeekBar seekBar1;
    private LocationFile locationFile = null;

    List<LatLng> list = null;

    Timer timer = null;

    int minutesForPlay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heat_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationFile = new LocationFile(getApplicationContext());
        final int[] progressr = {0};
        timer = new Timer();
        seekBar1 = (SeekBar) findViewById(R.id.seekBar);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            TextView t = (TextView) findViewById(R.id.minutes);

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
//
                if(minutesForPlay != 0){
                    Toast.makeText(getApplicationContext(), "Please wait while we finish the other task.",Toast.LENGTH_LONG).show();
                    return ;
                }
                int start = 0;
                int end = 1;
                if(progressr[0] != 0) {
                    start = progressr[0]-1;
                    end = progressr[0];
                }
                t.setText(start + "-" + end + " minute range");
//                addHeatMap(start,progressr[0]);
                new Task1().execute(start,end);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                progressr[0] = progress;
                int start = progressr[0] -1;
                if(start < 0) {
                    start = 0;
                }
                if (progress < 1) {
                    seekBar.setProgress(1);
                }
                t.setText(start + "-" + progressr[0] + " minute range");
            }
        });

        Button playBtn = (Button) findViewById(R.id.btnPlay);
        playBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(minutesForPlay > 0){
                    Toast.makeText(getApplicationContext(), "Please wait while we finish the other task.",Toast.LENGTH_LONG).show();
                    return ;
                }
                minutesForPlay = 1;
                seekBar1.setProgress(1);
                new Task1().execute(0,1);
            }
        });

        Button stopBtn = (Button) findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                minutesForPlay = 0;
                seekBar1.setProgress(1);
            }
        });


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        addHeatMap(0,1);
        new Task1().execute(0,1);
    }




    private void addHeatMap(int start, int end) {


        // Get the data: latitude/longitude positions of police stations.

        list = readItems( start,  end);



    }

    private void addToMap(){
        if(list == null || list.size() == 0 ) {
            Toast.makeText(getApplicationContext(), "Done: No relevant data",Toast.LENGTH_LONG).show();
        }
        if(mOverlay != null){
            mOverlay.remove();
        }

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private ArrayList<LatLng> readItems(int start, int end)  {

        ArrayList<LatLng> list = locationFile.getLongiTudeLatitude(start,  end);

        return list;
    }

    private void loopTask() {
        int start = minutesForPlay -1;
        int end = minutesForPlay;
        if(start< 0){
            start=0;
            end=1;
        }
        new Task1().execute(start,end);
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
            Log.d("started it -> ", "->>" + arg0[0] +" " +arg0[1]);
            addHeatMap(arg0[0],arg0[1]);


            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            Log.d("started it -> ", "->>finfish");
//            Toast.makeText(getApplicationContext(), "Done",Toast.LENGTH_LONG).show();
            addToMap();

            if(minutesForPlay < 60 && minutesForPlay != 0) {
                minutesForPlay++;
                seekBar1.setProgress(minutesForPlay);
                new Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                // your code here
                                loopTask();

                            }
                        },
                        3000
                );
            }
        }
    }
}



