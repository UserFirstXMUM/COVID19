package com.example.covid19;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.covid19.bean.UserInfo;
import com.example.covid19.database.UserDBHelper;
import com.example.covid19.util.FileUtil;
import com.example.covid19.util.SharedUtil;


import static android.content.Context.MODE_PRIVATE;

public class MeFragment extends Fragment {

    private static final String TAG = "MeFragment";
    private UserDBHelper mHelper;
    private TextView tvUsername;
    private ImageView avatar;
    private String user_id;

    RelativeLayout daily_report;
    RelativeLayout trip_record;
    RelativeLayout qrcode_scanner;
    RelativeLayout personal_information;
    RelativeLayout log_out;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        user_id = ((MainActivity) activity).getTitles();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        daily_report=view.findViewById(R.id.rela_dailyreport);
        trip_record = view.findViewById(R.id.rela_trip_record);
        qrcode_scanner=view.findViewById(R.id.rela_qrcode_scanner);
        personal_information = view.findViewById(R.id.rela_personal_information);
        avatar = view.findViewById(R.id.iv_avatar);
        log_out=view.findViewById(R.id.rela_logout);

        tvUsername = view.findViewById(R.id.tv_username_mine);
        SharedPreferences settings = getActivity().getSharedPreferences("settings", MODE_PRIVATE);
        String username = settings.getString("username", "");
        Log.d(TAG, username);

        //user_id = Integer.parseInt(getArguments().getString("user_id"));

        if (!username.isEmpty()) {
//            userService.info(username)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<BaseResponse<String>>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onNext(BaseResponse<String> response) {
//                            if (response.getStatus() == HttpURLConnection.HTTP_OK) {
//                                String currentAvatar = response.getData();
//                                Log.d(TAG, "current Avatar:" + currentAvatar);
//                                if (currentAvatar != null && !currentAvatar.isEmpty()) {
//                                    Glide.with(getContext())
//                                            .load(currentAvatar)
//                                            .into(avatar);
//                                } else {
//                                    Glide.with(getContext())
//                                            .load(defaultAvatar)
//                                            .into(avatar);
//                                }
//                            } else {
//                                Toast.makeText(getContext(),
//                                        response.getMsg(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            e.printStackTrace();
//                            Toast.makeText(getContext()
//                                    , "Connection Timeout", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
            tvUsername.setText(username);
        } else {
            Glide.with(getContext())
                    .load(R.drawable.avatar0)
                    .into(avatar);
        }

        setOnPersonalInfomationClicked();
        setOnDailyReportCalendarClicked();
        setOnTripRecordClicked();
        setonQRcodeScannerClicked();
        setOnLogOutClicked();

        //showHead();

        return view;
    }

    private String mFirst = "true";
    public void onResume() {
        super.onResume();
        mHelper = UserDBHelper.getInstance(getContext(), 2);
        mHelper.openReadLink();
        mHelper.openWriteLink();
        UserInfo.getDefaultList();
        UserInfo info = mHelper.UserqueryByUserid(user_id);
        avatar.setScaleType(ImageView.ScaleType.FIT_CENTER);
        avatar.setImageResource(R.drawable.moren);
        tvUsername.setText(info.username);
    }


    private void setOnPersonalInfomationClicked() {
        personal_information.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditPage.class);
            Bundle bundle = new Bundle();
            bundle.putString("user_id", user_id);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        });
    }

    private void setOnDailyReportCalendarClicked() {
        daily_report.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Date.class);
            Bundle bundle = new Bundle();
            bundle.putString("user_id", user_id);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setOnTripRecordClicked() {
        trip_record.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Travel.class);
            Bundle bundle = new Bundle();
            bundle.putString("user_id", user_id);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setonQRcodeScannerClicked() {
        qrcode_scanner.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QRcode.class);
            Bundle bundle = new Bundle();
            bundle.putString("user_id", user_id);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setOnLogOutClicked() {
        log_out.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginPage.class);
            startActivity(intent);
        });
    }
}
