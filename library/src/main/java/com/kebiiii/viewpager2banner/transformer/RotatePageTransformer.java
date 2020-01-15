package com.kebiiii.viewpager2banner.transformer;

import android.view.View;

public class RotatePageTransformer extends BasePageTransformer {
    private float mMaxRotation = 15.0f;

    public RotatePageTransformer() {
    }

    public RotatePageTransformer(float maxRotation) {
        setMaxRotation(maxRotation);
    }

    @Override
    public void handleInvisiblePage(View view, float position) {
        view.setPivotX(view.getMeasuredWidth() * 0.5f);
        view.setPivotY(view.getMeasuredHeight());
        view.setRotation( 0);
    }

    @Override
    public void handleLeftPage(View view, float position) {
        float rotation = (mMaxRotation * position);
        view.setPivotX( view.getMeasuredWidth() * 0.5f);
        view.setPivotY(view.getMeasuredHeight());
        view.setRotation( rotation);
    }

    @Override
    public void handleRightPage(View view, float position) {
        handleLeftPage(view, position);
    }

    public void setMaxRotation(float maxRotation) {
        if (maxRotation >= 0.0f && maxRotation <= 40.0f) {
            mMaxRotation = maxRotation;
        }
    }
}