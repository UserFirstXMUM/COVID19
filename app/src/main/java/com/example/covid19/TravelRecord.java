package com.example.covid19;

import com.example.covid19.database.UserDBHelper;

public class TravelRecord {
    String name;
    String address;
    String date;
    public TravelRecord(String name,String address,String date)
    {
        this.name=name;
        this.address=address;
        this.date=date;
    }
}
