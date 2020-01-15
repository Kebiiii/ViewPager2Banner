package com.pothead.viewpager2banner;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.pothead.viewpager2banner.indicator.DotsIndicator;
import com.pothead.viewpager2banner.indicator.Indicator;
import com.pothead.viewpager2banner.itemdecoration.MarginItemDecoration;
import com.pothead.viewpager2banner.transformer.MultiplePagerScaleInTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pothead on 2019-12-03.
 */
public class ViewPager2BannerHelper {
    private ViewPager2Banner viewPager2Banner;
    private RecyclerView.Adapter adapter;
    @ViewPager2.Orientation
    private int orientation = ViewPager2.ORIENTATION_HORIZONTAL;
    @ViewPager2.OffscreenPageLimit
    private int limit = 1;
    private CompositePageTransformer compositePageTransformer;
    private List<RecyclerView.ItemDecoration> itemDecorations;
    private List<ViewPager2.OnPageChangeCallback> pageChangeCallbacks;

    private long autoTurningTime;

    private Indicator indicator;

    public ViewPager2BannerHelper(@NonNull ViewPager2Banner viewPager2Banner) {
        this.viewPager2Banner = viewPager2Banner;
    }

    public ViewPager2BannerHelper setAdapter(@Nullable RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public ViewPager2BannerHelper setOrientation(@ViewPager2.Orientation int orientation) {
        this.orientation = orientation;
        return this;
    }

    public ViewPager2BannerHelper setOffscreenPageLimit(@ViewPager2.OffscreenPageLimit int limit) {
        this.limit = limit;
        return this;
    }

    public ViewPager2BannerHelper addPageTransformer(@NonNull ViewPager2.PageTransformer pageTransformer) {
        if (compositePageTransformer == null) {
            compositePageTransformer = new CompositePageTransformer();
        }
        compositePageTransformer.addTransformer(pageTransformer);
        return this;
    }

    private ViewPager2BannerHelper addItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration) {
        if (itemDecorations == null) {
            itemDecorations = new ArrayList<>();
        }
        itemDecorations.add(itemDecoration);
        return this;
    }

    public ViewPager2BannerHelper registerOnPageChangeCallback(@NonNull ViewPager2.OnPageChangeCallback callback) {
        if (pageChangeCallbacks == null) {
            pageChangeCallbacks = new ArrayList<>();
        }
        pageChangeCallbacks.add(callback);
        return this;
    }

    public ViewPager2BannerHelper setMultiplePagerScaleInTransformer(int nextItemVisiblePx,
                                                                     int currentItemHorizontalMarginPx,
                                                                     float scale) {
        addItemDecoration(new MarginItemDecoration(currentItemHorizontalMarginPx));
        addPageTransformer(new MultiplePagerScaleInTransformer(nextItemVisiblePx + currentItemHorizontalMarginPx, scale));
        return this;
    }

    public ViewPager2BannerHelper setAutoTurning(long autoTurningTime) {
        this.autoTurningTime = autoTurningTime;
        return this;
    }

    public ViewPager2BannerHelper setIndicator(@Nullable Indicator indicator) {
        this.indicator = indicator;
        return this;
    }

    public ViewPager2BannerHelper setDotsIndicator(float radius, @ColorInt int selectedColor,
                                                   @ColorInt int unSelectedColor, float dotsPadding,
                                                   int leftMargin, int bottomMargin, int rightMargin,
                                                   @DotsIndicator.Direction int direction) {
        DotsIndicator dotsIndicator = new DotsIndicator(viewPager2Banner.getContext());
        dotsIndicator.setRadius(radius);
        dotsIndicator.setSelectedColor(selectedColor);
        dotsIndicator.setUnSelectedColor(unSelectedColor);
        dotsIndicator.setDotsPadding(dotsPadding);
        dotsIndicator.setLeftMargin(leftMargin);
        dotsIndicator.setBottomMargin(bottomMargin);
        dotsIndicator.setRightMargin(rightMargin);
        dotsIndicator.setDirection(direction);
        this.indicator = dotsIndicator;
        return this;
    }

    public void build() {
        viewPager2Banner.setOrientation(orientation);
        viewPager2Banner.setOffscreenPageLimit(limit);

        if (adapter != null) {
            viewPager2Banner.setAdapter(adapter);
        }
        if (itemDecorations != null && !itemDecorations.isEmpty()) {
            for (RecyclerView.ItemDecoration itemDecoration : itemDecorations) {
                viewPager2Banner.addItemDecoration(itemDecoration);
            }
        }
        if (compositePageTransformer != null) {
            viewPager2Banner.setPageTransformer(compositePageTransformer);
        }
        if (pageChangeCallbacks != null && !pageChangeCallbacks.isEmpty()) {
            for (ViewPager2.OnPageChangeCallback pageChangeCallback : pageChangeCallbacks) {
                viewPager2Banner.registerOnPageChangeCallback(pageChangeCallback);
            }
        }
        viewPager2Banner.setIndicator(indicator);
        if (autoTurningTime > 0) {
            viewPager2Banner.setAutoTurning(autoTurningTime);
        }
    }
}
