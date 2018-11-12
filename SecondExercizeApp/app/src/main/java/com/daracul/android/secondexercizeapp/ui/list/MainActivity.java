package com.daracul.android.secondexercizeapp.ui.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.ui.detail.NewsDetailFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        isTwoPanel = findViewById(R.id.frame_detail)!=null;

        if (isTwoPanel){
            NewsListFragment newsListFragment = (NewsListFragment)getSupportFragmentManager().
                    findFragmentByTag(TAG_LIST_FRAGMENT);
            if (newsListFragment==null){
                Log.d("myLogs","Activity: fragment == null");
                newsListFragment = new NewsListFragment();
            } else Log.d("myLogs","Activity: fragment != null");
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_list,newsListFragment,TAG_LIST_FRAGMENT).commit();
            NewsDetailFragment newsDetailFragment = (NewsDetailFragment)getSupportFragmentManager().
                    findFragmentByTag(TAG_DETAIL_FRAGMENT);
            if (newsDetailFragment!=null){
                getSupportFragmentManager().beginTransaction().remove(newsDetailFragment)
                        .commit();
            }
        } else {
            if (savedInstanceState==null){
                NewsListFragment newsListFragment = new NewsListFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_list,newsListFragment).commit();
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    @Override
//    public void onBackPressed() {
//        if (isTwoPanel){
//            if (getSupportFragmentManager().getBackStackEntryCount()<=1){
//                finish();
//            }
//        }
//    }

    @Override
    public void openDetailFragment(int id) {
            NewsDetailFragment newsDetailFragment = NewsDetailFragment.newInstance(id);
            int frameId = isTwoPanel?R.id.frame_detail:R.id.frame_list;
            getSupportFragmentManager().beginTransaction()
                    .replace(frameId,newsDetailFragment,TAG_DETAIL_FRAGMENT)
                    .addToBackStack(null)
                    .commit();
        }
    }

