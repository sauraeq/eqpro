package com.equalinfotechuser.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.equalinfotechuser.AppSharedPreference;
import com.equalinfotechuser.R;
import com.equalinfotechuser.URL_SUPPORT;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {
    EditText username,password;
    Button login;
    AppSharedPreference sharedpreferences;
    private Boolean GPS_PROVIDER = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedpreferences = AppSharedPreference.getsharedprefInstance(getApplicationContext());
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);

        login=findViewById(R.id.login);



        permissionManage();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(username.getText().toString(),password.getText().toString());
            }
        });

    }


    private void permissionManage() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GPS_PROVIDER = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!GPS_PROVIDER) {
            Toast.makeText(this, "\nPlease enable High Accuracy Location Setting Permission!\n", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } else if (
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }

    public void Login(String email,String id) {
        ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();



        String url = URL_SUPPORT.Baseurl+"userlogin";


        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("employee_id",email);
            jsonObject.accumulate("password",id);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        Log.e("jsonpostdata",""+jsonObject);
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {



            try {
                JSONObject json= (JSONObject) new JSONTokener(response.toString()).nextValue();
                JSONObject json2 = json.getJSONObject("data");
                String user_id = (String) json2.get("user_id");
                String email_id = (String) json2.get("email");
                String employee_code = (String) json2.get("employee_code");
                sharedpreferences.setuser_id(user_id);
                sharedpreferences.setEmailId(email_id);
                sharedpreferences.setEmployee_code(employee_code);
                Intent intent=new Intent(LoginActivity.this,HomeActivityStudent.class);
                startActivity(intent);
            } catch (JSONException e) {

                try {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Alert")
                            .setContentText(response.getString("msg"))
                            .show();
                } catch (JSONException s) {
                    s.printStackTrace();
                }

                e.printStackTrace();
            }


            pDialog.dismiss();


        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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
                1000*5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);


    }

}
