# ViewPager2Banner
## Introduction

使用 `ViewPager2` 实现无限轮播效果，可以用来实现 banner 以及上下滚动文字广告等。

## Screenshots

1. MultiplePagerScaleInTransformer

![viewpager2banner](assets/viewpager2banner.gif)

2. ZoomOutPageTransformer

![viewpager2banner_zoom](assets/viewpager2banner_zoom.gif)

3. DepthPageTransformer

![viewpager2banner_depth](assets/viewpager2banner_depth.gif)

## Features

* 支持无限自动轮播
* 支持水平竖直方向
* 支持圆点指示符及自定义
* 支持一屏显示 3 个 item 的切换效果
* 支持 各种动画切换 效果
    AccordionPageTransformer();
    AlphaPageTransformer();
    CubePageTransformer();
    DefaultPageTransformer();
    DepthPageTransformer());
    FlipCenterPageTransformer();
    FlipPageTransformer();
    NGGuidePageTransformer();
    RotatePageTransformer();
    ScalePageTransformer();
    StackPageTransformer();
    ZoomCenterPageTransformer();
    ZoomFadePageTransformer();
    ZoomPageTransformer();
    ZoomStackPageTransformer();
    ZoomOutPageTransformer();

## Getting started

在项目的根节点的 `build.gradle` 中添加如下代码
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

在项目的 `build.gradle` 中添加
```
dependencies {
    implementation 'com.github.Kebiiii:ViewPager2Banner:1.0.1'
}
```

**注意：还需要自行添加 ViewPager2 的依赖**。如：
```
dependencies {
    implementation "androidx.viewpager2:viewpager2:1.0.0"
}
``` 

ViewPager2 最新版本请点击[**此处**](https://developer.android.com/jetpack/androidx/releases/viewpager2)

## Usage

1. 在 XML 布局文件中：
```
<com.kebiiii.viewpager2banner.ViewPager2Banner
        android:id="@+id/banner"
        android:layout_width="0dp"
        android:layout_height="250dp"
        android:layout_marginTop="16dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
```

2. 继承 `ViewPager2BannerAdapter` 或 `ViewPager2BannerFragmentAdapter` 实现相关方法，跟 `RecyclerView` 的 `Adapter` 基本类似，如：
```
private inner class MyCyclePagerAdapter : ViewPager2BannerAdapter<PagerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return PagerViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_pager, parent, false)
        )
    }

    override fun getRealItemCount(): Int = items.size

    override fun onBindRealViewHolder(holder: PagerViewHolder, position: Int) {
        holder.ivPager.setImageResource(items[position])
        holder.tvTitle.text = position.toString()
    }
}

private inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var ivPager: ImageView = itemView.findViewById(R.id.iv_pager)
    var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
} 
```

3. 通过 `ViewPager2Banner` 设置相关参数。
``` 
//设置 adapter，此 adapter 必须是继承自 ViewPager2BannerAdapter 或 ViewPager2BannerFragmentAdapter
viewPager2Banner.setAdapter(adapter)
//设置指示符
viewPager2Banner.setIndicator(indicator)
//设置自动轮播间隔
viewPager2Banner.setAutoTurning(autoTurningTime)
//设置切换效果，可以多个效果组合
viewPager2Banner.setPageTransformer(compositePageTransformer)
//添加间距
viewPager2Banner.addItemDecoration(itemDecoration)
//添加切换监听
viewPager2Banner.registerOnPageChangeCallback(pageChangeCallback)

viewPager2Banner.setOffscreenPageLimit(limit)

viewPager2Banner.setOrientation(orientation)
```

或者使用 `ViewPager2BannerHelper` 方式。
``` 
ViewPager2BannerHelper(banner)
            .setAdapter(adapter)
            .setMultiplePagerScaleInTransformer(
                nextItemVisiblePx.toInt(),
                currentItemHorizontalMarginPx.toInt(),
                0.1f
            )
            .setDotsIndicator(
                dotsRadius,
                Color.RED,
                Color.WHITE,
                dotsPadding,
                0,
                dotsBottomMargin.toInt(),
                0,
                DotsIndicator.Direction.CENTER
            )
            .setAutoTurning(3000L)
            .build()
```

4. 更多特殊效果

* 指示符（Indicator），目前库中仅简单实现了圆点的指示符 `DotsIndicator`，更多的效果可以实现 `Indicator` 接口自定义，具体可以参考 `DotsIndicator` 的实现。
* `MultiplePagerScaleInTransformer`，实现了一屏多个的效果，这个效果需要配合 `MarginItemDecoration` 来使用（也可以通过设置 item 的间距达到相同的效果）。
* 更多的切换效果（PageTransformer），需要自己实现 `ViewPager2.PageTransformer`。

## License

Apache License, Version 2.0