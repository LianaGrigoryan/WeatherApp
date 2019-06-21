package com.example.user.weatherapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<WeatherDay> day = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(Context context, List<WeatherDay> day) {
        this.context = context;
        this.day = day;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LinearLayout.inflate(parent.getContext(), R.layout.list_item,parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.dayTv.setText(DateFormatSymbols.getInstance().getWeekdays()[ day.get(position).getDate().get(Calendar.DAY_OF_WEEK)]);
        holder.maxTempTv.setText(day.get(position).getTempMax());
        holder.minTempTv.setText(day.get(position).getTempMin());
        Glide.with(context).load(day.get(position).getIconUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dayTv, minTempTv, maxTempTv;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            dayTv = itemView.findViewById(R.id.daytv);
            minTempTv = itemView.findViewById(R.id.mintemptv);
            maxTempTv = itemView.findViewById(R.id.maxtemptv);
        }
    }


}
