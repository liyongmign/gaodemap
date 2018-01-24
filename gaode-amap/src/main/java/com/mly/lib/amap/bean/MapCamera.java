package com.mly.lib.amap.bean;

import android.support.annotation.NonNull;

import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;

/**
 * Created by lym on 2018/1/24.
 * <p>
 * 地图相关设置
 */

public class MapCamera {
    public final static float DEF_ZOOM = 15;
    public final static float DEF_TILT = 24;
    public final static float DEF_BEARING = 20;

    private static float zoom;
    private static float tilt;
    private static float bearing;

    public static void setZoom(float zoom) {
        MapCamera.zoom = zoom;
    }

    public static float getZoom() {
        return zoom <= 0 ? DEF_ZOOM : zoom;
    }

    public static void setTilt(float tilt) {
        MapCamera.tilt = tilt;
    }

    public static float getTilt() {
        return tilt <= 0 ? DEF_TILT : tilt;
    }

    public static void setBearing(float bearing) {
        MapCamera.bearing = bearing;
    }

    public static float getBearing() {
        return bearing <= 0 ? DEF_BEARING : bearing;
    }

    public static com.amap.api.maps2d.model.CameraPosition
    newCameraPosition2D(@NonNull com.amap.api.maps2d.model.LatLng latLng) {
        return new com.amap.api.maps2d.model.CameraPosition(
                latLng, getZoom(), getTilt(), getBearing());
    }

    public static CameraPosition newCameraPosition3D(@NonNull LatLng latLng) {
        return new CameraPosition(latLng, getZoom(), getTilt(), getBearing());
    }

}
