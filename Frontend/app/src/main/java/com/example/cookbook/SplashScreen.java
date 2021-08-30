package com.example.cookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {


    ImageView iconImage;
    TextView text1;
    Animation animation1,animation2;
    private final int SPLASH_DISPLAY_LENGTH = 4000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        iconImage = findViewById(R.id.iconImage);
        text1 = findViewById(R.id.cookTextView);
        animation1 = AnimationUtils.loadAnimation(SplashScreen.this,R.anim.custom);
        animation2 = AnimationUtils.loadAnimation(SplashScreen.this,R.anim.fadein2);


        iconImage.startAnimation(animation1);
        text1.startAnimation(animation2);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
