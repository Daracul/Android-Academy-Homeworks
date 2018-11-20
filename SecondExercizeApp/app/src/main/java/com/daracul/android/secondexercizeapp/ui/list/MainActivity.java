package com.daracul.android.secondexercizeapp.ui.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.sync.NewsLoadService;
import com.daracul.android.secondexercizeapp.ui.detail.NewsDetailFragment;
import com.daracul.android.secondexercizeapp.utils.Utils;
import com.daracul.android.secondexercizeapp.utils.VersionUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements NewsListFragment.DetailFragmentListener {

    private static final String TAG_LIST_FRAGMENT = "list:fragment";
    private static final String TAG_DETAIL_FRAGMENT = "detail:fragment";
    private boolean isTwoPanel;


    public static void start(Activity activity) {
        Intent newsListActivity = new Intent(activity, MainActivity.class);
        activity.startActivity(newsListActivity);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isTwoPanel = findViewById(R.id.frame_detail) != null;

        if (savedInstanceState == null) {
            NewsListFragment newsListFragment = (NewsListFragment) getSupportFragmentManager().findFragmentByTag(TAG_LIST_FRAGMENT);
            if (newsListFragment == null) {
                newsListFragment = new NewsListFragment();
            }
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frame_list, newsListFragment, TAG_LIST_FRAGMENT).addToBackStack(TAG_LIST_FRAGMENT).commit();
            if (isTwoPanel) {
                getSupportFragmentManager().popBackStack(TAG_DETAIL_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                //checkicng if we have newsDetail in frame_list

                Bundle arguments = new Bundle();
                arguments.putInt(NewsDetailFragment.KEY_FOR_POSITION, 0);
                NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
                newsDetailFragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_detail, newsDetailFragment, TAG_DETAIL_FRAGMENT).commit();
            }

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (isTwoPanel) {
            int position = 0;
            NewsDetailFragment detailFragment = (NewsDetailFragment) getSupportFragmentManager().findFragmentByTag(TAG_DETAIL_FRAGMENT);
            if (detailFragment != null) {
                position = detailFragment.getPositionId();
            } else detailFragment = NewsDetailFragment.newInstance(0);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frame_list, getSupportFragmentManager().findFragmentByTag(TAG_LIST_FRAGMENT)).commit();
            openDetailFragment(position);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isTwoPanel) {
            finish();
        } else if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
            return;
        }
        super.onBackPressed();
    }


    @Override
    public void openDetailFragment(int id) {
        NewsDetailFragment newsDetailFragment = NewsDetailFragment.newInstance(id);
        int frameId = isTwoPanel ? R.id.frame_detail : R.id.frame_list;
        getSupportFragmentManager().beginTransaction()
                .replace(frameId, newsDetailFragment, TAG_DETAIL_FRAGMENT)
                .addToBackStack(TAG_DETAIL_FRAGMENT)
                .commit();
    }
}


