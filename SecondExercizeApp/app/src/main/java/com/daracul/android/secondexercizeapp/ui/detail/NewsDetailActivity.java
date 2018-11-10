package com.daracul.android.secondexercizeapp.ui.detail;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.database.Db;
import com.daracul.android.secondexercizeapp.database.News;
import com.daracul.android.secondexercizeapp.utils.Utils;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewsDetailActivity extends AppCompatActivity {
    private static final String KEY_FOR_POSITION = "position_key";
    private static final String LOG_TAG = "myLogs";
    private EditText topicTextView;
    private EditText fullTextView;
    private Drawable originalDrawable;
    private News news;
    private Db db;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static void start(Activity activity, int position) {
        Intent detailActivity = new Intent(activity, NewsDetailActivity.class);
        detailActivity.putExtra(NewsDetailActivity.KEY_FOR_POSITION, position);
        activity.startActivity(detailActivity);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        db = new Db(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int position = getIntent().getExtras().getInt(KEY_FOR_POSITION);
            loadDataFromDb(position);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                makeLookLikeEditText(topicTextView,originalDrawable);
                makeLookLikeEditText(fullTextView, originalDrawable);
                return true;
            case R.id.action_save:
                updateDb();
                return true;
            case R.id.action_delete:
                deleteFromdDb();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    private void updateDb() {
        if (news!=null){
            news.setTitle(topicTextView.getText().toString());
            news.setPreviewText(fullTextView.getText().toString());
            Disposable disposable = db.updateNews(news)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
            compositeDisposable.add(disposable);

        }
    }

    private void deleteFromdDb() {
        if (news!=null){
            Disposable disposable = db.deleteNews(news)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
            compositeDisposable.add(disposable);
            finish();
        }
    }


    private void loadDataFromDb(int position) {
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
                        Log.d(LOG_TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void fillViews(News news) {
        this.news = news;
        topicTextView = findViewById(R.id.topic);
        originalDrawable = topicTextView.getBackground();
        TextView dateTextView = findViewById(R.id.date);
        fullTextView = findViewById(R.id.full_text);
        makeLookLikeTextView(topicTextView);
        makeLookLikeTextView(fullTextView);
        ImageView pictureImageView = findViewById(R.id.news_picture);

        topicTextView.setText(news.getTitle());
        dateTextView.setText(Utils.formatDateFromApi(news.getPublishDate()));
        fullTextView.setText(news.getPreviewText());
        if (!news.getImageUrl().isEmpty()){
            Utils.loadImageAndSetToView(news.getImageUrl(), pictureImageView);
        } else pictureImageView.setImageResource(R.drawable.placeholder);
        setupActionBar(news.getCategory());
    }

    private void setupActionBar(String title) {

        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.collapsing_toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (toolbarLayout != null && actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbarLayout.setTitle(title);
        }
    }

    private void makeLookLikeTextView(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setLongClickable(false);
        editText.setBackground(null);
    }

    private void makeLookLikeEditText(EditText editText, Drawable original) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setLongClickable(true);
        editText.setBackground(original);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}