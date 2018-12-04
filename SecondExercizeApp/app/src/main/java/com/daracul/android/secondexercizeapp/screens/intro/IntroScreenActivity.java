package com.daracul.android.secondexercizeapp.screens.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.screens.list.MainActivity;
import com.daracul.android.secondexercizeapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;


import me.relex.circleindicator.CircleIndicator;

public class IntroScreenActivity extends AppCompatActivity {
    private static final String SHARED_PREF_KEY = "KEY_SHARED_PREF";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isLogoShown = Utils.loadBooleanFromSharedPreference(this, SHARED_PREF_KEY);
        if (isLogoShown) {
            Utils.saveBooleanToSharedPreference(this, false, SHARED_PREF_KEY);
            setContentView(R.layout.activity_intro);
            findViewById(R.id.tv_welcome).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.start(IntroScreenActivity.this);
                }
            });
            setupViewPager();
        } else {
            Utils.saveBooleanToSharedPreference(this, true, SHARED_PREF_KEY);
            MainActivity.start(this);
        }

    }

    private void setupViewPager() {
        ViewPager viewPager = findViewById(R.id.view_pager);
        IntroPageAdapter pageAdapter = new IntroPageAdapter(getSupportFragmentManager(), createImageIdList());
        CircleIndicator indicator = findViewById(R.id.indicator);
        viewPager.setAdapter(pageAdapter);
        indicator.setViewPager(viewPager);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private List<Integer> createImageIdList() {
        List<Integer> imageIdList = new ArrayList<>();
        imageIdList.add(R.drawable.image1);
        imageIdList.add(R.drawable.image2);
        imageIdList.add(R.drawable.image3);
        return imageIdList;
    }

}
