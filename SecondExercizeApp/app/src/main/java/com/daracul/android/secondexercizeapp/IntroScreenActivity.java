package com.daracul.android.secondexercizeapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

public class IntroScreenActivity extends AppCompatActivity {
    private static final String SHARED_PREF = "INTRO_SHARED_PREF";
    private static final String SHARED_PREF_KEY = "KEY_SHARED_PREF";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isLogoShown = initLogoBoolean();
        if (isLogoShown){
            saveLogoBoolean(false);
            createSplashScreen();
            waitToShowLogo();
        } else {
            saveLogoBoolean(true);
            NewsListActivity.start(this);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
        finish();
    }
    
    private void waitToShowLogo(){
        Disposable disposable = Completable
                .fromCallable(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return null;
                    }
                })
                .delay(2,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        NewsListActivity.start(IntroScreenActivity.this);
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void createSplashScreen(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.nyt_logo);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        linearLayout.addView(imageView);
        setContentView(linearLayout);
    }

    private void saveLogoBoolean(Boolean showLogo){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREF_KEY,showLogo);
        editor.apply();
    }

    private boolean initLogoBoolean(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        return sharedPreferences.getBoolean(SHARED_PREF_KEY,true);
    }
}
