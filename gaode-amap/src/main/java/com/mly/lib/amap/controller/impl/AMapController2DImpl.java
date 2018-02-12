package com.mly.lib.amap.controller.impl;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.mly.lib.amap.R;
import com.mly.lib.amap.bean.MapCamera;
import com.mly.lib.amap.bean.MapLatLong;
import com.mly.lib.amap.controller.AMapController2D;

/**
 * Created by lym on 2018/1/23.
 * <p>
 * 地图控制接口实现 - 2D地图
 */

public class AMapController2DImpl extends BaseAMapController implements AMapController2D, AMap.OnMapLoadedListener, LocationSource, AMapLocationListener {
    // 地图view
    private MapView m2dMapView;
    private AMap m2dAMap;
    // 定位监听
    private OnLocationChangedListener m2dListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    // 显示定位按钮
    private boolean showMyLcButton = true;

    public AMapController2DImpl(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void setCurrentLatLong(AMapLocation aMapLocation) {
        MapLatLong mLocation = getMyLocation();
        if (mLocation == null) {
            mLocation = new MapLatLong();
        }
        mLocation.setAMapLocation(aMapLocation);
        setCurrentLatLong(mLocation);
    }

    /**
     * 显示当前定位
     */
    @Override
    protected void onShowMyLocation() {
        if (mLocationClient != null) {
            mLocationClient.startLocation();
        }
    }

    @Override
    public void showMyLocationButton(boolean show) {
        showMyLcButton = show;
        if (getAMap() != null) {
            getAMap().getUiSettings().setMyLocationButtonEnabled(show);
        }
    }

    @Override
    public void onRefreshMap() {
        if (getAMap() != null) {
            getAMap().invalidate();
        }
    }

    @Override
    public final MapView getMapView() {
        return m2dMapView;
    }

    @Nullable
    protected final AMap getAMap() {
        return m2dAMap;
    }

    @Override
    public View onCreateMapView(LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        m2dMapView = (MapView) inflater.inflate(R.layout.map_2d_view, container, false);
        return m2dMapView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 创建地图
        getMapView().onCreate(savedInstanceState);
        // 初始化地图
        if (m2dAMap == null) {
            m2dAMap = getMapView().getMap();
            // 初始化地图属性
            setUpMap();
        }
    }

    // 初始化地图属性
    @Override
    protected void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 设置小蓝点的图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromBitmap(getCurrentLcIcBmp()));
        // 设置圆形的边框颜色
        myLocationStyle.strokeColor(Color.BLACK);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));
        // 设置小蓝点的锚点
        // myLocationStyle.anchor(int,int)
        // 设置圆形的边框粗细
        myLocationStyle.strokeWidth(1.0f);
        m2dAMap.setMyLocationStyle(myLocationStyle);

        // 地图加载完成监听
        m2dAMap.setOnMapLoadedListener(this);
        // 设置定位监听
        m2dAMap.setLocationSource(this);

        UiSettings uiSettings = m2dAMap.getUiSettings();
        // 设置默认定位按钮是否显示
        uiSettings.setMyLocationButtonEnabled(false);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        m2dAMap.setMyLocationEnabled(showMyLcButton);
        // 改变logo位置(右下方)
        //uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
    }

    @Override
    public void onDestroy() {
        // 销毁地图
        getMapView().onDestroy();
    }

    @Override
    public void onPause() {
        // 暂停地图绘制
        getMapView().onPause();
    }

    @Override
    public void onResume() {
        // 恢复地图绘制
        getMapView().onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存地图数据
        getMapView().onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        // 内存不足
        //getMapView().onLowMemory();
    }

    @Override
    public void onMapLoaded() {
        // 地图加载完成
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener m2dListener) {
        this.m2dListener = m2dListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getContext());
            // 设置定位监听
            mLocationClient.setLocationListener(this);

            mLocationOption = new AMapLocationClientOption();
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption
                    .AMapLocationMode.Hight_Accuracy);
            // 设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
        } else if (singleLocation()) {
            // 定位一次
            mLocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        m2dListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (m2dListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                m2dListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                // 记录当前定位
                setCurrentLatLong(aMapLocation);

                // 停止定位
                if (mLocationClient != null && singleLocation()) {
                    mLocationClient.stopLocation();

                    // 地图移到定位位置
                    changeCamera(aMapLocation);
                }
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": "
                        + aMapLocation.getErrorInfo();
                error(errText);
            }
        }
    }

    @Override
    protected void changeCamera(double lat, double lng) {
        changeCamera(new LatLng(lat, lng));
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     *
     * @param latLng 中心坐标
     */
    protected void changeCamera(LatLng latLng) {
        if (getAMap() == null) return;

        CameraUpdate update = CameraUpdateFactory.newCameraPosition(
                MapCamera.newCameraPosition2D(latLng));
        getAMap().moveCamera(update);

    }

}
