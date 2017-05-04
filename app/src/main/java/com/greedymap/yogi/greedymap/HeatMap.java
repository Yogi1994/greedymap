package com.greedymap.yogi.greedymap;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class HeatMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    SeekBar seekBar1;
    private LocationFile locationFile = null;

    List<LatLng> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heat_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationFile = new LocationFile(getApplicationContext());
        final int[] progressr = {0};
        seekBar1 = (SeekBar) findViewById(R.id.seekBar);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            TextView t = (TextView) findViewById(R.id.minutes);

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), String.valueOf(progressr[0]),Toast.LENGTH_LONG).show();
                int start = progressr[0] -1;
                if(start < 0) {
                    start = 0;
                }
                t.setText(start + "-" + progressr[0] + " minute range");
//                addHeatMap(start,progressr[0]);
                new Task1().execute(start,progressr[0]);
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
                t.setText(start + "-" + progressr[0] + " minute range");
//                t1.setTextSize(progress);
//                Toast.makeText(getApplicationContext(), String.valueOf(progress),Toast.LENGTH_LONG).show();

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

    class Task1 extends AsyncTask<Integer , Void, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Log.d("started it -> ", "->>start");
            Toast.makeText(getApplicationContext(), "Loading started...",Toast.LENGTH_LONG).show();
        }
        @Override
        protected String doInBackground(Integer... arg0)
        {
            //Record method
            addHeatMap(arg0[0],arg0[1]);

            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            Log.d("started it -> ", "->>finfish");
            Toast.makeText(getApplicationContext(), "Done",Toast.LENGTH_LONG).show();
//            addToMap();
        }
    }
}



