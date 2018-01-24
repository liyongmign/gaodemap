package com.mly.lib.amap.controller;

import android.support.annotation.ColorInt;

import com.mly.lib.amap.bean.MapLatLong;

/**
 * Created by lym on 2018/1/24.
 * <p>
 * 地图轨迹
 */

public interface MapPolylineFace {
    /**
     * 设置起点图标
     *
     * @param mStartIcon 起点图标
     */
    void setPolylineStartIcon(int mStartIcon);

    /**
     * 设置终点图标
     *
     * @param mEndIcon 终点图标
     */
    void setPolylineEndIcon(int mEndIcon);

    /**
     * 显示轨迹起点位置
     */
    void showPolylineStart();

    /**
     * 显示轨迹终点位置
     */
    void showPolylineEnd();

    /**
     * 设置轨迹颜色
     *
     * @param color 轨迹颜色
     */
    void setPolylineColor(@ColorInt int color);

    /**
     * 设置轨迹线条宽度
     *
     * @param polylineWidth 轨迹线条宽度，dp值
     */
    void setPolylineWidth(float polylineWidth);

    /**
     * 设置轨迹列表
     *
     * @param polyline 轨迹坐标列表
     */
    void setPolyline(MapLatLong... polyline);

}
