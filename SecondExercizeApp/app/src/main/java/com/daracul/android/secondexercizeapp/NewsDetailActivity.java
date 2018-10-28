package com.daracul.android.secondexercizeapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.daracul.android.secondexercizeapp.utils.Utils;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NewsDetailActivity extends AppCompatActivity {
    private static final String KEY_FOR_URL = "url_key";
    private WebView webView;

    public static void start (Activity activity, String url){
        Intent detailActivity = new Intent(activity, NewsDetailActivity.class);
        detailActivity.putExtra(NewsDetailActivity.KEY_FOR_URL, url);
        activity.startActivity(detailActivity);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        webView = findViewById(R.id.web_view);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String url = getIntent().getExtras().getString(KEY_FOR_URL);
            webView.loadUrl(url);
        }

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }



}
