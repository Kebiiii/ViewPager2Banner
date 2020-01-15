package com.pothead.viewpager2banner.util;

/**
 * Created by Pothead on 2019-12-17.
 */
public class BannerPositionUtil {

    public static int getRealPosition(int position, int realItemCount) {
        int fixPosition;
        if (position == 0) {
            fixPosition = realItemCount - 1;
        } else if (position == realItemCount + 1) {
            fixPosition = 0;
        } else {
            fixPosition = position - 1;
        }
        return fixPosition;
    }
}
