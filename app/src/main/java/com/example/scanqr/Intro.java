package com.example.scanqr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ramotion.paperonboarding.PaperOnboardingEngine;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

public class Intro extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_main_layout);

        PaperOnboardingEngine engine = new PaperOnboardingEngine(findViewById(R.id.onboardingRootView), getPaperOnboardingPageData(), this);
        engine.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {
                Intent login = new Intent(Intro.this, LoginActivity.class);
                startActivity(login);
            }
        });
    }
    private ArrayList<PaperOnboardingPage> getPaperOnboardingPageData() {
        PaperOnboardingPage scr1 = new PaperOnboardingPage("Xem những người bán mà bạn đã tiếp xúc",
                "Giúp các bạn xem danh sách các người bán và thông tin chi tiết của họ một các nhanh chóng và đơn giản nhất.",
                Color.parseColor("#172738"), R.drawable.iconintro, R.drawable.icon_doc1);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);

        return elements;
    }
}
