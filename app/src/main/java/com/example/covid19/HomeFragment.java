package com.example.covid19;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.covid19.Assessment;
import com.example.covid19.R;
import com.example.covid19.ui.home.HomeViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;

public class HomeFragment extends Fragment {

    MaterialCardView Assessment_button;
    MaterialCardView info_button;
    MaterialCardView news_button;
    MaterialCardView code_button;
    com.google.android.material.button.MaterialButton report_but;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(getContext());
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Assessment_button=root.findViewById(R.id.assessment_button);
        Assessment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Assessment.class);
                startActivity(intent);
            }
        });
        report_but= root.findViewById(R.id.daily_report);
        report_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet.setContentView(R.layout.bottom_sheet);
                bottomSheet.show();
            }
        });
        info_button=root.findViewById(R.id.info_but);
        info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().
                        beginTransaction().replace(R.id.nav_host_fragment,new InfoFragment(),null).
                        addToBackStack(null).commit();
            }
        });
        news_button=root.findViewById(R.id.news_but);
        news_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().
                        beginTransaction().replace(R.id.nav_host_fragment,new NewsFragment(),null).
                        addToBackStack(null).commit();
            }
        });
        code_button=root.findViewById(R.id.code_but);
        code_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), QRPage.class);
                startActivity(intent);
            }
        });
        return root;
    }
}