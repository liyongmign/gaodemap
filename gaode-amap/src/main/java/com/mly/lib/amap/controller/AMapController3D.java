package com.mly.lib.amap.controller;

import com.amap.api.maps.MapView;

/**
 * Created by lym on 2018/1/23.
 * <p>
 * 地图控制接口 - 3D地图
 */

public interface AMapController3D extends AMapController {
    /**
     * @return 地图view
     */
    MapView getMapView();
}
