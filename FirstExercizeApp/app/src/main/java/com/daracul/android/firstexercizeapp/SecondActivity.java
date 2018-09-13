package com.daracul.android.firstexercizeapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    public static final String MY_TEXT_KEY = "my_key" ;
    private TextView textView;
    private Button emailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView = (TextView)findViewById(R.id.second_text_view);
        emailButton = (Button) findViewById(R.id.email_button);
        Bundle bundle = getIntent().getExtras();
        String text = bundle.getString(MY_TEXT_KEY);
        textView.setText(text);

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEmailClient(textView.getText().toString());
            }
        });
    }

    private void startEmailClient(String text) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"au.malakhov@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, SecondActivity.class.getSimpleName().toString());
        if ((emailIntent.resolveActivity(getPackageManager())!=null)){
            startActivity(emailIntent);
        } else {
            Toast.makeText(this, R.string.toast_text_no_email_clients, Toast.LENGTH_LONG).show();
        }

    }

    public static void start(Activity activity, String text) {
        Intent secondActivityIntent = new Intent(activity, SecondActivity.class);
        secondActivityIntent.putExtra(SecondActivity.MY_TEXT_KEY, text);
        activity.startActivity(secondActivityIntent);
    }
}
