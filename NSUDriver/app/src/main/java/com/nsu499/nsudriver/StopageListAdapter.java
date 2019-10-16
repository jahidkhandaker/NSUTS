package com.nsu499.nsudriver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StopageListAdapter extends RecyclerView.Adapter<StopageListAdapter.stopageViewHolder> {

    Context mContext;
    ArrayList<StopageList> mStopageLists;

    public StopageListAdapter(Context c, ArrayList<StopageList> s){
        this.mContext = c;
        this.mStopageLists = s;
    }

    @NonNull
    @Override
    public stopageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new stopageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.stopage_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull stopageViewHolder holder, int position) {
        holder.stopageName.setText(mStopageLists.get(position).getStopageName());
        holder.waitingNo.setText(mStopageLists.get(position).getWaitingNum());
    }

    @Override
    public int getItemCount() {
        return mStopageLists.size();
    }

    class stopageViewHolder extends RecyclerView.ViewHolder {
        TextView stopageName,waitingNo;
        public stopageViewHolder(@NonNull View itemView) {
            super(itemView);
            stopageName = (TextView) itemView.findViewById(R.id.stopage_name);
            waitingNo = (TextView) itemView.findViewById(R.id.waiting_no);
        }
    }

}
