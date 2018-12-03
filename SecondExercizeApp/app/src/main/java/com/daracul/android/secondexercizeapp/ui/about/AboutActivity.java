package com.daracul.android.secondexercizeapp.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.ui.about.mvp.AboutPresenter;
import com.daracul.android.secondexercizeapp.ui.about.mvp.AboutView;


public class AboutActivity extends MvpAppCompatActivity implements AboutView {
    private static final String EMAIL_ADDRESS = "au.malakhov@gmail.com";
    private static final String EMAIL_SUBJECT = "Buisness letter";
    private static final String INSTAGRAM_URL = "https://www.instagram.com/daracul/?hl=ru";
    private static final String FACEBOOK_URL = "https://www.facebook.com/alexey.malakhov.9";
    private static final String VK_URL = "https://vk.com/id171214";

    @InjectPresenter
    AboutPresenter aboutPresenter;

    private TextView firstJobTitle;
    private TextView studyingJobTitle;
    private TextView currentJobTitle;
    private TextView infoTextView;
    private ImageView instagramImageView;
    private ImageView facebookImageView;
    private ImageView vkImageView;
    private Button sendMessageButton;
    private EditText editText;

//    @ProvidePresenter
//    AboutPresenter provideAboutPresenter(){
//        return new AboutPresenter();
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setupUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupUX();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindUX();
    }

    private void unbindUX() {
        instagramImageView.setOnClickListener(null);
        facebookImageView.setOnClickListener(null);
        vkImageView.setOnClickListener(null);
        sendMessageButton.setOnClickListener(null);
    }

    private void setupUI() {
        setupTextViews();
        setupSendButton();
        setupSocialNetworkButtons();
        setupDisclaimer();
    }

    private void setupUX(){
        instagramImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutPresenter.onClickOpenBrowser(INSTAGRAM_URL);
            }
        });
        facebookImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutPresenter.onClickOpenBrowser(FACEBOOK_URL);
            }
        });
        vkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutPresenter.onClickOpenBrowser(VK_URL);
            }
        });
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutPresenter.onClickSendMessage(EMAIL_ADDRESS, editText.getText().toString());
            }
        });
    }

    private void setupDisclaimer() {
        RelativeLayout bottomLinearLayout = findViewById(R.id.bottom_ll);
        TextView disclaimerTextView = new TextView(this);
        disclaimerTextView.setText(R.string.disclaimer_text);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.facebook_image_view);
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        disclaimerTextView.setLayoutParams(params);
        bottomLinearLayout.addView(disclaimerTextView);
    }

    private void setupSocialNetworkButtons() {
        instagramImageView =  findViewById(R.id.instagram_image_view);
        facebookImageView = findViewById(R.id.facebook_image_view);
        vkImageView = findViewById(R.id.vk_image_view);
    }


    private void setupTextViews() {
        firstJobTitle = findViewById(R.id.first_job_text_view);
        studyingJobTitle = findViewById(R.id.second_job_text_view);
        currentJobTitle = findViewById(R.id.third_job_text_view);
        infoTextView = findViewById(R.id.info_text_view);
    }

    private void setupSendButton() {
        sendMessageButton = findViewById(R.id.send_message_button);
        editText = findViewById(R.id.send_message_edit_text);
    }

    private void startEmailClient(String email, String text) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
        if ((emailIntent.resolveActivity(getPackageManager())!=null)){
            startActivity(emailIntent);
        } else {
            showToastMessage();
        }

    }

    private void showToastMessage() {
        Toast.makeText(this, R.string.toast_text_no_email_clients, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showActionBar(@NonNull String text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setTitle(text);
        }
    }

    @Override
    public void showDataInTextViews() {
        firstJobTitle.setText(R.string.first_job_title);
        studyingJobTitle.setText(R.string.studying_job_title);
        currentJobTitle.setText(R.string.current_job_title);
        infoTextView.setText(R.string.about_me);
    }

    @Override
    public void openEmailClient(@NonNull String email, @NonNull String message) {
        startEmailClient(EMAIL_ADDRESS, message);

    }

    @Override
    public void showToastMessage(@NonNull String toastText) {
        Toast.makeText(AboutActivity.this, toastText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

    }
}
