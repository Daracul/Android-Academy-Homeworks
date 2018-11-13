package com.daracul.android.secondexercizeapp.ui.detail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.database.Db;
import com.daracul.android.secondexercizeapp.database.News;
import com.daracul.android.secondexercizeapp.utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewsDetailFragment extends Fragment {
    public static final String KEY_FOR_POSITION = "position_key";
    private static final String LOG_TAG = "myLogs";
    private EditText topicTextView;
    private EditText fullTextView;
    private Drawable originalDrawable;
    private News news;
    private Db db;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public static NewsDetailFragment newInstance(int position){
        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_FOR_POSITION, position);
        newsDetailFragment.setArguments(bundle);
        return newsDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_scrolling, container, false);
        Log.d(LOG_TAG,"Detail:onCreateView");
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG,"Detail:onActivityCreated");
        if (getActivity()!=null){
            int position = getArguments().getInt(KEY_FOR_POSITION);
            db = new Db(getActivity().getApplicationContext());
            loadDataFromDb(position);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        Log.d(LOG_TAG,"Detail:onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
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
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG,"Detail:onStop");
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
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action() {
                        @Override
                        public void run() throws Exception {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    });
            compositeDisposable.add(disposable);
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
        View view = getView();
        if (view!=null) {
            topicTextView = view.findViewById(R.id.topic);
            originalDrawable = topicTextView.getBackground();
            TextView dateTextView = view.findViewById(R.id.date);
            fullTextView = view.findViewById(R.id.full_text);
            makeLookLikeTextView(topicTextView);
            makeLookLikeTextView(fullTextView);
            ImageView pictureImageView = view.findViewById(R.id.news_picture);

            topicTextView.setText(news.getTitle());
            dateTextView.setText(Utils.formatDateFromApi(news.getPublishDate()));
            fullTextView.setText(news.getPreviewText());
            if (!news.getImageUrl().isEmpty()) {
                Utils.loadImageAndSetToView(news.getImageUrl(), pictureImageView);
            } else pictureImageView.setImageResource(R.drawable.placeholder);
            setupActionBar(news.getCategory(),view);
        }
    }

    private void setupActionBar(String title, View view) {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
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

    public int getPositionId(){
        int positionId = 0;
        if (getArguments()!=null){
            positionId = getArguments().getInt(KEY_FOR_POSITION);
        }
        return positionId;
    }
}