package com.daracul.android.secondexercizeapp.ui.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.ui.detail.NewsDetailFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements NewsListFragment.DetailFragmentListener {


    public static void start(Activity activity) {
        Intent newsListActivity = new Intent(activity, MainActivity.class);
        activity.startActivity(newsListActivity);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState==null){
            NewsListFragment newsListFragment = new NewsListFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_list,newsListFragment).commit();
        }

    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void openDetailFragment(int id) {
        NewsDetailFragment newsDetailFragment = NewsDetailFragment.newInstance(id);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_list,newsDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}
