package com.mly.lib.amap.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lym on 2018/1/23.
 * <p>
 * 地图控制接口
 */

public interface AMapController extends MapFace {
    /**
     * 创建地图view
     *
     * @param inflater           {@link LayoutInflater}
     * @param container          容器
     * @param savedInstanceState {@link Bundle}
     * @return 地图view
     */
    View onCreateMapView(LayoutInflater inflater, @NonNull ViewGroup container,
                         @Nullable Bundle savedInstanceState);

    /**
     * 创建地图
     */
    void onCreate(@Nullable Bundle savedInstanceState);

    /**
     * 销毁地图
     */
    void onDestroy();

    /**
     * 暂停地图的绘制
     */
    void onPause();

    /**
     * 重新绘制加载地图
     */
    void onResume();

    /**
     * 保存地图当前的状态
     */
    void onSaveInstanceState(Bundle outState);

    /**
     * 内存不足
     */
    void onLowMemory();
}
