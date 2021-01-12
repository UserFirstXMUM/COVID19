package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

public class Travel extends AppCompatActivity {

    RecyclerView rv;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        rv=findViewById(R.id.travel_list);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        TravelAda listAda=new TravelAda(getApplicationContext(),user_id);
        rv.setAdapter(listAda);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);
    }
}