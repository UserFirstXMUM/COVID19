package com.example.covid19;

        import android.annotation.SuppressLint;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Bitmap;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;

        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import android.content.res.AssetManager;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.webkit.WebSettings;
        import android.webkit.WebView;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.google.gson.Gson;
        import com.google.gson.reflect.TypeToken;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.nio.charset.StandardCharsets;
        import java.util.List;

        import com.example.covid19.R;
        import com.example.covid19.adapter.RecycleViewAdapter;
        import com.example.covid19.News;

public class NewsFragment extends Fragment {

    private static final String TAG = "NewsFragment";

    private static int count = 0;
    private List<News> newsList;
    private RecycleViewAdapter adapter;

    @Nullable
    @Override
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        initNewsData();
        adapter = new RecycleViewAdapter(newsList, getContext());
        RecyclerView recyclerView = view.findViewById(R.id.news_recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);




        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")


    private void initNewsData() {
        //获得assets资源管理器（assets中的文件无法直接访问，可以使用AssetManager访问）
        AssetManager assetManager = getContext().getAssets();
        try (InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open("who_news.json"), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(inputStreamReader)) {
            Gson gson = new Gson();
            newsList = gson.fromJson(br, new TypeToken<List<News>>() {
            }.getType());
            newsList = newsList.subList(0,50);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



