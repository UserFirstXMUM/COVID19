package com.example.covid19;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19.database.UserDBHelper;

import java.util.ArrayList;

public class TravelAda extends RecyclerView.Adapter {
    UserDBHelper db;
    ArrayList<TravelRecord> arr_record=new ArrayList<>();
    int count;
    public TravelAda(Context context)
    {
        db=new UserDBHelper(context);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        Cursor result=db.getTravelRecord();
        result.moveToFirst();
        count=result.getCount();
        return count;
    }
    private class ListViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView address;
        TextView date;
        TextView num;
        com.google.android.material.button.MaterialButton detail_but;
        ConstraintLayout Detail;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.travel_name);
            address=itemView.findViewById(R.id.address);
            date=itemView.findViewById(R.id.tra_date);
            num=itemView.findViewById(R.id.trave_no);
            detail_but=itemView.findViewById(R.id.detail_btn);
            Detail=itemView.findViewById(R.id.detail_tra);
            Cursor result=db.getTravelRecord();
            db=new UserDBHelper(itemView.getContext());
            result.moveToFirst();
            for (int i=0;i<count;i++)
            {
                String name=result.getString(result.getColumnIndex("NAME"));
                String address=result.getString(result.getColumnIndex("ADDRESS"));
                String date=result.getString(result.getColumnIndex("DATE"));
                arr_record.add(new TravelRecord(name,address,date));
                result.moveToNext();
            }
        }
        public void bindView(final int position) {
            name.setText(arr_record.get(position).name);
            address.setText(arr_record.get(position).address);
            date.setText(arr_record.get(position).date);
            num.setText(Integer.toString(position+1));
            detail_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Detail.getVisibility()==View.GONE)
                        Detail.setVisibility(View.VISIBLE);
                    else
                        Detail.setVisibility(View.GONE);
                }
            });
        }
    }
}
