package com.daracul.android.secondexercizeapp.ui.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.ui.about.AboutActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ListActivity extends AppCompatActivity {

    public static void start(Activity activity) {
        Intent newsListActivity = new Intent(activity, ListActivity.class);
        activity.startActivity(newsListActivity);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        NewsListFragment newsListFragment = new NewsListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_list,newsListFragment).commit();
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
