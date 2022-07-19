package com.equalinfotechuser.Activity.Attendence.beanclass;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.equalinfotechuser.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    List<MonthlyReportBean> listdata = new ArrayList<MonthlyReportBean>();
    SharedPreferences shared;
    TextView tvTime, punchout,timeintervall;
    String status;


    // RecyclerView recyclerView;
    public MyListAdapter(List<MonthlyReportBean> listdata) {
        this.listdata = listdata;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        tvTime = view.findViewById(R.id.tvTime);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.calander1, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;


    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MonthlyReportBean myListData = listdata.get(position);

        String test = listdata.get(position).timeinterval;
        int len=test.length();
        char first = test.charAt(1);
        Log.e("value",String.valueOf(first));
        try {
            int total_working_hour=Integer.parseInt(String.valueOf(first));
            if(total_working_hour<9)
            {
                holder.timeintervall.setTextColor(Color.RED);
            }
            else
            {
                holder.timeintervall.setTextColor(Color.WHITE);
            }
        } catch (Exception e)
        {

        }










        try {
            if (myListData.getIn_time().isEmpty() && myListData.getOut_time().isEmpty()) {
                  holder.rel_ab.setVisibility(View.VISIBLE);
                 holder.rel_pre.setVisibility(View.GONE);
                holder.rel_ab.setVisibility(View.VISIBLE);
                holder.rel_pre.setVisibility(View.GONE);


                String datetime = null;
                java.text.DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat d = new SimpleDateFormat("MMMM dd");
                try {
                    Date convertedDate = inputFormat.parse(myListData.getDate());
                    datetime = d.format(convertedDate);

                } catch (ParseException e) {

                }



                holder.date_absent.setText(datetime);
              /*  if (Integer.valueOf(first)<9) {

                    holder.timeintervall.setTextColor(Color.RED);


                } else {
                    // timeintervall.setTextColor(Color.BLACK);

                }*/

            } else {
                //   holder.layout_red.setVisibility(View.GONE);
                //   holder.linearLayout.setBackgroundResource(R.drawable.layout_back_green);
                holder.rel_ab.setVisibility(View.GONE);
                holder.rel_pre.setVisibility(View.VISIBLE);
                String datetime = null;
                java.text.DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat d = new SimpleDateFormat("MMMM dd");
                try {
                    Date convertedDate = inputFormat.parse(myListData.getDate());
                    datetime = d.format(convertedDate);

                } catch (ParseException e) {

                }

                holder.date.setText(datetime);
                holder.tvTime.setText(myListData.getIn_time());
                holder.tvTimeOut.setText(myListData.getOut_time());
                holder.timeintervall.setText(myListData.getTimeinterval()+" Hrs");
             /*   if (Integer.valueOf(first)<9) {

                    holder.timeintervall.setTextColor(Color.RED);


                } else {
                    // timeintervall.setTextColor(Color.BLACK);

                }*/

            }


        } catch (Exception e) {
            // holder.linearLayout.setBackgroundResource(R.drawable.layout_back_red);
        }

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date, tvTime, tvTimeOut, date_absent,timeintervall;
        public LinearLayout linearLayout, layout_red, lin_in, lin_out;
        public RelativeLayout rel_ab, rel_pre;

        public ViewHolder(View itemView) {
            super(itemView);
            this.date = (TextView) itemView.findViewById(R.id.tvDate);
            this.tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            this.tvTimeOut = (TextView) itemView.findViewById(R.id.tvTimeOut);
            this.timeintervall = (TextView) itemView.findViewById(R.id.timeintervall);
            this.lin_in = itemView.findViewById(R.id.lin_in);
            this.rel_ab = itemView.findViewById(R.id.relative_ab);
            this.rel_pre = itemView.findViewById(R.id.rel_present);
            this.date_absent = itemView.findViewById(R.id.tvDate_ab);


     /*       this.linearLayout = itemView.findViewById(R.id.layout);
            this.layout_red = itemView.findViewById(R.id.layout_red);*/

            //  this.date_absent=itemView.findViewById(R.id.date_absent);
        }
    }
}