package com.nothingsoft.nicolassaad.garagehunter.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nothingsoft.nicolassaad.garagehunter.CheckInternetConnection;
import com.nothingsoft.nicolassaad.garagehunter.MainActivity;
import com.nothingsoft.nicolassaad.garagehunter.Models.GarageSale;
import com.nothingsoft.nicolassaad.garagehunter.R;
import com.nothingsoft.nicolassaad.garagehunter.SaleActivity;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by nicolassaad on 5/2/16.
 */
public class MapsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private Button hideSearchButton;
    private Button clearButton;
    private EditText searchLocEdit;
    public static LinearLayout searchLayout;
    private int buttonCounter;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final int NOTIFICATION_NOT_AVAILABLE = 2;
    public final static String SALE_KEY1 = "Title";
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private Marker userMarker;
    private GoogleApiClient mGoogleApiClient;
    private SupportMapFragment fragment;
    private Query query2;
    private Firebase mFireBase;
    private String address;
    private ProgressBar progressBar;
    private RelativeLayout progressLayout;
    private TextView progressText;
    private Button searchButton;

    public MapsFragment() {
    }

    private static final String TAG = "MapsFragment";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_maps, container, false);
        setViews(view);
        clearButton.setVisibility(View.INVISIBLE);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (location == null) {
                    Log.d(TAG, "Location is null");
                } else {
                    if (CheckInternetConnection.isOnline(getContext())) {

                        handleNewLocation(location);
                    } else {
                        Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
                        handleNewLocation(location);
                    }
                }
            }
        });
        hideSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonCounter % 2 == 0) {
                    searchLayout.setVisibility(View.GONE);
                    hideSearchButton.setText(R.string.show_search);
                    buttonCounter++;
                } else {
                    searchLayout.setVisibility(View.VISIBLE);
                    hideSearchButton.setText(R.string.hide_search);
                    buttonCounter++;
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLocEdit.setText("");
                clearButton.setVisibility(View.INVISIBLE);
            }
        });

        settingLocationRequest();
        settingGoogleApiClient();

        mFireBase = new Firebase(getString(R.string.firebase_address));

        searchButton.setOnClickListener(searchListeners);
        showProgressLayout();
        query2 = mFireBase.orderByChild("title").getRef();
        query2.addListenerForSingleValueEvent(valueEventListener);

        if (!CheckInternetConnection.isOnline(getContext())) {
            Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
            showNetworkNotAvailableNotification();
        }

        // Code that controls the visibility for the Clear Button in the searchByLocEdit EditText
        searchLocEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    clearButton.setVisibility(View.VISIBLE);
                } else {
                    clearButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // left blank
            }
        });

        return view;
    }

    private void setViews(View view) {
        searchButton = (Button) view.findViewById(R.id.search_button);
        searchLayout = (LinearLayout) view.findViewById(R.id.search_layout);
        hideSearchButton = (Button) view.findViewById(R.id.hide_search_button);
        searchLocEdit = (EditText) view.findViewById(R.id.search_loc_edit);
        clearButton = (Button) view.findViewById(R.id.clear_button);
        progressLayout = (RelativeLayout) view.findViewById(R.id.progress_bar_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressText = (TextView) view.findViewById(R.id.hunting_sales_text);
        progressBar.setMax(10);

    }

    private void searchForLocation() {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        address = searchLocEdit.getText().toString();
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() == 0) {
                Toast.makeText(getContext(), R.string.enter_valid_address_loc, Toast.LENGTH_LONG).show();
            } else {
                addUserPosition(addresses);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addUserPosition(List<Address> addresses) {
        double lat = 0.0, lng = 0.0;

        Log.d(TAG, addresses.get(0).getLatitude() + "");
        Log.d(TAG, addresses.get(0).getLongitude() + "");
        lat = addresses.get(0).getLatitude();
        lng = addresses.get(0).getLongitude();
        double currentLatitude = lat;
        double currentLongitude = lng;
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        if (mMap != null) {
            hideKeyboard(getActivity());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15.0f).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
            Log.d(TAG, "Map is null");
        }
    }

    /**
     * Notification that informs the user if they have no internet connectivity. It is triggered onCreate
     * onResume and in every button in MapsFragment
     */
    private void showNetworkNotAvailableNotification() {

        Intent intent = new Intent(getContext(), MainActivity.class);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(getContext(), (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext());
        mBuilder.setStyle(bigPictureStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.ihatethisapp)));
        mBuilder.setSmallIcon(R.drawable.icon);
        mBuilder.setContentText(getString(R.string.network_not_available));
        mBuilder.setContentTitle(getString(R.string.please_check_your_network));
        mBuilder.setContentIntent(pIntent);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigPictureStyle);

        NotificationManager mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
