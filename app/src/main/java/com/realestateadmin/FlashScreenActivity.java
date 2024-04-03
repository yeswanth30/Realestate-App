package com.realestateadmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FlashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_TIME = 1500;
    TextView back;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashscreen_activity);

//        back=findViewById(R.id.back);
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FlashScreenActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (isLoggedIn()) {
                    Intent intent = new Intent(FlashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                   // Intent intent = new Intent(FlashScreenActivity.this, PagerActivity.class);
                    Intent intent = new Intent(FlashScreenActivity.this, LoginActivity.class);

                    startActivity(intent);
                }
                finish();
            }
        }, SPLASH_DISPLAY_TIME);
    }

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}

