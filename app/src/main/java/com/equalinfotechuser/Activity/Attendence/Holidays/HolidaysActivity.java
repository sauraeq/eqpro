package com.equalinfotechuser.Activity.Attendence.Holidays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.equalinfotechuser.R;
import com.equalinfotechuser.URL_SUPPORT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HolidaysActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holidays);
        getHolidays("90");
    }

    private void getHolidays(String user_id) {
        ProgressDialog pDialog = new ProgressDialog(HolidaysActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();


        String url = URL_SUPPORT.Baseurl + "getHoliday";

        Date d = new Date();

        CharSequence s = DateFormat.format("dd/MMM/yyyy", d.getTime());
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("user_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("jsonpostdata", "" + jsonObject);
        final RequestQueue requestQueue = Volley.newRequestQueue(HolidaysActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {

            try {

                JSONObject jsonRootObject = new JSONObject(response.toString());

                JSONArray jsonArray = jsonRootObject.optJSONArray("data");

                List<HolidaysBean> list = new ArrayList<HolidaysBean>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);

                    HolidaysBean HolidaysBean = new HolidaysBean();
                    HolidaysBean.setdate_month_year(data.getString("date_month_year"));
                    HolidaysBean.setday(data.getString("day"));
                    list.add(HolidaysBean);
                }

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvHolidays);
                //GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3,LinearLayoutManager.VERTICAL,false);
                // recyclerView.setLayoutManager(gridLayoutManager);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(staggeredGridLayoutManager);

                HolidaysAdapter adapter = new HolidaysAdapter(list);
                recyclerView.setAdapter(adapter);


            } catch (JSONException e) {


                e.printStackTrace();
            }


            pDialog.dismiss();


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
/*
    public void getHolidays(String id, String date_month_year, String day) {

        ProgressDialog pDialog = new ProgressDialog(HolidaysActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();


        String url = URL_SUPPORT.Baseurl + "getHoliday";

        Date d = new Date();

        CharSequence s = DateFormat.format("MMMM/dd/yyyy", d.getTime());
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("id", id);
            jsonObject.accumulate("date_month_year", date_month_year);
            jsonObject.accumulate("day", day);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("jsonpostdata", "" + jsonObject);
        final RequestQueue requestQueue = Volley.newRequestQueue(HolidaysActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {

            try {

                JSONObject jsonRootObject = new JSONObject(response.toString());

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = jsonRootObject.optJSONArray("data");

                List<HolidaysBean> list = new ArrayList<HolidaysBean>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);

                    HolidaysBean HolidaysBean = new HolidaysBean();
                    HolidaysBean.setdate_month_year(data.getString("date_month_year"));
                    HolidaysBean.setday(data.getString("day"));
                    list.add(HolidaysBean);
                }

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvHolidays);
                //GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3,LinearLayoutManager.VERTICAL,false);
                // recyclerView.setLayoutManager(gridLayoutManager);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(staggeredGridLayoutManager);

                HolidaysAdapter adapter = new HolidaysAdapter(list);
                recyclerView.setAdapter(adapter);


            } catch (JSONException e) {


                e.printStackTrace();
            }


            pDialog.dismiss();


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


    }*/
}