package com.example.covid19;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid19.database.UserDBHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class HomeFragment extends Fragment {

    MaterialCardView Assessment_button;
    MaterialCardView info_button;
    MaterialCardView news_button;
    MaterialCardView code_button;
    WebView webView;
    com.google.android.material.button.MaterialButton report_but;
    TextView confirm_all;
    TextView death_all;
    TextView recoverd_all;
    TextView confirm_c;
    TextView death_c;
    TextView recoverd_c;
    String user_id;
    RadioButton radio;
    com.hbb20.CountryCodePicker ccp;
    UserDBHelper db;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        user_id = ((MainActivity) activity).getTitles();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        BottomSheetDialogFragment bottomSheet = new myBottomSheetDialog();
        Bundle bundle = new Bundle();
        bundle.putString("user_id", user_id);
        bottomSheet.setArguments(bundle);
        radio=root.findViewById(R.id.radioButton);
        webView=root.findViewById(R.id.webView);
        confirm_all=root.findViewById(R.id.confirm_all);
        death_all=root.findViewById(R.id.death_all);
        recoverd_all=root.findViewById(R.id.recover_all);
        confirm_c=root.findViewById(R.id.c_confirm);
        death_c=root.findViewById(R.id.c_death);
        recoverd_c=root.findViewById(R.id.c_recoverd);
        Assessment_button=root.findViewById(R.id.assessment_button);
        report_but= root.findViewById(R.id.daily_report);
        ccp=root.findViewById(R.id.ccp);
        Assessment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Assessment.class);
                startActivity(intent);
            }
        });
        db=new UserDBHelper(getContext());
        radio.setClickable(false);
        report_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet.show(getFragmentManager(), "DialogFragment");
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
                Intent intent = new Intent(getContext(), QRcode.class);
                startActivity(intent);
            }
        });
        initWebView(webView);
        getData();
        getCountryData(ccp.getSelectedCountryName());
        checkReport();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                getCountryData(ccp.getSelectedCountryName());
            }
        });
        return root;
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(WebView view) {
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setJavaScriptEnabled(true);
        view.setWebViewClient(new WebViewClient());
        //view.loadUrl("https://public.domo.com/cards/bWxVg");
        view.loadUrl("file:///android_asset/map.html");
    }
    private void getData(){
        RequestQueue queue= Volley.newRequestQueue(getActivity());

        String url="https://corona.lmao.ninja/v2/all";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    confirm_all.setText(bigNumber(jsonObject.getInt("cases")));
                    death_all.setText(bigNumber(jsonObject.getInt("deaths")));
                    recoverd_all.setText(bigNumber(jsonObject.getInt("recovered")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response",error.toString());
            }
        });
        queue.add(stringRequest);
    }
    private void getCountryData(String country){
        RequestQueue queue= Volley.newRequestQueue(getActivity());

        String url="https://corona.lmao.ninja/v2/countries";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject data=null;
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        String temp=jsonArray.getJSONObject(i).getString("country");
                        if (country.equals(temp))
                        {
                            data=jsonArray.getJSONObject(i);
                        }
                    }
                    if (data!=null) {
                        confirm_c.setText(data.getString("cases"));
                        death_c.setText(data.getString("deaths"));
                        recoverd_c.setText(data.getString("recovered"));
                    }
                    else
                    {
                        confirm_c.setText("/");
                        death_c.setText("/");
                        recoverd_c.setText("/");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response",error.toString());
            }
        });
        queue.add(stringRequest);
    }
    private String bigNumber (int number)
    {
        String result;
        if (number>1000000)
        {
            result=Integer.toString(number/1000000)+"."+Integer.toString((number-(number/1000000)*1000000)/100000)+"M";
        }
        else
        {
            result=Integer.toString(number);
        }
        return result;
    }
    public void checkReport()
    {
        Calendar calendar = Calendar.getInstance();
//获取系统的日期
//年
        int year = calendar.get(Calendar.YEAR);
//月
        int month = calendar.get(Calendar.MONTH)+1;
//日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date=year+"/"+month+"/"+day;
        if (!db.ReportDone(user_id,date))
        {
            radio.setChecked(false);
        }
        else
        {
            radio.setChecked(true);
            report_but.setVisibility(View.GONE);
        }

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.covid19.fresh_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("data");
                if("refresh".equals(msg)){
                    checkReport();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
    }

}