package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.covid19.bean.UserInfo;

import com.example.covid19.database.UserDBHelper;
import com.example.covid19.util.FileUtil;
import com.example.covid19.util.SharedUtil;

import java.util.ArrayList;

public class LoginPage extends AppCompatActivity {
    private EditText UserName;
    private EditText Password;
    private TextView Login;
    private TextView Register;
    private TextView Info;
    private int Counter;
    private UserDBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        UserName = findViewById(R.id.et_username);
        Password = findViewById(R.id.et_password1);
        Login = findViewById(R.id.loginbtn);
        Register = findViewById(R.id.register);
        Info = findViewById(R.id.info);
        Info.setText("No of attempts remaining: 5");
        Counter = 5;

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = UserName.getText().toString();
                String password = Password.getText().toString();
                UserInfo info=mHelper.UserqueryByUsername(username);
                if(info==null){
                    Counter --;
                    Info.setText("No. of attempts remaining: " + String.valueOf(Counter));
                    if (Counter == 0){
                        Login.setEnabled(false);}
                }
                else if(password.equals(info.password)){
                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("user_id",info.xuhao);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                    Counter --;
                    Info.setText("No. of attempts remaining: " + String.valueOf(Counter));
                    if (Counter == 0){
                        Login.setEnabled(false);
                    }
                }

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

    @Override
    protected void onStart() {
        super.onStart();
        // Get an instance of the database Helper
        mHelper = UserDBHelper.getInstance(this, 2);
        // Open the write connection to the database Helper
        mHelper.openWriteLink();
        UserInfo.getDefaultList();
    }
    private String mFirst = "true";
    @Override
    protected void onResume() {
        super.onResume();
        // Get an instance of the database Helper
        mHelper = UserDBHelper.getInstance(this, 2);
        // To restore the page, open the database connection
        mHelper.openWriteLink();
        mFirst = SharedUtil.getIntance(this).readShared("mylifefirst", "true");
        String path = MainApplication.getInstance().getExternalFilesDir(
                Environment.DIRECTORY_DOWNLOADS).toString() + "/";
        if (mFirst.equals("true")) { // open it for the first time
            ArrayList<UserInfo> usersList = UserInfo.getDefaultList();
            for (int i = 0; i < usersList.size(); i++) {
                UserInfo info = usersList.get(i);
                long rowid = mHelper.insert_user(info);
                info.rowid = rowid;
                Bitmap thumb = BitmapFactory.decodeResource(getResources(), info.portrait);
                MainApplication.getInstance().userIconMap.put((long)info.xuhao, thumb);
                String thumb_path = path + info.username + "_s.jpg";
                FileUtil.saveImage(thumb_path, thumb);
                info.head_portrait = thumb_path;
                mHelper.Userupdate(info);
            }
            ArrayList<UserInfo> goodsArray = mHelper.Userquery("1=1");
            for (int i = 0; i < goodsArray.size(); i++) {
                UserInfo info = goodsArray.get(i);
                Bitmap thumb = BitmapFactory.decodeFile(info.head_portrait);
                MainApplication.getInstance().userIconMap.put((long) info.xuhao, thumb);
            }
        }else { // not the first time
            ArrayList<UserInfo> goodsArray = mHelper.Userquery("1=1");
            for (int i = 0; i < goodsArray.size(); i++) {
                UserInfo info = goodsArray.get(i);
                // Reads bitmap data from the image file from the specified path
                Bitmap thumb = BitmapFactory.decodeFile(info.head_portrait);
                // Save the bitmap object in the global variable of the application instance
                MainApplication.getInstance().userIconMap.put((long) info.xuhao, thumb);
            }
        }
        SharedUtil.getIntance(this).writeShared("mylifefirst", "false");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause the page and close the database connection
        mHelper.closeLink();
    }
}
