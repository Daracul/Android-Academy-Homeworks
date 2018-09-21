package com.daracul.android.secondexercizeapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String EMAIL_ADDRESS = "au.malakhov@gmail.com";
    private static final String EMAIL_SUBJECT = "Buisness letter";
    private static final String INSTAGRAM_URL = "https://www.instagram.com/daracul/?hl=ru";
    private static final String FACEBOOK_URL = "https://www.facebook.com/alexey.malakhov.9";
    private static final String VK_URL = "https://vk.com/id171214";
    private static final String DISCLAIMER_TEXT = "© 2018 Alexey Malakhov";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setTitle(R.string.my_name);
        }

        setupTextViews();
        setupSendButton();
        setupSocialNetworkButtons();
        setupDisclaimer();

    }

    private void setupDisclaimer() {
        LinearLayout bottomLinearLayout = (LinearLayout)findViewById(R.id.bottom_ll);
        TextView disclaimerTextView = new TextView(this);
        disclaimerTextView.setText(DISCLAIMER_TEXT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        disclaimerTextView.setLayoutParams(params);
        bottomLinearLayout.addView(disclaimerTextView);
    }

    private void setupSocialNetworkButtons() {

        ImageView instagramImageView = (ImageView) findViewById(R.id.instagram_image_view);
        ImageView facebookImageView = (ImageView)findViewById(R.id.facebook_image_view);
        ImageView vkImageView = (ImageView)findViewById(R.id.vk_image_view);
        instagramImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBrowserWithSocial(INSTAGRAM_URL);
            }
        });
        facebookImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBrowserWithSocial(FACEBOOK_URL);
            }
        });
        vkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBrowserWithSocial(VK_URL);
            }
        });
    }

    private void startBrowserWithSocial(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void setupTextViews() {
        TextView firstJobTitle = (TextView)findViewById(R.id.first_job_text_view);
        firstJobTitle.setText(R.string.first_job_title);

        TextView studyingJobTitle = (TextView)findViewById(R.id.second_job_text_view);
        studyingJobTitle.setText(R.string.studying_job_title);

        TextView currentJobTitle = (TextView)findViewById(R.id.third_job_text_view);
        currentJobTitle.setText(R.string.current_job_title);

        TextView infoTextView = (TextView)findViewById(R.id.info_text_view);
        infoTextView.setText(R.string.about_me);
    }

    private void setupSendButton() {
        Button sendMessageButton = (Button)findViewById(R.id.send_message_button);
        final EditText editText = (EditText)findViewById(R.id.send_message_edit_text);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToSend = editText.getText().toString();
                if (!textToSend.isEmpty()){
                    startEmailClient(textToSend);
                } else Toast.makeText(MainActivity.this, R.string.edit_text_empty_message,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startEmailClient(String text) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL_ADDRESS});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
        if ((emailIntent.resolveActivity(getPackageManager())!=null)){
            startActivity(emailIntent);
        } else {
            Toast.makeText(this, R.string.toast_text_no_email_clients, Toast.LENGTH_LONG).show();
        }

    }
}
