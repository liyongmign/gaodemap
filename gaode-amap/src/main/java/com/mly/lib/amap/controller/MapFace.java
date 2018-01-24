package com.mly.lib.amap.controller;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

import com.mly.lib.amap.bean.MapLatLong;

/**
 * Created by lym on 2018/1/23.
 * <p>
 * 基础地图接口
 */

public interface MapFace {

    /**
     * 设置定位图标，未设置使用默认值
     *
     * @param icResId 图标
     */
    void setMyLocationIcon(@DrawableRes int icResId);

    /**
     * 显示当前定位
     */
    void showMyLocation();

    /**
     * 显示当前定位
     *
     * @param single 只定位一次，成功后通知定位
     */
    void showMyLocation(boolean single);

    /**
     * @return 当前定位信息
     */
    @Nullable
    MapLatLong getMyLocation();

    /**
     * 刷新地图
     */
    void onRefreshMap();

}

