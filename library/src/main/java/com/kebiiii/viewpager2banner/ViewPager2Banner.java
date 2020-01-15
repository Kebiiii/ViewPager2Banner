package com.kebiiii.viewpager2banner;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.kebiiii.viewpager2banner.adapter.ViewPager2BannerBaseAdapter;
import com.kebiiii.viewpager2banner.adapter.ViewPager2BannerBaseFragmentAdapter;
import com.kebiiii.viewpager2banner.indicator.Indicator;
import com.kebiiii.viewpager2banner.util.BannerLogger;

import java.lang.ref.WeakReference;
import java.util.Objects;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

/**
 * Created by Kebiiii on 2019-12-02.
 */
public class ViewPager2Banner extends FrameLayout {

    private ViewPager2 mViewPager2;

    private boolean canAutoTurning = false;
    private long autoTurningTime;
    private boolean isTurning = false;
    private AutoTurningRunnable mAutoTurningRunnable;

    private int mPendingCurrentItem = NO_POSITION;

    private RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            int itemCount = Objects.requireNonNull(getAdapter()).getItemCount();
            if (itemCount <= 1) {
                if (isTurning) {
                    stopAutoTurning();
                }
            } else {
                if (!isTurning) {
                    startAutoTurning();
                }
            }
            if (mIndicator != null) {
                mIndicator.onChanged(getPagerRealCount(), getIndexCurrentItem());
            }
        }
    };

    private Indicator mIndicator;

    public ViewPager2Banner(@NonNull Context context) {
        super(context);
        initialize(context, null);
    }

    public ViewPager2Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public ViewPager2Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ViewPager2Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs) {
        mViewPager2 = new ViewPager2(context);
        mViewPager2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mViewPager2.setOffscreenPageLimit(1);

        CycleOnPageChangeCallback mCycleOnPageChangeCallback = new CycleOnPageChangeCallback();
        mViewPager2.registerOnPageChangeCallback(mCycleOnPageChangeCallback);

        mAutoTurningRunnable = new AutoTurningRunnable(this);

        addView(mViewPager2);
    }

    public void setAutoTurning(long autoTurningTime) {
        setAutoTurning(true, autoTurningTime);
    }

    public void setAutoTurning(boolean canAutoTurning, long autoTurningTime) {
        this.canAutoTurning = canAutoTurning;
        this.autoTurningTime = autoTurningTime;
        stopAutoTurning();
        startAutoTurning();
    }

    public void startAutoTurning() {
        if (!canAutoTurning || autoTurningTime <= 0 || isTurning) return;
        isTurning = true;
        postDelayed(mAutoTurningRunnable, autoTurningTime);
    }

    public void stopAutoTurning() {
        isTurning = false;
        removeCallbacks(mAutoTurningRunnable);
    }

    public void setAdapter(@Nullable RecyclerView.Adapter adapter) {
        if (adapter instanceof ViewPager2BannerBaseAdapter || adapter instanceof ViewPager2BannerBaseFragmentAdapter) {
            if (mViewPager2.getAdapter() == adapter) return;
            adapter.registerAdapterDataObserver(mAdapterDataObserver);
            mViewPager2.setAdapter(adapter);
            setRealCurrentItem(2, false);
            initIndicator();
            return;
        }
        throw new IllegalArgumentException("adapter must be an instance of ViewPager2BannerBaseAdapter or ViewPager2BannerBaseFragmentAdapter");
    }

    @Nullable
    public RecyclerView.Adapter getAdapter() {
        return mViewPager2.getAdapter();
    }

    private int getPagerRealCount() {
        RecyclerView.Adapter adapter = getAdapter();
        if (adapter instanceof ViewPager2BannerBaseAdapter) {
            return ((ViewPager2BannerBaseAdapter) adapter).getRealItemCount();
        }
        if (adapter instanceof ViewPager2BannerBaseFragmentAdapter) {
            return ((ViewPager2BannerBaseFragmentAdapter) adapter).getRealItemCount();
        }
        return 0;
    }

    public void setOrientation(@ViewPager2.Orientation int orientation) {
        mViewPager2.setOrientation(orientation);
    }

    @ViewPager2.Orientation
    public int getOrientation() {
        return mViewPager2.getOrientation();
    }

    public void setPageTransformer(@Nullable ViewPager2.PageTransformer transformer) {
        mViewPager2.setPageTransformer(transformer);
    }

    public void addItemDecoration(@NonNull RecyclerView.ItemDecoration decor) {
        mViewPager2.addItemDecoration(decor);
    }

    public void addItemDecoration(@NonNull RecyclerView.ItemDecoration decor, int index) {
        mViewPager2.addItemDecoration(decor, index);
    }

    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        if (getPagerRealCount() >= 2) {
            item = (item + 2) % getPagerRealCount();
        }
        setRealCurrentItem(item, smoothScroll);
    }

    private void setRealCurrentItem(int item, boolean smoothScroll) {
        BannerLogger.d("setRealCurrentItem " + item);
        mViewPager2.setCurrentItem(item, smoothScroll);
        if (!smoothScroll && mIndicator != null) {
            mIndicator.onPageSelected(getIndexCurrentItem());
        }
    }

    public int getCurrentItem() {
        return mViewPager2.getCurrentItem();
    }

    public int getIndexCurrentItem() {
        int currentItem = getCurrentItem();
        return getPrePosition(currentItem);
    }
    public int getRealCurrentItem() {
        int currentItem = getCurrentItem();
        int realCurrentItem = getPrePosition(currentItem);
        realCurrentItem = getPrePosition(realCurrentItem);
//        realCurrentItem = getCurrentItem() >= 1 ? getCurrentItem() - 1 : getCurrentItem();
        return realCurrentItem;
    }

    private int getPrePosition(int currentPosition) {
        int lastPosition = currentPosition - 1;
        lastPosition = lastPosition >= 0 ? lastPosition : getPagerRealCount() - 1;
        return lastPosition > 0 ? lastPosition : 0;
    }

    public void setOffscreenPageLimit(@ViewPager2.OffscreenPageLimit int limit) {
        mViewPager2.setOffscreenPageLimit(limit);
    }

    public int getOffscreenPageLimit() {
        return mViewPager2.getOffscreenPageLimit();
    }

    public void registerOnPageChangeCallback(@NonNull ViewPager2.OnPageChangeCallback callback) {
        mViewPager2.registerOnPageChangeCallback(callback);
    }

    public void unregisterOnPageChangeCallback(@NonNull ViewPager2.OnPageChangeCallback callback) {
        mViewPager2.unregisterOnPageChangeCallback(callback);
    }

    @NonNull
    public ViewPager2 getViewPager2() {
        return mViewPager2;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            if (canAutoTurning && isTurning) {
                stopAutoTurning();
            }
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL ||
                action == MotionEvent.ACTION_OUTSIDE) {
            if (canAutoTurning) startAutoTurning();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAutoTurning();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoTurning();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.mCurrentItem = getCurrentItem();
        BannerLogger.d("onSaveInstanceState: " + ss.mCurrentItem);
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mPendingCurrentItem = ss.mCurrentItem;
        BannerLogger.d("onRestoreInstanceState: " + mPendingCurrentItem);
        restorePendingState();
    }

    private void restorePendingState() {
        if (mPendingCurrentItem == NO_POSITION) {
            // No state to restore, or state is already restored
            return;
        }
        int currentItem = Math.max(0, Math.min(mPendingCurrentItem, Objects.requireNonNull(getAdapter()).getItemCount() - 1));
        BannerLogger.d("restorePendingState: " + currentItem);
        mPendingCurrentItem = NO_POSITION;
        setRealCurrentItem(currentItem, false);
    }

    public void setIndicator(@Nullable Indicator indicator) {
        if (mIndicator == indicator) return;
        removeIndicatorView();
        mIndicator = indicator;
        initIndicator();
    }

    private void initIndicator() {
        if (mIndicator == null || getAdapter() == null) return;
        addView(mIndicator.getIndicatorView());
        mIndicator.onChanged(getPagerRealCount(), getIndexCurrentItem());
    }

    private void removeIndicatorView() {
        if (mIndicator == null) return;
        removeView(mIndicator.getIndicatorView());
    }

    //1.normal:
    //onPageScrollStateChanged(state=1) -> onPageScrolled... -> onPageScrollStateChanged(state=2)
    // -> onPageSelected -> onPageScrolled... -> onPageScrollStateChanged(state=0)
    //2.setRealCurrentItem(,true):
    //onPageScrollStateChanged(state=2) -> onPageSelected -> onPageScrolled... -> onPageScrollStateChanged(state=0)
    //3.other: no call onPageSelected()
    //onPageScrollStateChanged(state=1) -> onPageScrolled... -> onPageScrollStateChanged(state=2)
    // -> onPageScrolled... -> onPageScrollStateChanged(state=0)
    private class CycleOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
        private static final int INVALID_ITEM_POSITION = -1;
        private boolean isBeginPagerChange;
        private int mTempPosition = INVALID_ITEM_POSITION;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            BannerLogger.d("onPageScrolled: " + position + " positionOffset: " + positionOffset
                    + " positionOffsetPixels: " + positionOffsetPixels);
            if (mIndicator != null) {
                mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            BannerLogger.d("onPageSelected: " + position);
            if (isBeginPagerChange) {
                mTempPosition = position;
            }
            if (mIndicator != null) {
                mIndicator.onPageSelected(getIndexCurrentItem());
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            BannerLogger.d("onPageScrollStateChanged: state " + state);
            if (state == ViewPager2.SCROLL_STATE_DRAGGING ||
                    (isTurning && state == ViewPager2.SCROLL_STATE_SETTLING)) {
                isBeginPagerChange = true;
            } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                isBeginPagerChange = false;
                int fixCurrentItem = getFixCurrentItem(mTempPosition);
                if (fixCurrentItem != INVALID_ITEM_POSITION && fixCurrentItem != mTempPosition) {
                    mTempPosition = INVALID_ITEM_POSITION;
                    BannerLogger.d("onPageScrollStateChanged: fixCurrentItem " + fixCurrentItem);
                    setRealCurrentItem(fixCurrentItem, false);
                }
            }
            if (mIndicator != null) {
                mIndicator.onPageScrollStateChanged(state);
            }
        }

        private int getFixCurrentItem(final int position) {
            if (position == INVALID_ITEM_POSITION) return INVALID_ITEM_POSITION;
            final int lastPosition = Objects.requireNonNull(getAdapter()).getItemCount() - 1;
            int fixPosition = INVALID_ITEM_POSITION;
            if (position == 0) {
                fixPosition = lastPosition == 0 ? 0 : lastPosition - 1;
            } else if (position == lastPosition) {
                fixPosition = 1;
            }
            return fixPosition;
        }
    }

    static class AutoTurningRunnable implements Runnable {
        private final WeakReference<ViewPager2Banner> reference;

        AutoTurningRunnable(ViewPager2Banner viewPager2Banner) {
            this.reference = new WeakReference<>(viewPager2Banner);
        }

        @Override
        public void run() {
            ViewPager2Banner viewPager2Banner = reference.get();
            if (viewPager2Banner != null && viewPager2Banner.canAutoTurning && viewPager2Banner.isTurning) {
                int itemCount = Objects.requireNonNull(viewPager2Banner.getAdapter()).getItemCount();
                if (itemCount == 0) return;
                int nextItem = (viewPager2Banner.getCurrentItem() + 1) % itemCount;
                viewPager2Banner.setRealCurrentItem(nextItem, true);
                viewPager2Banner.postDelayed(viewPager2Banner.mAutoTurningRunnable, viewPager2Banner.autoTurningTime);
            }
        }
    }

    static class SavedState extends BaseSavedState {
        int mCurrentItem;

        SavedState(Parcel source) {
            super(source);
            readValues(source, null);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            readValues(source, loader);
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        private void readValues(Parcel source, ClassLoader loader) {
            mCurrentItem = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mCurrentItem);
        }

        public static final Creator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source, ClassLoader loader) {
                return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                        ? new SavedState(source, loader)
                        : new SavedState(source);
            }

            @Override
            public SavedState createFromParcel(Parcel source) {
                return createFromParcel(source, null);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
