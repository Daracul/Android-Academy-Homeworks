package com.daracul.android.secondexercizeapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daracul.android.secondexercizeapp.data.DataUtils;
import com.daracul.android.secondexercizeapp.data.NewsItem;
import com.daracul.android.secondexercizeapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsListActivity extends AppCompatActivity {
    private static final int SPACE_BETWEEN_CARDS_IN_DP = 4;
    private static final String LOG_TAG = NewsListActivity.class.getSimpleName();
    static ProgressBar progressBar;
    RecyclerView list;
    NewsRecyclerAdapter adapter;

    private final NewsRecyclerAdapter.OnItemClickListener clickListener =
            new NewsRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            NewsDetailActivity.start(NewsListActivity.this, position);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        progressBar = findViewById(R.id.progress_bar);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        list = findViewById(R.id.recycler);
        adapter = new NewsRecyclerAdapter(this,
                new ArrayList<NewsItem>(),clickListener);
        new LoadNewsAsync(adapter).execute();
        list.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager;
        if (Utils.isHorizontal(this)){
            layoutManager = new GridLayoutManager(this, 2);
        } else layoutManager = new LinearLayoutManager(this);
        list.addItemDecoration(
                new VerticalSpaceItemDecoration(Utils.convertDpToPixel(SPACE_BETWEEN_CARDS_IN_DP,
                        this)));
        list.setLayoutManager(layoutManager);
        list.setHasFixedSize(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (list!=null) list=null;
        if (adapter!=null) adapter = null;

    }

    public static class LoadNewsAsync extends AsyncTask <Void,Void, List<NewsItem>>{
        NewsRecyclerAdapter adapter;

        LoadNewsAsync(NewsRecyclerAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
        }

        @Override
        protected List<NewsItem> doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
                Log.d(LOG_TAG, Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return DataUtils.generateNews();
        }

        @Override
        protected void onPostExecute(List<NewsItem> newsItems) {
            super.onPostExecute(newsItems);
            hideProgressBar();
            if (newsItems!=null){
                adapter.swapData(newsItems);
            }
        }
    }

    private static void showProgressBar(){
        if (progressBar!=null){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private static void hideProgressBar(){
        if (progressBar!=null){
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
