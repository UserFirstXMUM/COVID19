package com.example.covid19;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.covid19.R;

public class InfoFragment extends Fragment {

    private VideoView videoView;
    private android.net.Uri Uri;
    private MediaController MediaController;
    private ImageView la1;
    private ImageView la2;
    private TextView text1;
    private TextView text3;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_info, container, false);

        ImageView la1 = root.findViewById(R.id.la1);
        ImageView la2 = root.findViewById(R.id.la2);
        TextView text1 = root.findViewById(R.id.text1);
        TextView text3 = root.findViewById(R.id.text3);

        la1.setOnClickListener(v -> {
            if (text1.getVisibility() == View.GONE)
                text1.setVisibility(View.VISIBLE);
            else
                text1.setVisibility(View.GONE);
        });

        la2.setOnClickListener(v -> {
            if (text3.getVisibility() == View.GONE)
                text3.setVisibility(View.VISIBLE);
            else
                text3.setVisibility(View.GONE);
        });

        return root;
    }


}