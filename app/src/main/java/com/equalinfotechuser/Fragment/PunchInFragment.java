package com.equalinfotechuser.Fragment;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.equalinfotechuser.Activity.GPSTracker;
import com.equalinfotechuser.Activity.LocationTrack;
import com.equalinfotechuser.AppSharedPreference;
import com.equalinfotechuser.R;
import com.equalinfotechuser.URL_SUPPORT;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.BuildConfig;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class PunchInFragment extends Fragment {
    double current_latitude = 0, current_longitude = 0, current_accuracy = 0;
    ImageView home, history, offer, cart;
    TextView location;
    private Boolean GPS_PROVIDER = false;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private long UPDATE_INTERVAL = 2 * 1000;
    private long FASTEST_INTERVAL = 2000;
    TextView name, email_id, address;
    AppSharedPreference sharedpreferences;
    TextView time, year, day, day_date, punchin, punchout;
    LinearLayout punch_out, punch_in;
    private String provider;
    private static final View TODO = null;
    double lat;
    double lng;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude, status;

    SharedPreferences shared;
    SharedPreferences.Editor ed;


    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = ACCESS_FINE_LOCATION;

    // GPSTracker class
    GPSTracker gps;

    RelativeLayout top;

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;


    // private static final String TAG = MainActivity.class.getSimpleName();


    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    ProgressBar progressBar;


    public PunchInFragment() {
        // Required empty public constructor
    }


    public static PunchInFragment newInstance(String param1, String param2) {
        PunchInFragment fragment = new PunchInFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment
        View view = inflater.inflate(R.layout.fragment_punch_in, container, false);
        sharedpreferences = AppSharedPreference.getsharedprefInstance(getActivity());
        address = view.findViewById(R.id.address);


        time = view.findViewById(R.id.time);
        year = view.findViewById(R.id.year);
        day = view.findViewById(R.id.day);

        punchout = view.findViewById(R.id.punchout);
        punchin = view.findViewById(R.id.punchin);

        punch_out = view.findViewById(R.id.punch_out);
        punch_in = view.findViewById(R.id.punch_in);
        shared = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        ed = shared.edit();


        day_date = view.findViewById(R.id.day_date);
        top = view.findViewById(R.id.top);
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:MM");
        time.setText(formatter.format(today));
        blink();
        // getLocation();

        progressBar = view.findViewById(R.id.progress_bar);


        if (shared.getString("status", "").equals("true")) {
            punchin.setTextColor(Color.RED);
        } else {
            punchin.setTextColor(Color.BLACK);
        }


        punch_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (address.getText().toString().equals("")) {

                } else {
                    gps = new GPSTracker(getActivity());
                    if (gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        // \n is for new line
                        //  Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                        //  Toast.makeText(getActivity(), "Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude, Toast.LENGTH_SHORT).show();


                        //   double latitude = location.getLatitude();
                        // double longitude = location.getLongitude();
                        Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());

                        //   Toast.makeText(getActivity(),String.valueOf(latitude)+" "+String.valueOf(longitude),Toast.LENGTH_SHORT).show();

                        lat = gps.getLatitude();
                        lng = gps.getLongitude();
                        List<Address> addresses = null;
                        try {
                            addresses = gc.getFromLocation(latitude, longitude, 1);
                            StringBuilder sb = new StringBuilder();
                            if (addresses.size() > 0) {
                                Address addres = addresses.get(0);
                                for (int i = 0; i < addres.getMaxAddressLineIndex(); i++)
                                    sb.append(addres.getAddressLine(i)).append("\n");
                                sb.append(addres.getLocality()).append("\n");
                                sb.append(addres.getPostalCode()).append("\n");
                                sb.append(addres.getCountryName());
                                address.setText(addresses.get(0).getAddressLine(0));
                                // Toast.makeText(getActivity(),addresses.get(0).getAddressLine(0),Toast.LENGTH_SHORT).show();


                            }

                            Location loc1 = new Location("");
                            loc1.setLatitude(lat);
                            loc1.setLongitude(lng);

                            Location loc2 = new Location("");
                            loc2.setLatitude(28.6245188);
                            loc2.setLongitude(77.3794972);


                            float distanceInMeters = loc1.distanceTo(loc2);

                            //  Toast.makeText(getActivity(),String.valueOf(distanceInMeters),Toast.LENGTH_SHORT).show();

                            if (distanceInMeters <= 50) {

                                Punchin();
                                // Toast.makeText(getActivity(),"You  are in Office range",Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getActivity(), "You  are not in Office range", Toast.LENGTH_SHORT).show();
                                //  Punchin();
                            }

                        } catch (IOException e) {

                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }

                    } else {
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }


                }

            }
        });


        punch_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (address.getText().toString().equals("")) {

                } else {


                    gps = new GPSTracker(getActivity());
                    if (gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        // \n is for new line
                        //  Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                        //  Toast.makeText(getActivity(), "Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude, Toast.LENGTH_SHORT).show();


                        //   double latitude = location.getLatitude();
                        // double longitude = location.getLongitude();
                        Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());

                        //   Toast.makeText(getActivity(),String.valueOf(latitude)+" "+String.valueOf(longitude),Toast.LENGTH_SHORT).show();

                        lat = gps.getLatitude();
                        lng = gps.getLongitude();
                        List<Address> addresses = null;
                        try {
                            addresses = gc.getFromLocation(latitude, longitude, 1);
                            StringBuilder sb = new StringBuilder();
                            if (addresses.size() > 0) {
                                Address addres = addresses.get(0);
                                for (int i = 0; i < addres.getMaxAddressLineIndex(); i++)
                                    sb.append(addres.getAddressLine(i)).append("\n");
                                sb.append(addres.getLocality()).append("\n");
                                sb.append(addres.getPostalCode()).append("\n");
                                sb.append(addres.getCountryName());
                                address.setText(addresses.get(0).getAddressLine(0));
                                // Toast.makeText(getActivity(),addresses.get(0).getAddressLine(0),Toast.LENGTH_SHORT).show();


                            }

                            Location loc1 = new Location("");
                            loc1.setLatitude(lat);
                            loc1.setLongitude(lng);

                            Location loc2 = new Location("");
                            loc2.setLatitude(28.6245188);
                            loc2.setLongitude(77.3794972);


                            float distanceInMeters = loc1.distanceTo(loc2);

                            //  Toast.makeText(getActivity(),String.valueOf(distanceInMeters),Toast.LENGTH_SHORT).show();

                            if (distanceInMeters <= 50) {
                                Punchout();
                                // Toast.makeText(getActivity(),"You  are in Office range",Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(getActivity(), "You  are not in Office range", Toast.LENGTH_SHORT).show();


                                // Punchout();
                            }

                        } catch (IOException e) {

                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }

                    } else {
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }


                }

            }
        });


        gps = new GPSTracker(getActivity());

        // check if GPS enabled
       /* if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
          //  Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

          //  Toast.makeText(getActivity(), "Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude, Toast.LENGTH_SHORT).show();


            //   double latitude = location.getLatitude();
            // double longitude = location.getLongitude();
            Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());

         //   Toast.makeText(getActivity(),String.valueOf(latitude)+" "+String.valueOf(longitude),Toast.LENGTH_SHORT).show();

            lat=gps.getLatitude();
            lng=gps.getLongitude();
            List<Address> addresses = null;
            try {
                addresses = gc.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {
                    Address addres = addresses.get(0);
                    for (int i = 0; i < addres.getMaxAddressLineIndex(); i++)
                        sb.append(addres.getAddressLine(i)).append("\n");
                    sb.append(addres.getLocality()).append("\n");
                    sb.append(addres.getPostalCode()).append("\n");
                    sb.append(addres.getCountryName());
                    address.setText(addresses.get(0).getAddressLine(0));
                   // Toast.makeText(getActivity(),addresses.get(0).getAddressLine(0),Toast.LENGTH_SHORT).show();


                }

                ViewPunchin();
            } catch (IOException e) {

                Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
*/

        permissionManage();

        //  locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //`getLocation();


        /*locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return TODO;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
        }*/


        //  Button btn = (Button) view.findViewById(R.id.btn);


        init();

        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState);

        startLocationButtonClick();
        return view;
    }


    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = java.text.DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }


    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {


            // Toast.makeText(getActivity(),"Lat: " + mCurrentLocation.getLatitude() + ", " + "Lng: " + mCurrentLocation.getLongitude(),Toast.LENGTH_LONG).show();


            double latitude = mCurrentLocation.getLatitude();
            double longitude = mCurrentLocation.getLongitude();


            Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());

            //   Toast.makeText(getActivity(),String.valueOf(latitude)+" "+String.valueOf(longitude),Toast.LENGTH_SHORT).show();

            lat = gps.getLatitude();
            lng = gps.getLongitude();
            List<Address> addresses = null;
            try {
                addresses = gc.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {
                    Address addres = addresses.get(0);
                    for (int i = 0; i < addres.getMaxAddressLineIndex(); i++)
                        sb.append(addres.getAddressLine(i)).append("\n");
                    sb.append(addres.getLocality()).append("\n");
                    sb.append(addres.getPostalCode()).append("\n");
                    sb.append(addres.getCountryName());
                    address.setText(addresses.get(0).getAddressLine(0));
                    // Toast.makeText(getActivity(),addresses.get(0).getAddressLine(0),Toast.LENGTH_SHORT).show();


                }

                ViewPunchin();
            } catch (IOException e) {

                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }

        } else {

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

    }


    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        // Toast.makeText(getActivity(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                // Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    // @OnClick(R.id.btn_start_location_updates)
    public void startLocationButtonClick() {

        progressBar.setVisibility(View.VISIBLE);
        startLocationUpdates();
        // Requesting ACCESS_FINE_LOCATION using Dexter library
    }

    //  @OnClick(R.id.btn_stop_location_updates)
    public void stopLocationButtonClick() {
        mRequestingLocationUpdates = false;
        stopLocationUpdates();
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(getActivity(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                        //toggleButtons();
                    }
                });
    }

    // @OnClick(R.id.btn_get_last_location)
    public void showLastKnownLocation() {
        if (mCurrentLocation != null) {
            Toast.makeText(getActivity(), "Lat: " + mCurrentLocation.getLatitude()
                    + ", Lng: " + mCurrentLocation.getLongitude(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Last known location is not available!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }


    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
/*
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }
*/

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            locationTrack.stopListener();

        } catch (Exception e) {

        }
    }


    private void permissionManage() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        GPS_PROVIDER = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!GPS_PROVIDER) {
            Toast.makeText(getActivity(), "\nPlease enable High Accuracy Location Setting Permission!\n", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } else if (
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            ACCESS_FINE_LOCATION,
                            ACCESS_COARSE_LOCATION},
                    1);
        }
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(), ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                Toast.makeText(getActivity(), "Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude, Toast.LENGTH_SHORT).show();


                //   double latitude = location.getLatitude();
                // double longitude = location.getLongitude();
                Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());

                Toast.makeText(getActivity(), String.valueOf(latitude) + " " + String.valueOf(longitude), Toast.LENGTH_SHORT).show();

                List<Address> addresses = null;
                try {
                    addresses = gc.getFromLocation(lat, longi, 1);
                    StringBuilder sb = new StringBuilder();
                    if (addresses.size() > 0) {
                        Address addres = addresses.get(0);
                        for (int i = 0; i < addres.getMaxAddressLineIndex(); i++)
                            sb.append(addres.getAddressLine(i)).append("\n");
                        sb.append(addres.getLocality()).append("\n");
                        sb.append(addres.getPostalCode()).append("\n");
                        sb.append(addres.getCountryName());
                        address.setText(addresses.get(0).getAddressLine(0));
                        Toast.makeText(getActivity(), addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();


                    }
                } catch (IOException e) {

                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }
                ViewPunchin();

                //  onLocationChanged(location);
                // showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                Toast.makeText(getActivity(), "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

/*
    @Override
    public void onLocationChanged(Location location) {
        lat = (int) (location.getLatitude());
        lng = (int) (location.getLongitude());
        //LatLng latLng = new LatLng(lat, lng);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());

        Toast.makeText(getActivity(),String.valueOf(latitude)+" "+String.valueOf(longitude),Toast.LENGTH_SHORT).show();

        List<Address> addresses = null;
        try {
            addresses = gc.getFromLocation(latitude, longitude, 1);
            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address addres = addresses.get(0);
                for (int i = 0; i < addres.getMaxAddressLineIndex(); i++)
                    sb.append(addres.getAddressLine(i)).append("\n");
                sb.append(addres.getLocality()).append("\n");
                sb.append(addres.getPostalCode()).append("\n");
                sb.append(addres.getCountryName());
                address.setText(addresses.get(0).getAddressLine(0));
                Toast.makeText(getActivity(),addresses.get(0).getAddressLine(0),Toast.LENGTH_SHORT).show();


            }
        } catch (IOException e) {

            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

            ViewPunchin();


//        latituteField.setText(String.valueOf(lat));
            //       longitudeField.setText(String.valueOf(lng));
        }
*/


    private void blink() {
        final Handler hander = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(550);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hander.post(new Runnable() {
                    @Override
                    public void run() {

                        String currentDateTimeString = java.text.DateFormat.getTimeInstance().format(new Date());
                        String years = java.text.DateFormat.getDateInstance().format(new Date());
                        Date currentTime = Calendar.getInstance().getTime();

                       /* String currentString = currentDateTimeString;
                        String[] separated = currentString.split(":");
                       String one= separated[0];
                       String two= separated[1]; */

// textView is the TextView view that should display it
                        time.setText(currentDateTimeString);
                        year.setText(years);

                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                        Date d = new Date();
                        String dayOfTheWeek = sdf.format(d);
                        day.setText(dayOfTheWeek);
                        day_date.setText(dayOfTheWeek + ", " + years);
                        /*if(time.getVisibility() == View.VISIBLE) {
                            time.setVisibility(View.INVISIBLE);
                        } else {
                            time.setVisibility(View.VISIBLE);
                        }*/
                        blink();
                    }
                });
            }
        }).start();
    }

    public void Punchout() {

        ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();


        String url = URL_SUPPORT.Baseurl + "markOutAttendence";


        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("user_id", sharedpreferences.getuser_id());
            jsonObject.accumulate("location", address.getText().toString());
            jsonObject.accumulate("lat", lat);
            jsonObject.accumulate("long", lng);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("jsonpostdata", "" + jsonObject);
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {


            pDialog.dismiss();
            punch_in.setVisibility(View.VISIBLE);
            punch_out.setVisibility(View.GONE);
            sharedpreferences.setpunchstatus("punchout");
            try {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Alert")
                        .setContentText(response.getString("msg"))
                        .show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ViewPunchin();


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-type", "application/json");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);


    }


    public void Punchin() {

        ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();


        String url = URL_SUPPORT.Baseurl + "markAttendence";


        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("user_id", sharedpreferences.getuser_id());
            jsonObject.accumulate("location", address.getText().toString());
            jsonObject.accumulate("lat", lat);
            jsonObject.accumulate("long", lng);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("jsonpostdata", "" + jsonObject);
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {


            Calendar firstLimit = Calendar.getInstance();
            firstLimit.set(Calendar.HOUR, 9);
            firstLimit.set(Calendar.MINUTE, 45);
            firstLimit.set(Calendar.SECOND, 30);

            Calendar secondLimit = Calendar.getInstance();
            secondLimit.set(Calendar.HOUR, 10);
            secondLimit.set(Calendar.MINUTE, 15);
            secondLimit.set(Calendar.SECOND, 30);

            Calendar finalTime = Calendar.getInstance();


            if (finalTime.after(firstLimit) && finalTime.before(secondLimit)) {
                punchin.setTextColor(Color.BLACK);

            } else {
                status = "true";
                punchin.setTextColor(Color.RED);
                ed.putString("status", status);
                ed.commit();
            }


            pDialog.dismiss();
            punch_in.setVisibility(View.GONE);
            punch_out.setVisibility(View.VISIBLE);
            sharedpreferences.setpunchstatus("punchin");
            ViewPunchin();

            try {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Alert")
                        .setContentText(response.getString("msg"))
                        .show();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-type", "application/json");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);


    }


    public void ViewPunchin() {

        ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();


        String url = URL_SUPPORT.Baseurl + "viewAttendence";

        Date d = new Date();
        CharSequence s = DateFormat.format("yyyy-MM-dd", d.getTime());
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("user_id", sharedpreferences.getuser_id());
            jsonObject.accumulate("date", String.valueOf(s));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("jsonpostdata", "" + jsonObject);
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {

            try {
                JSONObject json = (JSONObject) new JSONTokener(response.toString()).nextValue();
                JSONObject json2 = json.getJSONObject("data");
                String in_date_time = (String) json2.get("in_date_time");
                String out_date_time = (String) json2.get("out_date_time");
                String in_location = (String) json2.get("in_location");
                String out_location = (String) json2.get("out_location");

                String punch_out_status = (String) json2.get("punch_out_status");
                String punch_in_status = (String) json2.get("punch_in_status");


                punchin.setText(in_date_time + " at " + in_location + " (In)");
                punchout.setText(out_date_time + " at " + out_location + " (Out)");


                Double lat1 = Double.valueOf(lat);
                Double long1 = Double.valueOf(lng);


                //  distance(lat1,long1);

                Location loc1 = new Location("");
                loc1.setLatitude(lat1);
                loc1.setLongitude(long1);

                Location loc2 = new Location("");
                loc2.setLatitude(28.6245188);
                loc2.setLongitude(77.3794972);

                // loc2.setLatitude(27.1879838);
                //loc2.setLongitude(78.0092309);

                float distanceInMeters = loc1.distanceTo(loc2);

                //  Toast.makeText(getActivity(),String.valueOf(distanceInMeters),Toast.LENGTH_SHORT).show();

                String status = punch_in_status;
                String statusout = punch_out_status;
                if (status.equals("1")) {

                    if (distanceInMeters <= 50) {
                        // punch_in.setVisibility(View.GONE);

                        if (in_date_time.equals("")) {
                            punch_in.setVisibility(View.VISIBLE);
                            punch_out.setVisibility(View.GONE);
                            // return;
                        } else if (out_date_time.equals("")) {
                            punch_out.setVisibility(View.VISIBLE);
                            punch_in.setVisibility(View.GONE);
                            // return;
                        } else {
                            punch_in.setVisibility(View.GONE);
                            punch_out.setVisibility(View.GONE);
                            // return;
                        }
                    } else {

                    }


                } else {
                    if (distanceInMeters <= 50) {
                        punch_in.setVisibility(View.VISIBLE);


                    } else {
                        punch_in.setVisibility(View.GONE);

                    }
                }

                if (statusout.equals("1")) {
                    if (distanceInMeters <= 50) {

                        if (in_date_time.equals("")) {
                            punch_in.setVisibility(View.VISIBLE);
                            punch_out.setVisibility(View.GONE);
                            // return;
                        } else if (out_date_time.equals("")) {
                            punch_out.setVisibility(View.VISIBLE);
                            punch_in.setVisibility(View.GONE);
                            // return;
                        } else {
                            punch_in.setVisibility(View.GONE);
                            punch_out.setVisibility(View.GONE);
                            // return;
                        }
                    } else {

                    }


                } else {

                    if (distanceInMeters <= 50) {
                        punch_out.setVisibility(View.VISIBLE);
                    } else {
                        punch_out.setVisibility(View.GONE);

                    }

                }

            } catch (JSONException e) {

                Double lat1 = Double.valueOf(lat);
                Double long1 = Double.valueOf(lng);
                //  distance(lat1,long1);

                Location loc1 = new Location("");
                loc1.setLatitude(lat1);
                loc1.setLongitude(long1);

                Location loc2 = new Location("");
                loc2.setLatitude(28.6245188);
                loc2.setLongitude(77.3794972);

                //loc2.setLatitude(27.1879838);
                //loc2.setLongitude(78.0092309);

                float distanceInMeters = loc1.distanceTo(loc2);

                // Toast.makeText(getActivity(),String.valueOf(distanceInMeters),Toast.LENGTH_SHORT).show();


                if (distanceInMeters <= 50) {
                    punch_in.setVisibility(View.VISIBLE);
                    punch_out.setVisibility(View.GONE);
                } else {
                    punch_in.setVisibility(View.GONE);
                    punch_out.setVisibility(View.GONE);

                }
               /* String status=sharedpreferences.getpunchstatus();
                if (status.equals("punchout")){
                    if (distanceInMeters<=10){
                        punch_in.setVisibility(View.VISIBLE);
                    }else {
                        punch_in.setVisibility(View.GONE);

                    }
                }else {
                    if (distanceInMeters<=10){
                        punch_out.setVisibility(View.VISIBLE);
                    }else {
                        punch_out.setVisibility(View.GONE);

                    }
                }*/

                e.printStackTrace();
            }


            pDialog.dismiss();
            top.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            stopLocationUpdates();


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-type", "application/json");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);


    }

    private class GeocoderHandler extends Handler {

        @Override
        public void handleMessage(Message message) {
            String addressVal = "";
            String localityVal = "";
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    localityVal = bundle.getString("subLocality") + " " + bundle.getString("locality");
                    addressVal = bundle.getString("address");
                    sharedpreferences.setaddress(addressVal);
                    break;

            }
            //location.setText(localityVal);
            // address.setText(addressVal);
        }
    }


    public boolean isMockLocationEnabled(Location location) {
        boolean isMock = false;
        if (android.os.Build.VERSION.SDK_INT >= 18) {
            isMock = location.isFromMockProvider();
        } else {
            isMock = !Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
        }
        if (isMock) {
            Toast.makeText(getActivity(), "\nSorry you have used mock/fake location, Please disable fake/mock location after punched!\n", Toast.LENGTH_SHORT).show();
        } else {

        }
        return isMock;
    }




    /* Remove the locationlistener updates when Activity is paused */


}
