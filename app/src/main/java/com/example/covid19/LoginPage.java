package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginPage<UserDBHelper> extends AppCompatActivity {
    private EditText UserName;
    private EditText Password;
    private com.google.android.material.textview.MaterialTextView Login;
    private com.google.android.material.textview.MaterialTextView Register;
    private TextView Info;
    private int Counter;
    private UserDBHelper mHelper;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        UserName = findViewById(R.id.et_username);
        Password = findViewById(R.id.et_password1);
        Login = findViewById(R.id.loginbtn);
        Register = findViewById((R.id.register));
        Info = findViewById(R.id.info);
        Info.setText("No of attempts remaining: 5");
        Counter = 5;

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                Bundle bundle=new Bundle();
                intent.putExtras(bundle);
                startActivity(intent);
                }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });
    }
}
