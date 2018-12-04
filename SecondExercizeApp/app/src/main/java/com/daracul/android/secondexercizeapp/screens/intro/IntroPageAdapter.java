package com.daracul.android.secondexercizeapp.screens.intro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;



public class IntroPageAdapter extends FragmentPagerAdapter {
    private List<Integer> imageIdList;

    public IntroPageAdapter(FragmentManager fm, List<Integer> imageIdList) {
        super(fm);
        this.imageIdList = imageIdList;
    }

    @Override
    public Fragment getItem(int position) {
        return IntroFragment.newInstance(imageIdList.get(position));
    }

    @Override
    public int getCount() {
        return imageIdList.size();
    }
}
