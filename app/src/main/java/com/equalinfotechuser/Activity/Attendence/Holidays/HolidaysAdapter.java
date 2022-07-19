package com.equalinfotechuser.Activity.Attendence.Holidays;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.equalinfotechuser.R;
import java.util.ArrayList;
import java.util.List;

public class HolidaysAdapter extends RecyclerView.Adapter<HolidaysAdapter.ViewHolder> {
    List<HolidaysBean> listdata = new ArrayList<HolidaysBean>();

    public HolidaysAdapter(List<HolidaysBean> listdata) {
        this.listdata = listdata;
    }

    public HolidaysAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        tvTime = view.findViewById(R.id.tvTime);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.holidays, parent, false);
        HolidaysAdapter.ViewHolder viewHolder = new HolidaysAdapter.ViewHolder(listItem);
        return viewHolder;


    }



    public void onBindViewHolder(HolidaysAdapter.ViewHolder holder, int position) {
        final HolidaysBean myListData = listdata.get(position);
        try {
   {

                holder.holidaydate.setText(myListData.getdate_month_year());
                holder.holidays.setText(myListData.getday());
            }
        } catch (Exception e) {
        }

    }

    public int getItemCount() {
        return listdata.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView holidaydate, holidays;

        public ViewHolder(View itemView) {
            super(itemView);
            this.holidaydate = (TextView) itemView.findViewById(R.id.tvHolidayDate);
            this.holidays = (TextView) itemView.findViewById(R.id.tvHoliday);

        }
    }
}
