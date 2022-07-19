package com.equalinfotechuser.Activity.Attendence;

import static java.util.Calendar.MONTH;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.equalinfotechuser.Activity.Attendence.beanclass.MonthlyReportBean;
import com.equalinfotechuser.Activity.Attendence.beanclass.MyListAdapter;
import com.equalinfotechuser.AppSharedPreference;
import com.equalinfotechuser.R;
import com.equalinfotechuser.URL_SUPPORT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonthlyReports extends AppCompatActivity {


    RecyclerView monthly_list;
    ImageView calendar_next_button, calendar_prev_button;
    TextView date_display_date,timeintervall;
    AppSharedPreference sharedpreferences;

    Integer month = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_reports);
        calendar_next_button = findViewById(R.id.calendar_next_button);
        calendar_prev_button = findViewById(R.id.calendar_prev_button);
        date_display_date = findViewById(R.id.date_display_date);
        sharedpreferences = AppSharedPreference.getsharedprefInstance(getApplicationContext());

        formatdate("dd-MM-yyyy");
        ArrayList<String> months = new ArrayList<String>();
        months.add("January");
        months.add("Feburary");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");

        Calendar currentMonth = Calendar.getInstance();

        month = currentMonth.get(MONTH);
        date_display_date.setText(months.get(month));
        getMonthlyreport(sharedpreferences.getuser_id(), String.valueOf(month+1));

        calendar_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (month == 11) {
                    month = 0;
                } else {
                    month = month + 1;
                }
                date_display_date.setText(months.get(month));
                getMonthlyreport(sharedpreferences.getuser_id(), String.valueOf(month+1));
            }
        });
        calendar_prev_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (month == 0) {
                    month = 11;
                } else {
                    month = month - 1;
                }
                date_display_date.setText(months.get(month));
                getMonthlyreport(sharedpreferences.getuser_id(), String.valueOf(month+1));
            }
        });

    }


    public String formatdate(String fdate) {
        String datetime = null;
        java.text.DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat d = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        } catch (ParseException e) {

        }
        return datetime;


    }


    public void getMonthlyreport(String userid, String month) {

        ProgressDialog pDialog = new ProgressDialog(MonthlyReports.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();


        String url = URL_SUPPORT.Baseurl + "getallattendencebyemployee";

        Date d = new Date();

        CharSequence s = DateFormat.format("MMMM/dd/yyyy", d.getTime());
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("user_id", userid);
            jsonObject.accumulate("month", month);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("jsonpostdata", "" + jsonObject);
        final RequestQueue requestQueue = Volley.newRequestQueue(MonthlyReports.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {

            try {

                JSONObject jsonRootObject = new JSONObject(response.toString());

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = jsonRootObject.optJSONArray("data");

                List<MonthlyReportBean> list = new ArrayList<MonthlyReportBean>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);

                    MonthlyReportBean monthlyReportBean = new MonthlyReportBean();
                    monthlyReportBean.setDate(data.getString("date"));
                    monthlyReportBean.setIn_time(data.getString("in_date_time"));
                    monthlyReportBean.setOut_time(data.getString("out_date_time"));
                    monthlyReportBean.setTimeinterval(data.getString("timeinterval"));
                    list.add(monthlyReportBean);
                }

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvdate);
                //GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3,LinearLayoutManager.VERTICAL,false);
                // recyclerView.setLayoutManager(gridLayoutManager);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(staggeredGridLayoutManager);

                MyListAdapter adapter = new MyListAdapter(list);
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



}
