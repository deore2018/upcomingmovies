package com.example.rahul.upcomingmovies.splashactivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.rahul.upcomingmovies.R;
import com.example.rahul.upcomingmovies.activity.ListActivity;

public class AnimationWindow extends AppCompatActivity {
    Button Go;
    LinearLayout l1;
    Animation uptodown;
    private RelativeLayout retryLayout;
    private static final int DELAY_SECONDS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_window);

        Go = (Button)findViewById(R.id.btngo);

        Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
            }
        });
        init();
    }
    private void init() {
        if (isNetworkAvailable(this)) {
            sleepAndContinue(DELAY_SECONDS);
            startActivity(new Intent(this, ListActivity.class));
            finish();
        } else {

            Toast.makeText(getApplicationContext(), R.string.please_connect_to_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private void sleepAndContinue(int delay) {
        Runnable activityStart = new Runnable() {
            @Override
            public void run() {
            }
        };
        new Handler().postDelayed(activityStart, delay);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
