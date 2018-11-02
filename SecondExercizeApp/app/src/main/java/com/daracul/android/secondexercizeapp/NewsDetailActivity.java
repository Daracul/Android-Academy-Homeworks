package com.daracul.android.secondexercizeapp;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.daracul.android.secondexercizeapp.database.Db;
import com.daracul.android.secondexercizeapp.database.News;
import com.daracul.android.secondexercizeapp.utils.Utils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewsDetailActivity extends AppCompatActivity {
    private static final String KEY_FOR_POSITION = "position_key";

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
            loadDataFromDb(position);
        }



    }

    private void loadDataFromDb(int position) {
        Db db = new Db(getApplicationContext());
        Disposable disposable = db.getNewsById(position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<News>() {
                    @Override
                    public void accept(News news) throws Exception {
                        fillViews(news);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("myLogs",throwable.toString());
                    }
                });
    }

    private void fillViews(News news) {
        TextView topicTextView = findViewById(R.id.topic);
        TextView dateTextView = findViewById(R.id.date);
        TextView fullTextView = findViewById(R.id.full_text);
        ImageView pictureImageView = findViewById(R.id.news_picture);

        topicTextView.setText(news.getTitle());
        dateTextView.setText(news.getPublishDate());
        fullTextView.setText(news.getPreviewText());
        Utils.loadImageAndSetToView(news.getImageUrl(),pictureImageView);

        setupActionBar(news.getCategory());
    }

    private void setupActionBar(String title) {

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