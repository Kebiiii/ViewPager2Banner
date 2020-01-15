package com.kebiiii.viewpager2banner.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.kebiiii.viewpager2banner.util.BannerPositionUtil;

import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.NO_ID;

/**
 * Created by Kebiiii on 2019-12-02.
 */
public abstract class ViewPager2BannerBaseFragmentAdapter extends FragmentStateAdapter {

    public ViewPager2BannerBaseFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public ViewPager2BannerBaseFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ViewPager2BannerBaseFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public final Fragment createFragment(int position) {
        return createRealFragment(BannerPositionUtil.getRealPosition(position, getRealItemCount()));
    }

    @Override
    public final int getItemCount() {
        return getRealItemCount() > 1 ? getRealItemCount() + 2 : getRealItemCount();
    }

    @Override
    public final long getItemId(int position) {
        return getRealItemId(BannerPositionUtil.getRealPosition(position, getRealItemCount()));
    }

    @Override
    public final void onBindViewHolder(@NonNull FragmentViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, BannerPositionUtil.getRealPosition(position, getRealItemCount()), payloads);
    }

    @Override
    public final int getItemViewType(int position) {
        return getRealItemViewType(BannerPositionUtil.getRealPosition(position, getRealItemCount()));
    }

    public abstract int getRealItemCount();

    @NonNull
    public abstract Fragment createRealFragment(int position);

    public int getRealItemViewType(int position) {
        return 0;
    }

    public long getRealItemId(int position) {
        return NO_ID;
    }

}
