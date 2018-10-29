package com.daracul.android.secondexercizeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.daracul.android.secondexercizeapp.data.DataUtils;
import com.daracul.android.secondexercizeapp.data.NewsItem;
import com.daracul.android.secondexercizeapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewsListActivity extends AppCompatActivity {
    private static final int SPACE_BETWEEN_CARDS_IN_DP = 4;
    private static final String LOG_TAG = NewsListActivity.class.getSimpleName();
    private ProgressBar progressBar;
    RecyclerView list;
    NewsRecyclerAdapter adapter;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

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
                new ArrayList<NewsItem>(), clickListener);
        loadNews();
        list.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager;
        if (Utils.isHorizontal(this)) {
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
        if (list != null) list = null;
        if (adapter != null) adapter = null;
        compositeDisposable.clear();

    }

    public void loadNews() {
        showProgressBar();

        final Disposable disposable = DataUtils.observeNews().
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NewsItem>>() {
                    @Override
                    public void accept(List<NewsItem> newsItems) throws Exception {
                        hideProgressBar();
                        if (newsItems != null) {
                            adapter.swapData(newsItems);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
        compositeDisposable.add(disposable);
    }


    private void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressBar() {
        if (progressBar != null) {
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
