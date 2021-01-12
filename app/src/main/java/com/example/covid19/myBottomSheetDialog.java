package com.example.covid19;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.covid19.R;
import com.example.covid19.database.UserDBHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class myBottomSheetDialog extends BottomSheetDialogFragment {
    com.google.android.material.button.MaterialButton button;
    com.google.android.material.textfield.TextInputEditText temperature;
    RadioGroup Q1;
    UserDBHelper db;
    String user_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bottom_sheet,
                container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            user_id = bundle.getString("user_id");
        }
        button=v.findViewById(R.id.report_button);
        temperature=v.findViewById(R.id.temperature);
        Q1=v.findViewById(R.id.q1_group);
        db=new UserDBHelper(getContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
//获取系统的日期
//年
                int year = calendar.get(Calendar.YEAR);
//月
                int month = calendar.get(Calendar.MONTH)+1;
//日
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String date=year+"/"+month+"/"+day;
                if (Q1.getCheckedRadioButtonId()==R.id.q1_checkBox2&&Float.valueOf(String.valueOf(temperature.getText()))<37.5)
                {
                    db.addReport(user_id,date,1);
                }
                else
                {
                    db.addReport(user_id,date,0);
                }
                Intent intent = new Intent("com.example.covid19.fresh_BROADCAST");
                intent.putExtra("data","refresh");
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                dismiss();
            }
        });
        return v;
    }
}
