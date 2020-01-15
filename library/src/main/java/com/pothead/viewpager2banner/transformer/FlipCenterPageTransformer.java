package com.pothead.viewpager2banner.transformer;

import android.view.View;

public class FlipCenterPageTransformer extends BasePageTransformer {
    private static final float ROTATION = 90.0f;

    @Override
    public void handleInvisiblePage(View view, float position) {

    }

    @Override
    public void handleLeftPage(View view, float position) {
        view.setTranslationX(-view.getWidth() * position);
        float rotation = (ROTATION * position);
        view.setRotationY(rotation);

        if (position > -0.5) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void handleRightPage(View view, float position) {
        view.setTranslationX(-view.getWidth() * position);
        float rotation = (ROTATION * position);
        view.setRotationY(rotation);

        if (position < 0.5) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }
}
