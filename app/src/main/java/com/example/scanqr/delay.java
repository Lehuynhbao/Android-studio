package com.example.scanqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

public class delay extends AppCompatActivity {

    ImageView bg,logo;
    TextView name,infec;
    private static int x=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay);

        logo=(ImageView) findViewById(R.id.logo);
        bg=(ImageView) findViewById(R.id.bg);
        name=(TextView) findViewById(R.id.delay_name);
        infec=(TextView) findViewById(R.id.delay_infec);

        logo.animate().translationY(-3000).setDuration(1000).setStartDelay(2000);
        name.animate().translationY(-3000).setDuration(1000).setStartDelay(2000);
        infec.animate().translationY(-3000).setDuration(1000).setStartDelay(2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(delay.this,LoginActivity.class);
                startActivity(intent);
            }
        },x);
    }
}