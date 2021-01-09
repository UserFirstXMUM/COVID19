package com.example.covid19;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(getContext());
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        webView=root.findViewById(R.id.webView);
        confirm_all=root.findViewById(R.id.confirm_all);
        death_all=root.findViewById(R.id.death_all);
        recoverd_all=root.findViewById(R.id.recover_all);
        confirm_c=root.findViewById(R.id.c_confirm);
        death_c=root.findViewById(R.id.c_death);
        recoverd_c=root.findViewById(R.id.c_recoverd);
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
                Intent intent = new Intent(getContext(), Travel.class);
                startActivity(intent);
            }
        });
        initWebView(webView);
        getData();
        getCountryData("Malaysia");
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
                    confirm_all.setText(jsonObject.getString("cases"));
                    death_all.setText(jsonObject.getString("deaths"));
                    recoverd_all.setText(jsonObject.getString("recovered"));
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
                        if (Objects.equals(country, jsonArray.getJSONObject(i).getString("country")));
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
                        confirm_c.setText(data.getString("/"));
                        death_c.setText(data.getString("/"));
                        recoverd_c.setText(data.getString("/"));
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
}