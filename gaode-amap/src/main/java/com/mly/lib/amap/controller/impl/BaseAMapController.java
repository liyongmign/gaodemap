package com.mly.lib.amap.controller.impl;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.mly.lib.amap.R;
import com.mly.lib.amap.bean.MapLatLong;
import com.mly.lib.amap.controller.AMapController;

/**
 * Created by lym on 2018/1/23.
 * <p>
 * 地图控制基础类
 */
public abstract class BaseAMapController implements AMapController {
    // 布局解析器
    private final Context context;

    // 当前定位图标
    @DrawableRes
    private int mCurrentLcIcon;
    private Bitmap mCurrentLcIcBmp;

    // 当前定位
    private MapLatLong mCurrentLatLong;
    // 显示当前定位，单次定位
    private boolean singleLocation = true;

    protected BaseAMapController(@NonNull Context context) {
        this.context = context;
    }

    /**
     * @return {@link Context}
     */
    @NonNull
    protected final Context getContext() {
        return context;
    }

    /**
     * @return {@link Resources}
     */
    protected final Resources getResources() {
        return getContext().getResources();
    }

    // 当前定位图标图片
    @Nullable
    protected Bitmap getCurrentLcIcBmp() {
        if (mCurrentLcIcBmp == null) {
            try {
                mCurrentLcIcBmp = BitmapFactory.decodeResource(
                        getResources(), mCurrentLcIcon);

                if (mCurrentLcIcBmp == null) {
                    mCurrentLcIcBmp = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.ic_amp_my_location);
                }
            } catch (IllegalArgumentException ignore) {
            }
        }
        return mCurrentLcIcBmp;
    }

    @Nullable
    @Override
    public MapLatLong getMyLocation() {
        return mCurrentLatLong;
    }

    /**
     * 记录当前定位
     *
     * @param mCurrentLatLong 当前定位
     */
    protected final void setCurrentLatLong(@Nullable MapLatLong mCurrentLatLong) {
        this.mCurrentLatLong = mCurrentLatLong;
    }

    /**
     * 记录当前定位
     *
     * @param aMapLocation 当前定位
     */
    protected abstract void setCurrentLatLong(AMapLocation aMapLocation);

    @Override
    public void setMyLocationIcon(int icResId) {
        mCurrentLcIcon = icResId;
        // 刷新地图
        onRefreshMap();
    }

    @Override
    public void showMyLocation() {
        showMyLocation(true);
    }

    @Override
    public void showMyLocation(boolean single) {
        this.singleLocation = single;

        MapLatLong latLong;
        if (single || (latLong = getMyLocation()) == null) {
            // 显示当前定位
            onShowMyLocation();
        } else {
            // 地图中心移到当前位置
            changeCamera(latLong.getLatitude(), latLong.getLatitude());
        }
    }

    // 显示当前定位
    protected abstract void onShowMyLocation();

    // 定位成功后停止定位
    protected final boolean singleLocation() {
        return singleLocation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            singleLocation = savedInstanceState.getBoolean("singleLocation");
            // 恢复图标
            mCurrentLcIcon = savedInstanceState.getInt("mCurrentLcIcon",
                    0);
            // 恢复定位数据
            mCurrentLatLong = savedInstanceState.getParcelable("mCurrentLatLong");
        }
    }

    // 初始化地图属性
    protected abstract void setUpMap();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("singleLocation", singleLocation);
        // 保存图标
        outState.putInt("mCurrentLcIcon", mCurrentLcIcon);
        // 保存定位
        outState.putParcelable("mCurrentLatLong", mCurrentLatLong);
    }

    // 答应错误日志
    protected final void error(String errorMsg) {
        if (errorMsg == null) {
            errorMsg = "null";
        }
        Log.e(getClass().getSimpleName(), errorMsg);
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     *
     * @param aMapLocation 坐标
     */
    protected final void changeCamera(AMapLocation aMapLocation) {
        changeCamera(aMapLocation.getLatitude(), aMapLocation.getLongitude());
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     *
     * @param lat 经度
     * @param lng 纬度
     */
    protected abstract void changeCamera(double lat, double lng);

}
