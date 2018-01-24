package com.mly.lib.amap.controller.impl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Polygon;
import com.amap.api.maps2d.model.PolygonOptions;
import com.mly.lib.amap.bean.MapCamera;
import com.mly.lib.amap.bean.MapLatLong;
import com.mly.lib.amap.controller.MapAreaFace;

import java.util.ArrayList;
import java.util.List;

import static com.mly.lib.amap.bean.MapConfig.AREA_COLOR;
import static com.mly.lib.amap.bean.MapConfig.AREA_STROKE_COLOR;
import static com.mly.lib.amap.bean.MapConfig.AREA_STROKE_WIDTH;

/**
 * Created by lym on 2018/1/24.
 * <p>
 * 地图区域绘制
 */

public class AMapAreaController2DImpl extends AMapController2DImpl
        implements MapAreaFace {
    // 区域
    private Polygon mPolygon;
    // 区域颜色
    private int mAreaColor = AREA_COLOR;
    // 区域边框颜色
    private int mAreaStrokeColor = AREA_STROKE_COLOR;
    // 区域边框宽度
    private float mAreaStrokeWidth = AREA_STROKE_WIDTH;

    public AMapAreaController2DImpl(@NonNull Context context) {
        super(context);
    }

    @Override
    public void setMapAreaColor(int color) {
        mAreaColor = color;
    }

    @Override
    public void setMapAreaStrokeColor(int color) {
        mAreaStrokeColor = color;
    }

    @Override
    public void setMapAreaStrokeWidth(float strokeWidth) {
        mAreaStrokeWidth = strokeWidth;
    }

    @Override
    public void setMapArea(MapLatLong... area) {
        // 清除就区域
        clearPolygon();
        if (area == null) return;
        AMap aMap;
        if ((aMap = getAMap()) == null) return;
        // 添加区域
        List<LatLng> areaList = createAreaList(area);
        PolygonOptions options = new PolygonOptions()
                .addAll(areaList)
                .fillColor(mAreaColor)
                .strokeColor(mAreaStrokeColor)
                .strokeWidth(mAreaStrokeWidth);
        mPolygon = aMap.addPolygon(options);

          // 显示地图区域
        LatLng latLng = null;
        MapLatLong c;
        if ((c = getMyLocation()) != null) {
            latLng = new LatLng(c.getLatitude(), c.getLongitude());
        }
        changeCamera(latLng);
    }

    // 创建区域列表
    private List<LatLng> createAreaList(@NonNull MapLatLong[] area) {
        List<LatLng> latLngs = new ArrayList<>();
        for (MapLatLong anArea : area) {
            latLngs.add(anArea.toLatLong2d());
        }
        return latLngs;
    }

    // 清除就区域
    private void clearPolygon() {
        if (mPolygon != null) {
            mPolygon.remove();
            mPolygon = null;
        }
    }

    @Override
    protected void changeCamera(LatLng latLng) {
        if (getAMap() == null) return;

        final CameraUpdate update;
        List<LatLng> latLngs;
        if (mPolygon == null || (latLngs = mPolygon.getPoints()) == null
                || latLngs.size() == 0) {
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
