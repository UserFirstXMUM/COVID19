package com.example.covid19;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.util.Objects;

public class Assessment extends AppCompatActivity {
    private RadioGroup group1;
    private RadioGroup group2;
    private RadioGroup group3;
    private RadioGroup group4;
    private RadioGroup group5;

    private LinearLayout layout2;
    private LinearLayout layout3;
    private LinearLayout layout4;
    private LinearLayout layout5;
    private LinearLayout[] layouts;
    ImageView back_button;

    private LinearLayout layoutNegative;
    private LinearLayout layoutPositive;

    private TextView textNegative;
    private TextView textPositive;

    private MaterialCardView resultCard;
    private NestedScrollView scrollView;
    private Button submitButton;


    private int resultType = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        resultCard = findViewById(R.id.assessment_result);
        submitButton = findViewById(R.id.button);
        layoutNegative = findViewById(R.id.assessment_result_N);
        layoutPositive = findViewById(R.id.assessment_result_Y);
        textNegative = findViewById(R.id.n_result);
        textPositive = findViewById(R.id.y_result);
        back_button=findViewById(R.id.back);
        scrollView=findViewById(R.id.sv);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initGroups();
        initLayout();

        setGroupOnClickListeners();
        setOnSubmitButtonClicked();
    }
    private void initGroups() {
        group1 = findViewById(R.id.q1_group);
        group2 = findViewById(R.id.q2_group);
        group3 =findViewById(R.id.q3_group);
        group4 = findViewById(R.id.q4_group);
        group5 = findViewById(R.id.q5_group);
    }

    private void initLayout() {
        layout2 = findViewById(R.id.q2_view);
        layout3 = findViewById(R.id.q3_view);
        layout4 = findViewById(R.id.q4_view);
        layout5 = findViewById(R.id.q5_view);
        layouts = new LinearLayout[]{layout2, layout3, layout4, layout5};
    }

    private void setGroupOnClickListeners() {
        group1.setOnCheckedChangeListener((group, checkedId) -> {
            resultCard.setVisibility(View.GONE);
            switch (checkedId) {
                case R.id.q1_checkBox:
                    resultType = 3;
                    setLayoutVisibilityGone(0);
                    break;
                case R.id.q1_checkBox2:
                    layout2.setVisibility(View.VISIBLE);
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        group2.setOnCheckedChangeListener((group, checkedId) -> {
            resultCard.setVisibility(View.GONE);
            switch (checkedId) {
                case R.id.q2_checkBox:
                    resultType = 3;
                    setLayoutVisibilityGone(1);
                    break;
                case R.id.q2_checkBox2:
                    layout3.setVisibility(View.VISIBLE);
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        group3.setOnCheckedChangeListener((group, checkedId) -> {
            resultCard.setVisibility(View.GONE);
            switch (checkedId) {
                case R.id.q3_checkBox:
                    resultType = 2;
                    setLayoutVisibilityGone(2);
                    break;
                case R.id.q3_checkBox2:
                    layout4.setVisibility(View.VISIBLE);
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        group4.setOnCheckedChangeListener((group, checkedId) -> {
            resultCard.setVisibility(View.GONE);
            switch (checkedId) {
                case R.id.q4_checkBox:
                    resultType = 1;
                    setLayoutVisibilityGone(3);
                    break;
                case R.id.q4_checkBox2:
                    layout5.setVisibility(View.VISIBLE);
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        group5.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.q5_checkBox:
                    resultType = 1;
                    break;
                case R.id.q5_checkBox2:
                    resultType = 0;
            }
        });
    }

    private void setLayoutVisibilityGone(int from) {
        for (int i = from; i < layouts.length; i++) {
            layouts[i].setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setOnSubmitButtonClicked() {
        submitButton.setOnClickListener(v -> {
            resultCard.setVisibility(View.VISIBLE);
            switch (resultType) {
                case 0:
                    layoutNegative.setVisibility(View.VISIBLE);
                    layoutPositive.setVisibility(View.GONE);
                    textNegative.setText("You don’t appear to have symptoms of COVID-19.");
                    break;
                case 1:
                    layoutNegative.setVisibility(View.VISIBLE);
                    layoutPositive.setVisibility(View.GONE);
                    textNegative.setText("Please self-isolate for 14 days and self-monitor for symptoms");
                    break;
                case 2:
                    textPositive.setText("Please get a COVID-19 test and self-isolate for 14 days or longer.");
                    layoutPositive.setVisibility(View.VISIBLE);
                    layoutNegative.setVisibility(View.GONE);
                    break;
                case 3:
                    textPositive.setText("Please go directly to your nearest emergency department.");
                    layoutPositive.setVisibility(View.VISIBLE);
                    layoutNegative.setVisibility(View.GONE);
                    break;
                default:
                    resultCard.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getApplicationContext()));
                    builder.setTitle("Reminder");
                    builder.setMessage("Please answer all questions");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    return;
            }
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        });
    }

}