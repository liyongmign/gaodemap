package com.mly.lib.amap.controller.impl;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.mly.lib.amap.R;
import com.mly.lib.amap.bean.MapCamera;
import com.mly.lib.amap.bean.MapConfig;
import com.mly.lib.amap.bean.MapLatLong;
import com.mly.lib.amap.controller.MapPolylineFace;
import com.mly.lib.amap.widget.AMapMarkerInfoView;

import java.util.List;

/**
 * Created by lym on 2018/1/24.
 * <p>
 * 地图轨迹显示
 */

public class AMapPolylineController2D extends AMapController2DImpl
        implements MapPolylineFace {
    // 当前轨迹
    private Polyline mPolyline;
    // 轨迹颜色
    private int mPolylineColor = MapConfig.POLYLINE_COLOR;
    // 起点图标
    private int mStartIcon = 0;
    // 终点图标
    private int mEndIcon = 0;
    // 线条宽度
    private float mPolylineWidth = MapConfig.POLYLINE_WIDTH;
    // 起点
    private Marker mStartMarker;
    // 终点
    private Marker mEndMarker;
    // 移动到指定标记
    private boolean moveToMarker = false;

    public AMapPolylineController2D(@NonNull Context context) {
        super(context);
    }

    @Override
    public void setPolylineStartIcon(int mStartIcon) {
        this.mStartIcon = mStartIcon;
        if (mStartMarker != null) {
            mStartMarker.setIcon(BitmapDescriptorFactory.fromResource(mStartIcon));
        }
    }

    // 起点图标
    private int getStartIcon() {
        if (mStartIcon == 0) {
            return R.mipmap.ic_map_polyline_start;
        }
        return mStartIcon;
    }

    @Override
    public void setPolylineEndIcon(int mEndIcon) {
        this.mEndIcon = mEndIcon;
        if (mEndMarker != null) {
            mEndMarker.setIcon(BitmapDescriptorFactory.fromResource(mEndIcon));
        }
    }

    @Override
    public void showPolylineStart() {
        if (mStartMarker == null) return;
        moveToMarker = true;
        changeCamera(mStartMarker.getPosition());
    }

    @Override
    public void showPolylineEnd() {
        if (mEndMarker == null) return;
        moveToMarker = true;
        changeCamera(mEndMarker.getPosition());
    }

    @Override
    public void setPolylineColor(int color) {
        mPolylineColor = color;
        if (mPolyline != null) {
            mPolyline.setColor(color);
        }
    }

    @Override
    public void setPolylineWidth(float polylineWidth) {
        this.mPolylineWidth = polylineWidth;
    }

    // 轨迹线条宽度
    private float getPolylineWidth() {
        int width = (int) (getResources().getDisplayMetrics().density
                * (mPolylineWidth > 0 ? mPolylineWidth : MapConfig.POLYLINE_WIDTH)
                + 0.5f);
        return Math.max(1, width);
    }

    // 终点图标
    private int getEndIcon() {
        if (mEndIcon == 0) {
            return R.mipmap.ic_map_polyline_end;
        }
        return mEndIcon;
    }

    @Override
    public void setPolyline(MapLatLong... polyline) {
        // 清除轨迹
        clearPolyline();
        if (polyline == null) return;
        AMap aMap;
        if ((aMap = getAMap()) == null) return;
        // 添加轨迹
        PolylineOptions options = new PolylineOptions()
                .color(mPolylineColor)
                .width(getPolylineWidth());
        for (MapLatLong latLong : polyline) {
            if (latLong != null) {
                options.add(latLong.toLatLong2d());
            }
        }
        mPolyline = aMap.addPolyline(options);

        // 添加起点
        addStartMarker();
        // 添加终点
        addEndMarker();
        // 添加起点/终点点击事件
        addMarkerClickListener();

        // 显示地图区域轨迹
        LatLng latLng = null;
        MapLatLong c;
        if ((c = getMyLocation()) != null) {
            latLng = new LatLng(c.getLatitude(), c.getLongitude());
        }
        changeCamera(latLng);
    }

    // 添加起点/终点点击事件
    private void addMarkerClickListener() {
        AMap aMap;
        if ((aMap = getAMap()) == null) return;

        // 点标记自定义弹窗
        aMap.setInfoWindowAdapter(infoWindowAdapter);
        // 点标记点击事件
        aMap.setOnMarkerClickListener(mMarkerClickListener);
    }

    // 点标记自定义弹窗
    private final AMap.InfoWindowAdapter infoWindowAdapter
            = new AMap.InfoWindowAdapter() {
        AMapMarkerInfoView infoView;

        @Override
        public View getInfoWindow(Marker marker) {
            return getView(marker);
        }

        View getView(Marker marker) {
            if (infoView == null) {
                infoView = new AMapMarkerInfoView(getContext());
            }
            // 标题
            infoView.setTitle(marker.getTitle());
            // 描述
            infoView.setDescription(marker.getSnippet());
            return infoView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    };

    // 点标记点击事件
    private final AMap.OnMarkerClickListener mMarkerClickListener
            = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            // 显示指定标记弹窗
            return showMarkerInfoWindow(marker);
        }
    };

    // 显示指定标记弹窗
    private boolean showMarkerInfoWindow(Marker marker) {
        if (marker != null) {
            if (TextUtils.isEmpty(marker.getTitle())) {
                final String snippet = marker.getSnippet();
                if (!TextUtils.isEmpty(snippet)) {
                    // 将描述信息与标题切换，确保有标题，才可显示弹窗
                    marker.setTitle(snippet);
                    marker.setSnippet(null);
                }
            }
            // 显示弹窗
            marker.showInfoWindow();
        }
        return true;
    }

    // 添加起点
    private void addStartMarker() {
        List<LatLng> latLngs;
        if (mPolyline == null || (latLngs = mPolyline.getPoints()) == null
                || latLngs.isEmpty()) {
            return;//没有轨迹
        }
        LatLng latLng = latLngs.get(0);
        if (latLng == null) return;//没有起点
        // 添加起点
        mStartMarker = addMarker("起点", latLng, getStartIcon());
    }

    // 添加终点
    private void addEndMarker() {
        List<LatLng> latLngs;
        if (mPolyline == null || (latLngs = mPolyline.getPoints()) == null
                || latLngs.size() < 2) {
            return;//没有终点
        }
        LatLng latLng = latLngs.get(latLngs.size() - 1);
        if (latLng == null) return;//没有终点
        // 添加终点
        mEndMarker = addMarker("终点", latLng, getEndIcon());
    }

    // 添加标记
    @Nullable
    private Marker addMarker(String title, @NonNull LatLng latLng, @DrawableRes int ic) {
        AMap aMap;
        if ((aMap = getAMap()) == null) return null;
        return aMap.addMarker(new MarkerOptions()
                // 位置
                .position(latLng)
                // 标题
                .title(title)
                // 图标
                .icon(BitmapDescriptorFactory.fromResource(ic))
        );
    }

    // 清除轨迹
    private void clearPolyline() {
        // 轨迹
        if (mPolyline != null) {
            mPolyline.remove();
            mPolyline = null;
        }
        // 起点
        if (mStartMarker != null) {
            mStartMarker.remove();
            mStartMarker = null;
        }
        // 终点
        if (mEndMarker != null) {
            mEndMarker.remove();
            mEndMarker = null;
        }
    }

    @Override
    protected void changeCamera(LatLng latLng) {
        if (getAMap() == null) return;

        final CameraUpdate update;
        List<LatLng> latLngs;
        if (moveToMarker || mPolyline == null || (latLngs = mPolyline.getPoints()) == null
                || latLngs.size() == 0) {
            moveToMarker = false;
            update = CameraUpdateFactory.newCameraPosition(
                    MapCamera.newCameraPosition2D(latLng));
        } else {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            // 当前定位
            if (latLng != null) {
                builder.include(latLng);
            }
            // 标记定位
            for (int i = 0; i < latLngs.size(); i++) {
                LatLng lng = latLngs.get(i);
                // 添加坐标
                if (lng != null) {
                    builder.include(lng);
                }
            }
            update = CameraUpdateFactory.newLatLngBounds(
                    builder.build(), 0);
        }
        getAMap().moveCamera(update);
    }

}
