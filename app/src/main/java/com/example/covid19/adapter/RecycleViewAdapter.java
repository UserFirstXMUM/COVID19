package com.example.covid19.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.example.covid19.R;
import com.example.covid19.News;

public class RecycleViewAdapter extends RecyclerView.Adapter {

    private List<News> newsList;
    private Context context;

    public RecycleViewAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        NewsViewHolder newsViewHolder = (NewsViewHolder) holder;

        News news = newsList.get(i);

        Glide.with(context)
                .load(news.getImage())
                .into(newsViewHolder.newsImage);
        newsViewHolder.newsDesc.setText(news.getTitle());
        newsViewHolder.newsTitle.setText(news.getTitle() + '\n' + news.getPostTime());

        newsViewHolder.share.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
            intent.putExtra(Intent.EXTRA_TEXT, news.getTitle());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Intent.createChooser(intent, news.getTitle()));
        });

        newsViewHolder.readMore.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse(news.getUrl()));
            context.startActivity(intent);
        });

        newsViewHolder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse(news.getUrl()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView newsImage;
        TextView newsTitle;
        TextView newsDesc;
        Button share;
        Button readMore;

        public NewsViewHolder(final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            newsImage = itemView.findViewById(R.id.news_photo);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsDesc = itemView.findViewById(R.id.news_desc);
            share = itemView.findViewById(R.id.btn_share);
            readMore = itemView.findViewById(R.id.btn_more);
            //设置TextView背景为半透明
            newsTitle.setBackgroundColor(Color.argb(20, 0, 0, 0));
        }

        public void bindView(final int position) {

        }
    }
}

