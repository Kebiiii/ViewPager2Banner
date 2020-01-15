package com.kebiiii.viewpager2banner.transformer;

import android.view.View;

public class NGGuidePageTransformer extends BasePageTransformer {
    private static final float MIN_ALPHA = 0.0f;    //最小透明度

    @Override
    public void handleInvisiblePage(View view, float position) {

    }

    @Override
    public void handleLeftPage(View view, float position) {
        int pageWidth = view.getWidth();    //得到view宽
        //消失的页面
        view.setTranslationX(-pageWidth * position);  //阻止消失页面的滑动
        // Fade the page relative to its size.
        float alphaFactor = Math.max(MIN_ALPHA, 1 - Math.abs(position));
        //透明度改变Log
        view.setAlpha(alphaFactor);
    }

    @Override
    public void handleRightPage(View view, float position) {
        int pageWidth = view.getWidth();    //得到view宽
        //出现的页面
        view.setTranslationX(pageWidth);        //直接设置出现的页面到底
        view.setTranslationX(-pageWidth * position);  //阻止出现页面的滑动
        // Fade the page relative to its size.
        float alphaFactor = Math.max(MIN_ALPHA, 1 - Math.abs(position));
        //透明度改变Log
        view.setAlpha(alphaFactor);
    }
}
