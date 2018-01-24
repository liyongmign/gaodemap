package com.mly.lib.amap.controller.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.mly.lib.amap.R;
import com.mly.lib.amap.bean.MapCamera;
import com.mly.lib.amap.bean.MapLatLong;
import com.mly.lib.amap.controller.MapMarkerFace;
import com.mly.lib.amap.widget.AMapMarkerInfoView;

import java.util.ArrayList;

/**
 * Created by lym on 2018/1/23.
 * <p>
 * 2D地图添加点标记控制
 */

public class AMapMarkerController2D extends AMapController2DImpl
        implements MapMarkerFace {
    // 图标列表
    private SparseIntArray mMarkerIcons;
    // 标记列表
    private SparseArray<Marker> mMarkers;

    public AMapMarkerController2D(@NonNull Context context) {
        super(context);
        mMarkerIcons = new SparseIntArray();
        mMarkers = new SparseArray<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 读取保存数据
        if (savedInstanceState != null) {
            // 图标列表
            ArrayList<Integer> iconsList = savedInstanceState.getIntegerArrayList("mMarkerIcons");
            int index = 0;
            while (iconsList != null && index < iconsList.size()) {
                mMarkerIcons.put(index, iconsList.get(index));
                index++;
            }
            // 标记列表

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存数据
        // 图标列表
        ArrayList<Integer> iconsList = new ArrayList<>();
        for (int i = 0; i < mMarkerIcons.size(); i++) {
            iconsList.add(mMarkerIcons.valueAt(i));
        }
        outState.putIntegerArrayList("mMarkerIcons", iconsList);
    }

    @Override
    public void setMarkerIcons(int... icons) {
        if (icons == null) return;
        mMarkerIcons.clear();

        int index = 0;
        for (int ic : icons) {
            mMarkerIcons.put(index, ic);
            index++;
        }
    }

    @Override
    public void setMarkers(MapLatLong... markers) {
        // 清空旧的标记
        clearMarkers();
        if (markers == null) return;
        // 添加标记
        MapLatLong c = null;
        for (int i = 0; i < markers.length; i++) {
            MapLatLong marker = markers[i];
            addMarker(marker, i);

            if (marker != null) {
                c = marker;
            }
        }
        // 显示最后添加的一个标记作为中心点
        if (c != null) {
            changeCamera(c.getLatitude(), c.getLongitude());
        }
        // marker点击事件
        setMarkerClickListener();
        // 显示最后一个添加的点标记弹窗
        showLastMarkerInfoWindow();
    }

    private void showLastMarkerInfoWindow() {
        Marker marker = null;
        for (int i = mMarkers.size() - 1; marker == null && i >= 0; i--) {
            Marker m = mMarkers.valueAt(i);
            if (m != null && m.isVisible()) {
                marker = m;
            }
        }
        // 显示指定标记弹窗
        showMarkerInfoWindow(marker);
    }

    // marker点击事件
    private void setMarkerClickListener() {
        if (getAMap() != null) {
            // 点标记自定义弹窗
            getAMap().setInfoWindowAdapter(infoWindowAdapter);
            // 点标记点击事件
            getAMap().setOnMarkerClickListener(mMarkerClickListener);
        }
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

    // 添加标记
    private void addMarker(@Nullable MapLatLong mapLatLong, int position) {
        if (mapLatLong == null) return;
        AMap aMap;
        if ((aMap = getAMap()) == null) return;

        // 添加标记
        MarkerOptions markerOptions = new MarkerOptions()
                // 坐标
                .position(mapLatLong.toLatLong2d())
                // 图标
                .icon(BitmapDescriptorFactory.fromResource(getMarkerIcon(position)))
                // 标题
                .title(mapLatLong.getName())
                // 点击弹窗信息
                .snippet(mapLatLong.getInfo())
                .visible(true);
        Marker marker = aMap.addMarker(markerOptions);
        // 记录标记
        mMarkers.put(position, marker);
    }

    // 点标记图标
    private int getMarkerIcon(int position) {
        int ic = mMarkerIcons.get(position);
        return ic != 0 ? ic : R.mipmap.ic_amp_marker;
    }

    // 清空标记
    private void clearMarkers() {
        while (mMarkers.size() > 0) {
            Marker marker = mMarkers.valueAt(0);
            if (marker != null) {
                marker.remove();
            }
            mMarkers.removeAt(0);
        }
        mMarkers.clear();
    }

    @Override
    protected void changeCamera(LatLng latLng) {
        if (getAMap() == null) return;

        final CameraUpdate update;
        if (mMarkers.size() == 0) {
            update = CameraUpdateFactory.newCameraPosition(
                    MapCamera.newCameraPosition2D(latLng));
        } else {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            // 当前定位
            if (latLng != null) {
                builder.include(latLng);
            }
            // 标记定位
            for (int i = 0; i < mMarkers.size(); i++) {
                Marker marker = mMarkers.valueAt(i);
                // 添加坐标
                if (marker != null && marker.isVisible()) {
                    builder.include(marker.getPosition());
                }
            }
            update = CameraUpdateFactory.newLatLngBounds(
                    builder.build(), 0);
        }
        getAMap().moveCamera(update);
    }

}
