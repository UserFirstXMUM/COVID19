package com.example.covid19;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid19.CalendarView;

/**
 * 日历控件
 */

public class Date extends AppCompatActivity {
    private com.example.covid19.CalendarView calendar;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        calendar= findViewById(R.id.calendar);
        calendar=new com.example.covid19.CalendarView(getApplicationContext(),user_id);
        calendar.setOnDrawDays(new CalendarView.OnDrawDays() {
            @Override
            public boolean drawDay(Day day, Canvas canvas, Context context, Paint paint) {
                return false;
            }

            @Override
            public void drawDayAbove(Day day, Canvas canvas, Context context, Paint paint) {
            }
        });

    }
}