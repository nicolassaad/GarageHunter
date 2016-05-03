package com.example.nicolassaad.garagehunter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LinearLayout searchLayout;
    private Button hideSearchButton;
    private Button searchButton;
    private int buttonCounter;
    private static final String TAG = "MapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        searchButton = (Button) findViewById(R.id.hide_search_button);
//        searchLayout = (LinearLayout) findViewById(R.id.search_layout);
//        hideSearchButton = (Button) findViewById(R.id.search_button);
//        hideSearchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchLayout.setVisibility(View.GONE);
//                hideSearchButton.setText("TESTING");
//                buttonCounter = 0;
//                if (buttonCounter % 2 == 0) {
//                    searchLayout.setVisibility(View.GONE);
//                    hideSearchButton.setText("Show Search");
//                    buttonCounter++;
//                    Log.d(TAG, buttonCounter + "");
//                } else if (buttonCounter % 2 != 0){
//                    searchLayout.setVisibility(View.VISIBLE);
//                    hideSearchButton.setText("Hide Search");
//                    buttonCounter++;
//                    Log.d(TAG, buttonCounter + "");
//                }
//            }
//        });

//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchLayout.setVisibility(View.GONE);
//                searchButton.setText(buttonCounter);
//                buttonCounter++;
//            }
//        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}