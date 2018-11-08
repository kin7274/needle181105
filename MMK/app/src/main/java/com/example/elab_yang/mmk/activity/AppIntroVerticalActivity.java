package com.example.elab_yang.mmk.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.dreamwalker.verticalintrov2.VerticalIntro;
import com.dreamwalker.verticalintrov2.VerticalIntroItem;
import com.example.elab_yang.mmk.R;

public class AppIntroVerticalActivity extends VerticalIntro {
    private static final String TAG = "IntroVertical";

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 1페이지
        addIntroItem(new VerticalIntroItem.Builder()
                .backgroundColor(R.color.white)
                .image(R.drawable.image123)
                .title("대한민국의 어머니들")
                .titleSize(30)
                .titleColor(R.color.black)
                .text("스마트 니들입니다. KNU 만세")
                .textSize(20)
                .textColor(R.color.black)
                .build());

        // 2페이지
        addIntroItem(new VerticalIntroItem.Builder()
                .backgroundColor(R.color.ergo_colorPrimary)
                .image(R.drawable.image_drawer_header)
                .title("편리한 혈당기록 가능")
                .titleSize(30)
                .titleColor(R.color.black)
                .text("자동저장해주요")
                .textSize(20)
                .textColor(R.color.black)
                .build());

        // 3페이지
        addIntroItem(new VerticalIntroItem.Builder()
                .backgroundColor(R.color.colorAccent)
                .image(R.drawable.ripple_bg_white)
                .title("고맙")
                .titleSize(30)
                .titleColor(R.color.white)
                .text("다 읽어줬네 ㄳ")
                .textSize(20)
                .textColor(R.color.white)
                .build());

        setDoneText("시작해볼까요?");
        setTheme(android.R.style.Theme_Holo_Dialog_NoActionBar);
        setSkipEnabled(true);
        setSkipText("스-킵");
        setSkipColor(R.color.black);

        setVibrateEnabled(true);
        setVibrateIntensity(60);

        setCustomTypeFace(Typeface.createFromAsset(getAssets(), "bmhanna_11yrs_ttf.ttf"));
    }

    @Override
    protected Integer setLastItemBottomViewColor() {
        return R.color.colorAccent;
    }

    @Override
    protected void onSkipPressed(View view) {
        Log.d(TAG, "onSkipPressed: 스키이이입입입입!!");
    }

    @Override
    protected void onFragmentChanged(int position) {
        Log.d(TAG, "onFragmentChanged: 변경 : position" + position);
    }

    @Override
    protected void onDonePressed() {
//        Intent intent = new Intent(this, DeviceScanActivity.class);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
