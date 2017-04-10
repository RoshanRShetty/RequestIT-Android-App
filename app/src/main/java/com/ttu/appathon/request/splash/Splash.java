package com.ttu.appathon.request.splash;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttu.appathon.request.R;

/**
 * Created by Pareshan on 2/20/2016.
 */
public class Splash extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        getSupportActionBar().hide();

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/brushstr.ttf");
        TextView tv = (TextView) findViewById(R.id.requesttitle);
        tv.setTypeface(tf);




        final ImageView emergency =  (ImageView) findViewById(R.id.emergency);
        final ImageView attraction =  (ImageView) findViewById(R.id.attraction);
        final ImageView hotel =  (ImageView) findViewById(R.id.hotel);
        final ImageView travel =  (ImageView) findViewById(R.id.travel);
        final ImageView food =  (ImageView) findViewById(R.id.waiter);
        final TextView requestTitle = (TextView) findViewById(R.id.requesttitle);
        final Animation animation = AnimationUtils.loadAnimation(getBaseContext(),R.anim.tween);
        final Animation animation2 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fall_in);


        emergency.startAnimation(animation);
        attraction.startAnimation(animation);
        hotel.startAnimation(animation);
        travel.startAnimation(animation);
        food.startAnimation(animation);
        requestTitle.startAnimation(animation);



        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                finish();
                Intent i;
                i = new Intent(getBaseContext(), com.ttu.appathon.request.layoutSelector.MainActivity.class);
                startActivity(i);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });





    }








}// end of class
