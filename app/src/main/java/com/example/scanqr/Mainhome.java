package com.example.scanqr;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.scanqr.fragment.Historyfragment;

public class Mainhome extends AppCompatActivity {
    ImageButton imgBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainhome);
        imgBT = (ImageButton) findViewById(R.id.nutdiemdanh);
        imgBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Mainhome.this,MainActivity.class);
                startActivity(intent);
            }
        });
        imgBT = (ImageButton) findViewById(R.id.dangx);
        imgBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Mainhome.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        imgBT= (ImageButton) findViewById(R.id.nuttuantra);
        imgBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mainhome.this, MapActivity.class);
                startActivity(intent);
            }
        });
        ImageButton mDialButton = (ImageButton) findViewById(R.id.nutthongbao);
        mDialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNo ="0931900062" ;
                String dial = "tel:" + phoneNo;
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
            }
        });
        imgBT= (ImageButton) findViewById(R.id.nutlich);
        imgBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mainhome.this,Historyfragment.class);
                startActivity(intent);
            }
        });
    }

}