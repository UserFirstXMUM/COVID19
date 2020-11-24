package com.example.covid19;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class RegisterPage extends AppCompatActivity implements OnClickListener {
    private EditText et_name;
    private EditText et_age;
    private EditText et_email;
    private EditText et_phone;
    private EditText et_password1;
    private EditText et_password2;
    private EditText et_gender;
    private EditText et_birth;
    private EditText registerbtn;
    private boolean manager = false;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        et_name = findViewById(R.id.et_username);
        et_age = findViewById(R.id.et_age);
        et_email = findViewById(R.id.et_email);
        et_phone = findViewById(R.id.et_phone);
        et_password1 = findViewById(R.id.et_password1);
        et_password2 = findViewById(R.id.et_password2);
        et_gender = findViewById(R.id.et_gender);
        et_birth = findViewById(R.id.et_birth);
        registerbtn = findViewById(R.id.register_btn);
        findViewById(R.id.register_btn).setOnClickListener(this);
        initTypeSpinner();
    }

    // Initializes the drop-down box
    private void initTypeSpinner() {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
                R.layout.item_select, typeArray);
        typeAdapter.setDropDownViewResource(R.layout.item_dropdown);
        Spinner sp_married = findViewById(R.id.sp_married);
        sp_married.setPrompt("Please select");
        sp_married.setAdapter(typeAdapter);
        sp_married.setSelection(0);
        sp_married.setOnItemSelectedListener(new TypeSelectedListener());
    }

    private String[] typeArray = {"User", "Manager"};
    class TypeSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            manager = (arg2 == 0) ? false : true;
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_btn) {
            String name = et_name.getText().toString();
            String age = et_age.getText().toString();
            String email = et_email.getText().toString();
            String phone = et_phone.getText().toString();
            String password1 = et_password1.getText().toString();
            String password2 = et_password2.getText().toString();
            String gender = et_gender.getText().toString();
            Boolean g=false;
            if (gender == "Male")
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
            }else if (TextUtils.isEmpty(birth)) {
                showToast("First fill birthday please");
                return;
            }else if (TextUtils.isEmpty(password2)) {
                showToast("First fill password please");
                return;
            }else if (!password1.equals(password2)) {
                showToast("Please make sure that the password input is consistent");
                return;
            }

            showToast("Information has been written to the SQLite database");
            Intent intent = new Intent(this, LoginPage.class);
            startActivity(intent);
        }
    }

    private void showToast(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
    }

}
