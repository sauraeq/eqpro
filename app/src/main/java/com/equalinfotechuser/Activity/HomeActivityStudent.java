package com.equalinfotechuser.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.equalinfotechuser.AppSharedPreference;
import com.equalinfotechuser.Fragment.HomeFragment;
import com.equalinfotechuser.Fragment.PunchInFragment;
import com.equalinfotechuser.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

public class HomeActivityStudent extends AppCompatActivity {
    ImageView punch_in_image,home,offer,cart,gallery,workshop,student_back;
    private Boolean GPS_PROVIDER = false;
    private long UPDATE_INTERVAL = 2 * 1000;
    private long FASTEST_INTERVAL = 2000;
    TextView text_punchin,text_home,text_setting,text_workshop;
    LinearLayout linear_gallery,linear_home,linear_workshop,linear_logout;
    RelativeLayout relative_home;
    LinearLayout linear_punchin,daily_challenge,free_write,class_gallery,public_gallery;
    AppSharedPreference sharedpreferences;
    CardView card;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);
        sharedpreferences = AppSharedPreference.getsharedprefInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

       /* if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        }*/

        text_home=findViewById(R.id.text_home);
        punch_in_image=findViewById(R.id.punch_in_image);
        text_punchin=findViewById(R.id.text_punchin);
        home=findViewById(R.id.home);
        //  card=findViewById(R.id.card);
        linear_logout=findViewById(R.id.linear_logout);

        linear_punchin=findViewById(R.id.linear_punchin);

        linear_home=findViewById(R.id.linear_home);





        permissionManage();




        /*HomeFragment mFragment = null;
        mFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, mFragment).commit();*/
        Fragment someFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //  transaction.replace(R.id.frameLayout, someFragment ); // give your fragment container id in first parameter
        transaction.replace(R.id.frameLayout,someFragment);
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();


        linear_punchin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // card.setVisibility(View.GONE);
                text_punchin.setTextColor(Color.parseColor("#059fc5"));
                text_home.setTextColor(Color.parseColor("#000000"));
                home.setImageResource(R.drawable.ic_home_black_24dp);
                punch_in_image.setImageResource(R.drawable.ic_perm_contact_calendar_black_24dp_red);


                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                    new AlertDialog.Builder(HomeActivityStudent.this)
                            .setTitle(R.string.gps_not_found_title)  // GPS not found
                            .setMessage(R.string.gps_not_found_message) // Want to enable?
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                            .setNegativeButton(R.string.no, null)
                            .show();
                }else {
                    PunchInFragment mFragment = null;
                    mFragment = new PunchInFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, mFragment).commit();
                }


            }
        });

        linear_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   card.setVisibility(View.VISIBLE);
                text_home.setTextColor(Color.parseColor("#059fc5"));
                text_punchin.setTextColor(Color.parseColor("#000000"));
                home.setImageResource(R.drawable.ic_home_black_24dp_red);
                punch_in_image.setImageResource(R.drawable.ic_perm_contact_calendar_black_24dp);
                HomeFragment mFragment = null;
                mFragment = new HomeFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, mFragment).commit();

            }
        });



        linear_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HomeActivityStudent.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                        .setMessage("Are you sure you want to Logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences settings =getSharedPreferences("sharedpref", Context.MODE_PRIVATE);
                                settings.edit().clear().commit();
                                Intent intent=new Intent(HomeActivityStudent.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("No", null).show();
            }
        });


        isGooglePlayServicesAvailable();


    }




    private void permissionManage() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GPS_PROVIDER = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!GPS_PROVIDER) {
            Toast.makeText(this, "\nPlease enable High Accuracy Location Setting Permission!\n", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } else if (
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }






    private boolean isGooglePlayServicesAvailable() {
        int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(this, "This device is not supported.", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }












    @Override
    public void onBackPressed() {
    }


    @Override
    protected void onPostResume() {

        //  iniLocation();
        //  isGooglePlayServicesAvailable();
        super.onPostResume();
    }
}
