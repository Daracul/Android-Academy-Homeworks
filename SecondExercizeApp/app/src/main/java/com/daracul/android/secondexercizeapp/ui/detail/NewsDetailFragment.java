package com.daracul.android.secondexercizeapp.ui.detail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.database.Db;
import com.daracul.android.secondexercizeapp.database.News;
import com.daracul.android.secondexercizeapp.ui.detail.mvp.DetailPresenter;
import com.daracul.android.secondexercizeapp.ui.detail.mvp.DetailView;
import com.daracul.android.secondexercizeapp.utils.Utils;


import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewsDetailFragment extends MvpAppCompatFragment implements DetailView {
    public static final String KEY_FOR_POSITION = "position_key";
    private static final String LOG_TAG = "myLogs";
    private EditText topicTextView;
    private EditText fullTextView;
    private TextView dateTextView;
    private Drawable originalDrawable;
    private ImageView pictureImageView;
    @InjectPresenter
    DetailPresenter detailPresenter;



    public static NewsDetailFragment newInstance(String id) {
        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_FOR_POSITION, id);
        newsDetailFragment.setArguments(bundle);
        return newsDetailFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_scrolling, container, false);
        setupUI(view);
        setHasOptionsMenu(true);
        return view;
    }

    private void setupUI(View view) {
        topicTextView = view.findViewById(R.id.topic);
        originalDrawable = topicTextView.getBackground();
        dateTextView = view.findViewById(R.id.date);
        fullTextView = view.findViewById(R.id.full_text);
        makeLookLikeTextView(topicTextView);
        makeLookLikeTextView(fullTextView);
        pictureImageView = view.findViewById(R.id.news_picture);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            String position = getPositionId();
            detailPresenter.setPosition(position);

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                makeLookLikeEditText(topicTextView, originalDrawable);
                makeLookLikeEditText(fullTextView, originalDrawable);
                return true;
            case R.id.action_save:
                String topic = topicTextView.getText().toString();
                String fullText = fullTextView.getText().toString();
                detailPresenter.updateDb(topic, fullText);
                return true;
            case R.id.action_delete:
                detailPresenter.deleteFromdDb();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    public String getPositionId() {
        String  positionId = null;
        if (getArguments() != null) {
            positionId = getArguments().getString(KEY_FOR_POSITION);
        }
        return positionId;
    }

    @Override
    public void showPicture(@NonNull String url) {
        if (!url.isEmpty()) {
            Utils.loadImageAndSetToView(url, pictureImageView);
        } else pictureImageView.setImageResource(R.drawable.placeholder);
    }

    @Override
    public void showTopic(@NonNull String topic) {
        topicTextView.setText(topic);
    }

    @Override
    public void showDate(@NonNull Date date) {
        View view = getView();
        if (view!=null){
            dateTextView.setText(Utils.formatDateTime(getView().getContext(), date));
        }
    }

    @Override
    public void showFullText(@NonNull String fullText) {
        fullTextView.setText(fullText);
    }

    @Override
    public void showActionBar(@NonNull String text) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(text);
        }
    }

    @Override
    public void popBackStack() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showError(Throwable throwable) {
        Log.d(LOG_TAG, throwable.toString());
    }
}