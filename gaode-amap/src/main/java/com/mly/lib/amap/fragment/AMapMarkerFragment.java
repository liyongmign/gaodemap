package com.mly.lib.amap.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mly.lib.amap.R;
import com.mly.lib.amap.bean.MapLatLong;
import com.mly.lib.amap.controller.AMapController;
import com.mly.lib.amap.controller.MapMarkerFace;
import com.mly.lib.amap.controller.impl.AMapMarkerController2D;

/**
 * 地图标记{@link Fragment}
 */
public class AMapMarkerFragment extends AMapLocationFragment
        implements MapMarkerFace {

    // 点标记控制器
    private MapMarkerFace mMarkerController;
    // 图标列表
    private int[] mMarkerIcons;
    // 标记列表
    private MapLatLong[] mMarkers;

    @Nullable
    @Override
    public View onCreateCustomView(LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amap_marker, container, false);
        // 当前定位
        final ImageView mLcView = view.findViewById(R.id.iv_my_location_button);
        mLcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示当前定位
                showMyLocation();
            }
        });
        return view;
    }

    @NonNull
    @Override
    protected AMapController createAMap2DController(Context context) {
        AMapMarkerController2D controller2D = new AMapMarkerController2D(context);
        mMarkerController = controller2D;
        return controller2D;
    }

    @NonNull
    @Override
    protected AMapController createAMap3DController(Context context) {
        // TODO: 2018/1/24
        AMapMarkerController2D controller3D = new AMapMarkerController2D(context);
        mMarkerController = controller3D;
        return controller3D;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 设置创建页面前设置的标记
        addMarkersBefore();
    }

    // 设置创建页面前设置的标记
    private void addMarkersBefore() {
        // 添加标记图标
        if (mMarkerIcons != null) {
            setMarkerIcons(mMarkerIcons);
        }
        // 添加标记
        if (mMarkers != null) {
            setMarkers(mMarkers);
        }
    }

    @Override
    public void setMarkerIcons(int... icons) {
        if (mMarkerController == null) {
            // 记录图标列表
            mMarkerIcons = icons;
            return;
        }
        mMarkerController.setMarkerIcons(icons);
        mMarkerIcons = null;
    }

    @Override
    public void setMarkers(MapLatLong... markers) {
        if (mMarkerController == null) {
            // 记录标记列表
            mMarkers = markers;
            return;
        }
        mMarkerController.setMarkers(markers);
        mMarkers = null;
    }

}
