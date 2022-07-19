package com.equalinfotechuser.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.equalinfotechuser.Activity.Attendence.Holidays.HolidaysActivity;
import com.equalinfotechuser.Activity.Attendence.MonthlyReports;
import com.equalinfotechuser.AppSharedPreference;
import com.equalinfotechuser.R;
import com.equalinfotechuser.URL_SUPPORT;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private static final View TODO = null;
    AppSharedPreference sharedpreferences;
    TextView name, emp_code, email_id, design, mobile;
    CircleImageView image;
    CardView card_attendance,card_holidays;

    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedpreferences = AppSharedPreference.getsharedprefInstance(getActivity());
        email_id = view.findViewById(R.id.email_id);
        emp_code = view.findViewById(R.id.emp_code);
        name = view.findViewById(R.id.name);
        image = view.findViewById(R.id.image);
        design = view.findViewById(R.id.designation);
        mobile = view.findViewById(R.id.mobile_no);
        card_attendance = view.findViewById(R.id.card_attendance);
        card_holidays = view.findViewById(R.id.cvHolidays);

                card_attendance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MonthlyReports.class);
                        startActivity(intent);

                    }

                }
        );
                    card_holidays.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), HolidaysActivity.class);
                        startActivity(intent);

                    }

                }
        );




        name.setText("User name");
        emp_code.setText(sharedpreferences.getEmployee_code());
        email_id.setText(sharedpreferences.getEmailId());

        ViewProfile();


        return view;
    }






    public void ViewProfile() {

        ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();


        String url = URL_SUPPORT.Baseurl + "myProfile";


        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("user_id", sharedpreferences.getuser_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("jsonpostdata", "" + jsonObject);
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {

            try {
                JSONObject json = (JSONObject) new JSONTokener(response.toString()).nextValue();
                JSONObject json2 = json.getJSONObject("data");
                String employee_code = (String) json2.get("employee_code");
                String names = (String) json2.get("name");
                String phone = (String) json2.get("phone");
                String email = (String) json2.get("email");
                String images = (String) json2.get("user_pic_url");
                // String department = (String) json2.get("department");
                String designation;
                try {
                    designation = (String) json2.get("designation");
                } catch (Exception e) {
                    designation = "";
                }


                name.setText(names);
                emp_code.setText(employee_code);
                email_id.setText(email);
                if (phone.equals("")) {
                    mobile.setText("XXXXXXXX");
                } else {
                    mobile.setText(phone);
                }

                design.setText(designation);

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Picasso.with(getActivity()).load(images)
                                .placeholder(R.drawable.ic_profile)
                                .error(R.drawable.ic_profile)
                                .into(image);
                    }
                });



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
