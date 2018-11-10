package com.daracul.android.secondexercizeapp.ui.intro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.ui.list.NewsListActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

public class IntroScreenActivity extends AppCompatActivity {
    private static final String SHARED_PREF = "INTRO_SHARED_PREF";
    private static final String SHARED_PREF_KEY = "KEY_SHARED_PREF";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isLogoShown = initLogoBoolean();
        if (isLogoShown) {
            saveLogoBoolean(false);
            setContentView(R.layout.activity_intro);
            findViewById(R.id.tv_welcome).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsListActivity.start(IntroScreenActivity.this);
                }
            });
            setupViewPager();
        } else {
            saveLogoBoolean(true);
            NewsListActivity.start(this);
        }

    }

    private void setupViewPager() {
        ViewPager viewPager = findViewById(R.id.view_pager);
        IntroPageAdapter pageAdapter = new IntroPageAdapter(getSupportFragmentManager(),createImageIdList());
        CircleIndicator indicator = findViewById(R.id.indicator);
        viewPager.setAdapter(pageAdapter);
        indicator.setViewPager(viewPager);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private List<Integer> createImageIdList(){
        List<Integer> imageIdList = new ArrayList<>() ;
        imageIdList.add(R.drawable.image1);
        imageIdList.add(R.drawable.image2);
        imageIdList.add(R.drawable.image3);
        return imageIdList;
    }

    private void saveLogoBoolean(Boolean showLogo) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREF_KEY, showLogo);
        editor.apply();
    }

    private boolean initLogoBoolean() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        return sharedPreferences.getBoolean(SHARED_PREF_KEY, true);
    }
}
