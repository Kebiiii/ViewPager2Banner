package com.pothead.viewpager2banner.indicator;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * Created by Pothead on 2019-12-04.
 */
public interface Indicator {

    @NonNull
    View getIndicatorView();

    void onChanged(int itemCount, int currentPosition);

    void onPageSelected(int position);

    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageScrollStateChanged(int state);
}
