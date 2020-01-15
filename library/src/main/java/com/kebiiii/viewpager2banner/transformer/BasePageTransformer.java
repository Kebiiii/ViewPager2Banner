package com.kebiiii.viewpager2banner.transformer;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

public abstract class BasePageTransformer implements ViewPager2.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        retView(view);
        if (position < -1.0f) {
            handleInvisiblePage(view, position);
        } else if (position <= 0.0f) {
            handleLeftPage(view, position);
        } else if (position <= 1.0f) {
            handleRightPage(view, position);
        } else {
            handleInvisiblePage(view, position);
        }
    }

    private void retView(View view) {
        view.setPivotX(0);
        view.setScaleX(1.0f);
        view.setPivotY(0);
        view.setScaleY(1.0f);
        view.setAlpha(1);
        view.setRotationX(0);
        view.setRotationY(0);
        view.setRotation(0);
        view.setTranslationX(0);
        view.setTranslationY(0);
    }

    public abstract void handleInvisiblePage(View view, float position);

    public abstract void handleLeftPage(View view, float position);

    public abstract void handleRightPage(View view, float position);

}