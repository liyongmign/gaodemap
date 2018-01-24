package com.mly.lib.amap.controller;

import com.amap.api.maps2d.MapView;

/**
 * Created by lym on 2018/1/23.
 * <p>
 * 地图控制接口 - 2D地图
 */

public interface AMapController2D extends AMapController {
    /**
     * @return 地图view
     */
    MapView getMapView();
}
