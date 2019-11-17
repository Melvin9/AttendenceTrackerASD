package com.divya.attendencetracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jackandphantom.circularprogressbar.CircleProgressbar;

import java.util.List;

public class AttendenceAdapter extends RecyclerView.Adapter<AttendenceAdapter.ViewHolder> {
    List<AttendenceData> attendenceData;
    private Context context;

    public AttendenceAdapter(List<AttendenceData> attendenceData, Context context) {
        this.attendenceData = attendenceData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendence_card,viewGroup,false);
        return new AttendenceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final AttendenceData list=attendenceData.get(i);
        viewHolder.per.setText((list.getTot_per()-list.getSt_per())*1.0/list.getTot_per()*100+"%"); // Default duration = 1500ms
        viewHolder.subid.setText(list.getSub_id());
        viewHolder.subname.setText(list.getSub_name());
        viewHolder.att.setText(list.getTot_per()-list.getSt_per()+"/"+list.getTot_per());

    }

    @Override
    public int getItemCount() { return attendenceData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subid,subname,att,per;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subid=itemView.findViewById(R.id.sid);
            subname=itemView.findViewById(R.id.subject);
            att=itemView.findViewById(R.id.attendence);
            per = itemView.findViewById(R.id.percentage);

        }
    }
}