// NOTIFICATION_ID allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_NOT_AVAILABLE, mBuilder.build());
    }

    private void settingLocationRequest() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void settingGoogleApiClient() {
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * This searchForLocation hides the softkeyboard automatically after use the search button is clicked
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void hideProgressLayout() {
        progressLayout.animate().translationXBy(progressLayout.getWidth() + 585).setDuration(500);
//        progressLayout.setVisibility(View.GONE);
//        progressBar.setVisibility(View.GONE);
//        progressLayout.setVisibility(View.GONE);
//        progressText.setVisibility(View.GONE);

    }

    private void showProgressLayout() {
        progressLayout.setVisibility(View.VISIBLE);
        progressLayout.animate().translationXBy(-585).alpha(1.0f).setDuration(900);
        progressBar.setVisibility(View.VISIBLE);
        progressText.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
    }

    /**
     * This searchForLocation runs on onResume an creates the map fragment if the map is null
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            Log.d("MainActivity", "Setting up if needed running");
            // Try to obtain the map from the SupportMapFragment.
            FragmentManager fm = getChildFragmentManager();
            fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
            mMap = fragment.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                Log.d(TAG, "Map is not null");
            }
        }
    }

    /**
     * Adds the user marker into the user's location when the map fragment is being created
     *
     * @param location
     */
    private void handleNewLocation(Location location) {

        Log.d(TAG, location.toString());
        Log.d(TAG, "Handling new location");
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        if (mMap != null) {
            if (userMarker != null) {
                userMarker.remove();
            }
            userMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker)));

            Log.d(TAG, currentLatitude + " " + currentLongitude);

            // TODO: 8/25/17 WHEN BACKING OUT FROM A SALEACTIVITY THIS CODE RECENTERS MAP ON USER. WE WANT THE MAP TO STAY STILL SOMEHOW
            // TODO: 8/25/17 MAKING SALEACTIVITY A FRAGMENT COULD POTENTIALLY FIX THIS ISSUE
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(9.5f).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        } else {
            Log.d(TAG, "Map is null");
        }
    }

    /**
     * This searchForLocation is designed for when the user is searching a specific location.
     * While the user is querying a specific place, this searchForLocation runs which
     * adds the user marker but doesn't position the camera on the user's location, allowing
     * the user to search by day and stay on the location they were searching for.
     *
     * @param location
     */
    private void handleNewLocNoCamAnim(Location location) {
        Log.d(TAG, location.toString());
        Log.d(TAG, "Handling new location");

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        if (mMap != null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker)));

            Log.d(TAG, currentLatitude + " " + currentLongitude);

        } else {
            Log.d(TAG, "Map is null");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // ask for the permission
//            requestPermissions();
            Log.d(TAG, "Permissions are gone");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            Log.d(TAG, "Location is null");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
   /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    View.OnClickListener searchListeners = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (CheckInternetConnection.isOnline(getContext())) {
                // do your search
                searchForLocation();
            } else {
                Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
                // TODO: 7/14/16 Include a popup that informs the user in a more apparent way 
                showNetworkNotAvailableNotification();
            }
        }
    };

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
            if (mMap == null) {
                setUpMapIfNeeded();
            }
            mMap.clear();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                Log.d(TAG, "Location is null");
            } else {
                if (searchLocEdit.getText().toString().equals("")) {
                    handleNewLocation(location);
                } else {
                    handleNewLocNoCamAnim(location);
                }
            }
            progressBar.setProgress(0);
            int markerCount = 0;
            while (iterator.hasNext()) {
                GarageSale daySearch = iterator.next().getValue(GarageSale.class);
                Log.d(TAG, daySearch.toString());
                // Creating the different colors of map markers that will be used to populate the map randomly.
                float[] colors = { BitmapDescriptorFactory.HUE_AZURE, BitmapDescriptorFactory.HUE_BLUE, BitmapDescriptorFactory.HUE_GREEN
                        , BitmapDescriptorFactory.HUE_ORANGE, BitmapDescriptorFactory.HUE_RED
                        , BitmapDescriptorFactory.HUE_YELLOW, BitmapDescriptorFactory.HUE_VIOLET
                        , BitmapDescriptorFactory.HUE_ROSE,  BitmapDescriptorFactory.HUE_CYAN};
                // Displays markers for all matching entries
                mMap.addMarker(new MarkerOptions().position(new LatLng(daySearch.getLat(), daySearch.getLon())).title(daySearch.getTitle()).icon(BitmapDescriptorFactory.defaultMarker(colors[new Random().nextInt(colors.length)])));
                markerCount++;
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String toSaleTitle = marker.getTitle();
                        Intent toSaleIntent = new Intent(getContext(), SaleActivity.class);
                        toSaleIntent.putExtra(SALE_KEY1, toSaleTitle);
                        startActivity(toSaleIntent);
                    }
                });
            }
            hideProgressLayout();
            int resultsLoaded = markerCount;
            if (resultsLoaded == 1) {
                Snackbar.make(getView(), resultsLoaded + getString(R.string.sale_found_worldwide), Snackbar.LENGTH_LONG).show();

            } else if (resultsLoaded == 0 ) {
                Snackbar.make(getView(),  getString(R.string.no_sales_found), Snackbar.LENGTH_LONG).show();

            } else {
                Snackbar.make(getView(), resultsLoaded + getString(R.string.sales_found_worldwide), Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    };

    public class RelativeView extends RelativeLayout {

        public RelativeView(Context context) {
            super(context);
        }

        @Override
        public void onAnimationEnd() {
            super.onAnimationEnd();
            progressLayout.setVisibility(GONE);
        }
    }
}