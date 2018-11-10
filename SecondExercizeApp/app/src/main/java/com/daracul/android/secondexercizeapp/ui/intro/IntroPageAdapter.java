package com.daracul.android.secondexercizeapp.ui.intro;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
