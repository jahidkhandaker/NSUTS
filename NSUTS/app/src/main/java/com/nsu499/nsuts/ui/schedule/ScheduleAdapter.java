package com.nsu499.nsuts.ui.schedule;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsu499.nsuts.R;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.stopageViewHolder> {

    Context mContext;
    ArrayList<ScheduleList> mScheduleLists;

    public ScheduleAdapter(Context c, ArrayList<ScheduleList> s){
        this.mContext = c;
        this.mScheduleLists = s;
    }

    @NonNull
    @Override
    public stopageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new stopageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.schedule_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull stopageViewHolder holder, int position) {
        holder.departTime.setText(mScheduleLists.get(position).getDepartTime());
        holder.FromTo.setText(mScheduleLists.get(position).getDepartFrom() +"->"+ mScheduleLists.get(position).getArrivedTo());
        holder.busTitle.setText(mScheduleLists.get(position).getBusTitle());
    }

    @Override
    public int getItemCount() {
        return mScheduleLists.size();
    }

    class stopageViewHolder extends RecyclerView.ViewHolder {
        TextView departTime, FromTo, busTitle;
        public stopageViewHolder(@NonNull View itemView) {
            super(itemView);
            departTime =  itemView.findViewById(R.id.departTime);
            FromTo =  itemView.findViewById(R.id.FromTo);
            busTitle =  itemView.findViewById(R.id.BusTitle);
        }
    }

}
