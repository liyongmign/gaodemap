package com.mly.lib.amap.utils;

import android.Manifest;
import android.app.Activity;

/**
 * Created by lym on 2018/1/24.
 * <p>
 * 地图工具类
 */

public class MapUtils {
    private static MapUtils instance;

    private MapUtils() {
    }

    private static MapUtils getInstance() {
        if (instance == null) {
            synchronized (MapUtils.class) {
                if (instance == null) {
                    instance = new MapUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 地图使用权限请求
     * @param activity
     */
    public static void chechMapPermission(Activity activity) {
        String[] permissions=new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        };
    }

    /**
     * 地图使用权限请求监听
     */
    public interface OnMapPermissionListener {
        /**
         * 地图权限通过
         */
        void onMapPermissionChecked();

        /**
         * 地图权限不通过
         */
        void onMapPermissionRefused();
    }

}
