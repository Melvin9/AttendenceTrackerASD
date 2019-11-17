package com.divya.attendencetracker;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    public static String[] subjects= new String[100];
    public static int c=0;
    List<SubjectData> subjectData;
    private Context context;
    public SubjectAdapter(List<SubjectData> subjectData,Context context) {
        this.subjectData = subjectData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subject_card,viewGroup,false);
        return new SubjectAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final SubjectData list=subjectData.get(i);
        viewHolder.period.setText("Period: "+(i+1));
        viewHolder.sub.setText(list.getSub_id());
    }

    @Override
    public int getItemCount() {
        return subjectData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView period,sub;
        LinearLayout linearLayout;
        int count=0;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sub=itemView.findViewById(R.id.subject);
            period=itemView.findViewById(R.id.period);
            linearLayout=itemView.findViewById(R.id.linearBg);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    count++;
                    if(count%2!=0) {
                        linearLayout.setBackgroundColor(Color.parseColor("#ffff4444"));
                        subjects[c]=sub.getText().toString();
                        c++;
                    }else {
                        for(int i=0;i<c;i++)
                            if(subjects[i].equals(sub.getText().toString())){
                                c--;
                                for(int j=i;j<c;j++){
                                    subjects[j]=subjects[j+1];
                                }
                                break;
                        }
                        linearLayout.setBackgroundColor(Color.parseColor("#3CBB54"));
                    }
                }
            });

        }
    }
}
