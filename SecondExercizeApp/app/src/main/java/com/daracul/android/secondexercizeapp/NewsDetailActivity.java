package com.daracul.android.secondexercizeapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.daracul.android.secondexercizeapp.data.DataUtils;
import com.daracul.android.secondexercizeapp.data.NewsItem;
import com.daracul.android.secondexercizeapp.utils.Utils;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NewsDetailActivity extends AppCompatActivity {
    private static final String KEY_FOR_POSITION = "position_key";
    private final List<NewsItem> newsList = DataUtils.generateNews();

    public static void start (Activity activity, int position){
        Intent detailActivity = new Intent(activity, NewsDetailActivity.class);
        detailActivity.putExtra(NewsDetailActivity.KEY_FOR_POSITION, position);
        activity.startActivity(detailActivity);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);



        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            int position = getIntent().getExtras().getInt(KEY_FOR_POSITION);
            setupActionBar(position, newsList.get(position).getCategory().getName());
            fillViews(position);
        }

    }

    private void fillViews(int position) {
        TextView topicTextView = findViewById(R.id.topic);
        TextView dateTextView = findViewById(R.id.date);
        TextView fullTextView = findViewById(R.id.full_text);
        ImageView pictureImageView = findViewById(R.id.news_picture);

        topicTextView.setText(newsList.get(position).getTitle());
        dateTextView.setText(Utils.convertDateToString(newsList.get(position).getPublishDate()));
        fullTextView.setText(newsList.get(position).getFullText());
        Utils.loadImageAndSetToView(newsList.get(position).getImageUrl(),pictureImageView);
    }

    private void setupActionBar(int position, String title) {

        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.collapsing_toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (toolbarLayout!=null&&actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbarLayout.setTitle(title);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
