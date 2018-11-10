package com.daracul.android.secondexercizeapp.ui.intro;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class IntroFragment extends Fragment {

    private static final String KEY_IMAGE_ID = "key:image:id";
    private int imageId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageId = getArguments().getInt(KEY_IMAGE_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return createSplashScreen();
    }

    private View createSplashScreen() {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(imageId);
        ViewGroup.LayoutParams imageLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ((LinearLayout.LayoutParams) imageLP).gravity = Gravity.CENTER;
        imageView.setLayoutParams(imageLP);
        linearLayout.addView(imageView);
        return linearLayout;
    }

    public static IntroFragment newInstance(int screen) {
        IntroFragment wordFragment = new IntroFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_IMAGE_ID, screen);
        wordFragment.setArguments(bundle);
        return wordFragment;
    }
}
