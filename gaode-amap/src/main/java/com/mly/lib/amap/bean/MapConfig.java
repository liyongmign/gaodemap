package com.mly.lib.amap.bean;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.model.LatLng;
import com.mly.lib.amap.fragment.AMapLocationFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lym on 2017/12/6.
 * <p>
 * 地图相关配置
 */

@SuppressWarnings("WeakerAccess")
public class MapConfig {
    public static final String UNKNOWN = "未知";

    /**
     * 默认高德坐标
     */
    public static final int DEFAULT = 0;
    /**
     * 百度坐标
     */
    public static final int BAIDU = 1;
    /**
     * 图吧坐标
     */
    public static final int MAPBAR = 2;
    /**
     * 图盟坐标
     */
    public static final int MAPABC = 3;
    /**
     * 搜搜坐标
     */
    public static final int SOSOMAP = 4;
    /**
     * 阿里云坐标
     */
    public static final int ALIYU = 5;
    /**
     * 谷歌坐标
     */
    public static final int GOOGLE = 6;
    /**
     * GPS坐标
     */
    public static final int GPS = 7;

    /**
     * 默认区域颜色
     */
    @ColorInt
    public static final int AREA_COLOR = 0x6C2A7CFF;
    /**
     * 默认区域边框颜色
     */
    @ColorInt
    public static final int AREA_STROKE_COLOR = 0xFF2A7CFF;
    /**
     * 默认区域边框宽度
     */
    public static final float AREA_STROKE_WIDTH = 1;
    /**
     * 默认轨迹线条颜色
     */
    @ColorInt
    public static final int POLYLINE_COLOR = 0xFF2A7CFF;
    /**
     * 默认轨迹线条宽度
     */
    public static final float POLYLINE_WIDTH = 2;

