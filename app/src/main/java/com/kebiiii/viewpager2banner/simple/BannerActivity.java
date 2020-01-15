package com.kebiiii.viewpager2banner.simple;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.kebiiii.viewpager2banner.ViewPager2Banner;
import com.kebiiii.viewpager2banner.ViewPager2BannerHelper;
import com.kebiiii.viewpager2banner.adapter.ViewPager2BannerBaseAdapter;
import com.kebiiii.viewpager2banner.indicator.DotsIndicator;
import com.kebiiii.viewpager2banner.transformer.CubePageTransformer;
import com.kebiiii.viewpager2banner.util.BannerLogger;
import com.kebiiii.viewpager2banner.util.BannerPositionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BannerActivity extends AppCompatActivity {
    private String TAG = "MainActivity";

    private List<Integer> resList = Arrays.asList(R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four);
    private ViewPager2Banner banner;
    private List<Integer> bannerItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BannerLogger.setIsDebug(true);
        banner = findViewById(R.id.banner);
        setBannerData();
    }

    private void setBannerData() {
        initData();

        final MyViewPager2BannerAdapter adapter = new MyViewPager2BannerAdapter();

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerItems.add(resList.get(0));
                adapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.btn_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bannerItems.isEmpty()) {
                    int index = bannerItems.size() - 1;
                    bannerItems.remove(index);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        float nextItemVisiblePx = getResources().getDimension(R.dimen.viewpager_next_item_visible);
        float currentItemHorizontalMarginPx = getResources().getDimension(R.dimen.viewpager_current_item_horizontal_margin);
        float dotsRadius = getResources().getDimension(R.dimen.dots_radius);
        float dotsPadding = getResources().getDimension(R.dimen.dots_padding);
        float dotsBottomMargin = getResources().getDimension(R.dimen.dots_bottom_margin);

        /*new ViewPager2BannerHelper(banner)
                .setAdapter(adapter)
                .setMultiplePagerScaleInTransformer(
                        0,
                        0,
                        0.0f
                )
                .setDotsIndicator(
                        dotsRadius,
                        Color.RED,
                        Color.WHITE,
                        dotsPadding,
                        0,
                        (int) dotsBottomMargin,
                        0,
                        DotsIndicator.Direction.CENTER
                )
                .setAutoTurning(3000L)
                .build();*/
        new ViewPager2BannerHelper(banner)
                .setAdapter(adapter)
                .addPageTransformer(new CubePageTransformer())
                .setAutoTurning(3000L)
                .setDotsIndicator(
                        dotsRadius,
                        Color.RED,
                        Color.WHITE,
                        dotsPadding,
                        0,
                        (int) dotsBottomMargin,
                        0,
                        DotsIndicator.Direction.CENTER
                )
                .build();
//        transformers.add(new AccordionPageTransformer());
//        transformers.add(new AlphaPageTransformer());
//        transformers.add(new CubePageTransformer());
//        transformers.add(new DefaultPageTransformer());
//        transformers.add(new DepthPageTransformer());
//        transformers.add(new FlipCenterPageTransformer());
//        transformers.add(new FlipPageTransformer());
//        transformers.add(new NGGuidePageTransformer());
//        transformers.add(new RotatePageTransformer());
//        transformers.add(new ScalePageTransformer());
//        transformers.add(new StackPageTransformer());
//        transformers.add(new ZoomCenterPageTransformer());
//        transformers.add(new ZoomFadePageTransformer());
//        transformers.add(new ZoomPageTransformer());
//        transformers.add(new ZoomStackPageTransformer());
//        transformers.add(new ZoomOutPageTransformer());

        banner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d(TAG, "position： " + position +
                        " ,RealCurrentItem: " + banner.getRealCurrentItem() +
                        " ,CurrentItem: " + banner.getCurrentItem()+
                        " ,RealPosition: " + BannerPositionUtil.getRealPosition(position, ((ViewPager2BannerBaseAdapter)banner.getViewPager2().getAdapter()).getRealItemCount())+
                        " ,Count: "+banner.getViewPager2().getAdapter().getItemCount());

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == 0) {
                }
            }
        });
    }

    private void initData() {
        bannerItems.add(resList.get(0));
        bannerItems.add(resList.get(1));
        bannerItems.add(resList.get(2));
        bannerItems.add(resList.get(3));
    }

    private class MyViewPager2BannerAdapter extends ViewPager2BannerBaseAdapter<PagerViewHolder> {
        @Override
        public int getRealItemCount() {
            return bannerItems.size();
        }

        @Override
        public void onBindRealViewHolder(@NonNull PagerViewHolder holder, int position) {
            holder.ivPager.setImageResource(bannerItems.get(position));

        }

        @NonNull
        @Override
        public PagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_pager, parent, false);
            return new PagerViewHolder(view);
        }
    }

    private class PagerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPager;
        public PagerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPager = itemView.findViewById(R.id.iv_pager);
        }
    }

    /*private class TextCyclePagerAdapter extends ViewPager2BannerBaseAdapter<TextViewHolder> {
        @Override
        public int getRealItemCount() {
            return textList.size();
        }

        @Override
        public void onBindRealViewHolder(@NonNull TextViewHolder holder, int position) {
            holder.tvItemContent.setText(textList.get(position));
        }

        @NonNull
        @Override
        public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vertical_cycleviewpager, parent, false);
            return new TextViewHolder(view);
        }
    }

    private class TextViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemContent;
        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemContent = itemView.findViewById(R.id.tv_item_content);
        }
    }*/
}
