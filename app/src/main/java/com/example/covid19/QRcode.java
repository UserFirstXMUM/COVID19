package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid19.database.UserDBHelper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.gpfreetech.awesomescanner.ui.GpCodeScanner;
import com.gpfreetech.awesomescanner.ui.ScannerView;
import com.gpfreetech.awesomescanner.util.DecodeCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class QRcode extends AppCompatActivity {

    private GpCodeScanner mCodeScanner;
    private TextView txtScanText;
    UserDBHelper db;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_rcode);
        ScannerView scannerView = findViewById(R.id.scanner_view);
        txtScanText = findViewById(R.id.text);
        db=new UserDBHelper(getApplicationContext());

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        mCodeScanner = new GpCodeScanner(this, scannerView);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*
                        Sample:"{Add:Jalan Sunsuria,Bandar Sunsuria,43900 Sepang,Selangor Darul Ehsan,Malaysia}
                       {Name:Xiamen University Malaysia}"
                         */
                        Calendar calendar = Calendar.getInstance();
//获取系统的日期
//年
                        int year = calendar.get(Calendar.YEAR);
//月
                        int month = calendar.get(Calendar.MONTH)+1;
//日
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//分钟
                        int minute = calendar.get(Calendar.MINUTE);
//秒
                        int second = calendar.get(Calendar.SECOND);
                        String content=result.getText();
                        String name=null;
                        String address=null;
                        if (content.contains("Add:") && content.contains("}"))
                        {
                            address=content.substring(content.indexOf("Add:")+4,content.indexOf("}"));
                            if (content.contains("Name:") && content.lastIndexOf("}")!=-1)
                            name=content.substring(content.indexOf("Name:")+5,content.lastIndexOf("}"));
                            db.addTravel(user_id,name,address,year+"/"+month+"/"+day+"/ "+hour+":"+minute+":"+second);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Wrong Address format", Toast.LENGTH_SHORT).show();
                        }
                        txtScanText.setText("" + result.getText());
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCodeScanner!=null) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        if(mCodeScanner!=null) {
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }
    }