    /**
     * 坐标类型
     */
    @IntDef({DEFAULT, BAIDU, MAPBAR, MAPABC, SOSOMAP, ALIYU, GOOGLE, GPS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CoordType {
    }

    static SparseArray<String> mCoordTypeValues
            = new SparseArray<>();

    static {
        mCoordTypeValues.put(DEFAULT, "高德地图坐标");
        mCoordTypeValues.put(BAIDU, "百度地图坐标");
        mCoordTypeValues.put(MAPBAR, "图吧地图坐标");
        mCoordTypeValues.put(MAPABC, "图盟地图坐标");
        mCoordTypeValues.put(SOSOMAP, "搜搜地图坐标");
        mCoordTypeValues.put(ALIYU, "阿里云地图坐标");
        mCoordTypeValues.put(GOOGLE, "谷歌地图坐标");
        mCoordTypeValues.put(GPS, "GPS定位坐标");
    }

    // 坐标类型
    public static String getCoordTypeValue(@CoordType int coordType) {
        return mCoordTypeValues.get(coordType);
    }

    // 坐标类型转换
    @CoordType
    public static int checkToCoordType(String coordType) {
        try {
            return checkToCoordType(Integer.valueOf(coordType));
        } catch (NumberFormatException ignore) {
        }
        return DEFAULT;
    }

    // 坐标类型转换
    @CoordType
    public static int checkToCoordType(int coordType) {
        switch (coordType) {
            case DEFAULT:
            case ALIYU:
            case BAIDU:
            case GOOGLE:
            case GPS:
            case MAPABC:
            case MAPBAR:
            case SOSOMAP:
                break;
            default:
                coordType = DEFAULT;
                break;
        }
        return coordType;
    }

    //-------地图类型------------
    static final String KEY_MAP_TYPE = "mapType";
    /**
     * 2D地图
     */
    public static final int MAP_2D = 0;
    /**
     * 3D地图
     */
    public static final int MAP_3D = 1;

    @IntDef({MAP_2D, MAP_3D})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MapType {
    }

    static SparseArray<String> mMapTypeValues
            = new SparseArray<>();

    static {
        mMapTypeValues.put(MAP_2D, "2D地图");
        mMapTypeValues.put(MAP_3D, "3D地图");
    }

    // 地图类型
    public static String getMapTypeValue(@MapType int mapType) {
        return mMapTypeValues.get(mapType);
    }

    // 地图类型转换
    @MapType
    public static int checkToMapType(String mapType) {
        try {
            return checkToMapType(Integer.valueOf(mapType));
        } catch (NumberFormatException ignore) {
        }
        return MAP_2D;
    }

    // 地图类型转换
    @MapType
    public static int checkToMapType(int mapType) {
        switch (mapType) {
            case MAP_2D:
            case MAP_3D:
                break;
            default:
                mapType = MAP_2D;
        }
        return mapType;
    }

    /**
     * 创建地图 {@link Fragment}
     *
     * @param cls     需要创建的{@link Fragment#getClass()}
     * @param mapType 使用地图类型，见{@link MapType}
     * @param <F>     需要创建的{@link Fragment}
     * @return 创建的{@link Fragment}，null则创建失败
     */
    public static <F extends AMapLocationFragment> F
    newMapFragment(Class<F> cls, @MapType int mapType) {
        F mapFragment = null;
        try {
            mapFragment = cls.newInstance();

            Bundle args = new Bundle();
            args.putInt(KEY_MAP_TYPE, mapType);
            mapFragment.setArguments(args);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return mapFragment;
    }

    /**
     * 使用地图类型
     *
     * @param mapFragment 使用地图的{@link Fragment}
     * @param <F>         使用地图的{@link Fragment}
     * @return 地图类型，见{@link MapType}
     */
    @MapType
    public static <F extends AMapLocationFragment> int
    getMapType(F mapFragment) {
        if (mapFragment != null) {
            int mapType = mapFragment.getArguments().getInt(KEY_MAP_TYPE, 0);
            return checkToMapType(mapType);
        }
        return MAP_2D;
    }

    public static LatLng toLatLng2D(MapLatLong mapLatLong) {
        LatLng latLng = new LatLng(
                mapLatLong.getLatitude(), mapLatLong.getLongitude()
        );
        if (mapLatLong.getCoordType() == DEFAULT) {
            return latLng;
        }
        // 坐标转换
        CoordinateConverter converter = new CoordinateConverter();
        switch (mapLatLong.getCoordType()) {
            case ALIYU:
                converter.from(CoordinateConverter.CoordType.ALIYUN);
                break;
            case BAIDU:
                converter.from(CoordinateConverter.CoordType.BAIDU);
                break;
            case DEFAULT:
                break;
            case GOOGLE:
                converter.from(CoordinateConverter.CoordType.GOOGLE);
                break;
            case GPS:
                converter.from(CoordinateConverter.CoordType.GPS);
                break;
            case MAPABC:
                converter.from(CoordinateConverter.CoordType.MAPABC);
                break;
            case MAPBAR:
                converter.from(CoordinateConverter.CoordType.MAPBAR);
                break;
            case SOSOMAP:
                converter.from(CoordinateConverter.CoordType.SOSOMAP);
                break;
        }
        converter.coord(latLng);
        return converter.convert();
    }

    // 南宁坐标
    public static final MapLatLong NANNING =
            new MapLatLong("南宁市", 22.8244, 108.4);
    // 北京坐标
    public static final MapLatLong BEIJING =
            new MapLatLong("北京市", 39.90403, 116.407525);
    // 西安市经纬度
    public static final MapLatLong XIAN =
            new MapLatLong("西安市", 34.341568, 108.940174);
    // 郑州市经纬度
    public static final MapLatLong ZHENGZHOU =
            new MapLatLong("郑州市", 34.7466, 113.625367);
    // 成都市经纬度
    public static final MapLatLong CHENGDU = new
            MapLatLong("成都市", 30.679879, 104.064855);

}
