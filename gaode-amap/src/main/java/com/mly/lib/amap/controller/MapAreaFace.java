package com.mly.lib.amap.controller;

import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;

import com.mly.lib.amap.bean.MapLatLong;

/**
 * Created by lym on 2018/1/24.
 * <p>
 * 地图区域显示
 */

public interface MapAreaFace {
    /**
     * @param color 区域颜色
     */
    void setMapAreaColor(@ColorInt int color);

    /**
     * @param color 区域边框颜色
     */
    void setMapAreaStrokeColor(@ColorInt int color);

    /**
     * @param strokeWidth 区域边框宽度
     */
    void setMapAreaStrokeWidth(@FloatRange(from = 0, fromInclusive = false) float strokeWidth);

    /**
     * 设置绘制地图区域
     *
     * @param area 区域坐标组
     */
    void setMapArea(MapLatLong... area);
}
