package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid19.bean.UserInfo;
import com.example.covid19.database.UserDBHelper;
import com.example.covid19.util.DateUtil;
import com.example.covid19.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class EditPage extends AppCompatActivity {

    private UserDBHelper mHelper; // Declare an object for a user database helper
    private EditText et_name;
    private EditText et_age;
    private EditText et_email;
    private EditText et_phone;
    private EditText et_password1;
    private EditText et_gender;
    private EditText et_birth;
    private TextView edit_btn;
    private ImageView head;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);
        Bundle bundle=getIntent().getExtras();
        user_id = bundle.getInt("user_id",0);
        et_name = findViewById(R.id.et_username);
        et_age = findViewById(R.id.et_age);
        et_email = findViewById(R.id.et_email);
        et_phone = findViewById(R.id.et_phone);
        et_password1 = findViewById(R.id.et_password1);
        et_gender = findViewById(R.id.et_gender);
        et_birth = findViewById(R.id.et_birth);
        head = findViewById(R.id.head);
        edit_btn = findViewById(R.id.edit_btn);

        edit_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String age = et_age.getText().toString();
                String email = et_email.getText().toString();
                String phone = et_phone.getText().toString();
                String password1 = et_password1.getText().toString();
                String gender = et_gender.getText().toString();
                Boolean g=false;
                if (gender.equals("Male"))
                    g = true;
                else
                    g = false;
                String birth = et_birth.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    showToast("First fill name please");
                    return;
                } else if (TextUtils.isEmpty(age)) {
                    showToast("First fill age please");
                    return;
                } else if (TextUtils.isEmpty(gender)) {
                    showToast("First fill gender please");
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    showToast("First fill email please");
                    return;
                }else if (TextUtils.isEmpty(phone)) {
                    showToast("First fill phone number please");
                    return;
                }else if (TextUtils.isEmpty(password1)) {
                    showToast("First fill password please");
                    return;
                }

                // The following declares a user information object and fills in its field values
                UserInfo info = new UserInfo();
                info.username = name;
                info.age = Integer.parseInt(age);
                info.email = email;
                info.phone = phone;
                info.password = password1;
                info.birthday = birth;
                info.gender = g;
                info.xuhao = user_id;
                String user_id_s=String.format("%d", user_id);
                mHelper.Userupdate(info, "_id=" + user_id_s);
                showToast("Information has been updated to the SQLite database");
                Intent intent = new Intent(EditPage.this, MainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("user_id",user_id);
                bundle.putInt("user_id_d",user_id);
                intent.putExtras(bundle);
                startActivity(intent);
                //Refresh the page data and reload the data within onResume ()
                onResume();
            }
        });
    }

    protected void showUser()
    {
        String user_id_s=String.format("%d", user_id);
        UserInfo info1 = mHelper.UserqueryByUserid(user_id_s);
        et_name.setText(info1.username);
        et_age.setText(String.format("%d", info1.age));
        et_email.setText(info1.email);
        et_birth.setText(info1.birthday);
        String gender = "Male";
        if (info1.gender == false)
            gender = "Female";
        else
            gender = "Male";
        et_gender.setText(gender);
        et_phone.setText(info1.phone);
        et_password1.setText(info1.password);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHelper = UserDBHelper.getInstance(this, 2);
        mHelper.openWriteLink();
        showUser();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHelper.closeLink();
    }

    private void showToast(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
    }
}