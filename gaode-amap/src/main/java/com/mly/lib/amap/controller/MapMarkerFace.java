package com.mly.lib.amap.controller;

import com.mly.lib.amap.bean.MapLatLong;

/**
 * Created by lym on 2018/1/23.
 * <p>
 * 绘制地图标记接口
 */

public interface MapMarkerFace {
    /**
     * 设置点标记图标
     *
     * @param icons 图片列表，根据标记绘制顺序选取，没有则使用默认值
     */
    void setMarkerIcons(int... icons);

    /**
     * 设置点标记
     *
     * @param markers 点标记列表
     */
    void setMarkers(MapLatLong... markers);

}
