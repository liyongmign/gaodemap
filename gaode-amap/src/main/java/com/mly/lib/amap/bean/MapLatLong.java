package com.mly.lib.amap.bean;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * 坐标系
 */
@SuppressWarnings("DeprecatedIsStillUsed")
public class MapLatLong implements Parcelable {
    /**
     * 地点名称
     */
    private String name;
    /**
     * 纬度
     */
    private double latitude;
    /**
     * 经度
     */
    private double longitude;
    /**
     * 坐标类型
     */
    @MapConfig.CoordType
    private int coordType = MapConfig.DEFAULT;
    /**
     * 附带信息
     */
    private String info;
    /**
     * 方向角
     */
    private float bearing;
    /**
     * 速度
     */
    private float speed;
    /**
     * 时间
     */
    private long time;

    private MapLatLong(Parcel in) {
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        bearing = in.readFloat();
        speed = in.readFloat();
        time = in.readLong();
        coordType = in.readInt();
        info = in.readString();
    }

    public static final Creator<MapLatLong> CREATOR = new Creator<MapLatLong>() {
        @Override
        public MapLatLong createFromParcel(Parcel in) {
            return new MapLatLong(in);
        }

        @Override
        public MapLatLong[] newArray(int size) {
            return new MapLatLong[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeFloat(bearing);
        dest.writeFloat(speed);
        dest.writeLong(time);
        dest.writeInt(coordType);
        dest.writeString(info);
    }

    // 附带信息（字符串、json等）
    public void setInfo(String info) {
        this.info = info;
    }

    // 附带信息（字符串、json等）
    public String getInfo() {
        return info;
    }

    public MapLatLong() {
        this(MapConfig.DEFAULT);
    }

    public MapLatLong(@MapConfig.CoordType int coordType) {
        this.coordType = coordType;
    }

    /**
     * 创建默认高德地图坐标
     *
     * @param latitude  纬度
     * @param longitude 经度
     */
    public MapLatLong(double latitude, double longitude) {
        this(null, latitude, longitude);
    }

    /**
     * 创建默认高德地图坐标
     *
     * @param name      地点名
     * @param latitude  纬度
     * @param longitude 经度
     */
    public MapLatLong(String name, double latitude, double longitude) {
        this(name, latitude, longitude, MapConfig.DEFAULT);
    }

    /**
     * 创建指定类型坐标
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @param coordType 见{@link MapConfig.CoordType}
     */
    public MapLatLong(double latitude, double longitude, @MapConfig.CoordType int coordType) {
        this(null, latitude, longitude, coordType);
    }

    /**
     * 创建指定类型坐标
     *
     * @param name      地点名
     * @param latitude  纬度
     * @param longitude 经度
     * @param coordType 见{@link MapConfig.CoordType}
     */
    public MapLatLong(String name, double latitude, double longitude, @MapConfig.CoordType int coordType) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.coordType = coordType;
    }

    /**
     * @return 方向角
     */
    public float getBearing() {
        return bearing;
    }

    /**
     * @param bearing 方向角
     */
    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    /**
     * @return 速度
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * @param speed 速度
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * @return 时间
     */
    public long getTime() {
        return time;
    }

    /**
     * @param time 时间
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * @return 地址名称
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 地址名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param latitude 纬度
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return 纬度
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param longitude 经度
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return 经度
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * 坐标类型
     *
     * @param coordType {@link MapConfig.CoordType}
     */
    public void setCoordType(@MapConfig.CoordType int coordType) {
        this.coordType = coordType;
    }

    @MapConfig.CoordType
    public int getCoordType() {
        return coordType;
    }

    /**
     * 创建坐标
     *
     * @param string 坐标字符串，如 "地名:经度,纬度" --> "南宁站:108.321763,22.83255"
     * @return MapLatLong坐标组
     */
    @Nullable
    public static MapLatLong newMapLatLong(String string) {
        return newMapLatLong(string, MapConfig.DEFAULT);
    }

    /**
     * 创建坐标
     *
     * @param string    坐标字符串，如 "地名:经度,纬度" --> "南宁站:108.321763,22.83255"
     * @param coordType 见 {@link MapConfig.CoordType}
     * @return MapLatLong坐标
     */
    @SuppressWarnings("WeakerAccess")
    @Nullable
    public static MapLatLong newMapLatLong(String string, @MapConfig.CoordType int coordType) {
        MapLatLong latLong = null;
        if (!TextUtils.isEmpty(string)) {
            try {
                String address = null;
                double latitude = -1;
                double longitude;
                // ":"前的地址信息
                int index = string.indexOf(":");
                if (index != -1) {
                    address = string.substring(0, index);
                    string = string.substring(index + 1, string.length());
                }
                // 经度值
                index = string.indexOf(",");
                if (index != -1) {
                    latitude = Double.valueOf(string.substring(0, index));
                    string = string.substring(index + 1, string.length());
                }
                // 纬度值
                longitude = Double.valueOf(string);

                if (latitude != -1) {
                    latLong = new MapLatLong(latitude, longitude);
                    latLong.setName(address);
                    latLong.setCoordType(coordType);
                }
            } catch (NumberFormatException ignore) {
                error("非法坐标字符串 -> \"" + string + "\"");
            }
        }
        return latLong;
    }

    // 错误日志
    private static void error(String error) {
        Log.e(MapLatLong.class.getSimpleName(), error);
    }


    @Override
    public String toString() {
        String str = latitude + ", " + longitude;
        if (!TextUtils.isEmpty(name)) {
            str = name + ":" + str;
        }
        return str;
    }

    /**
     * 复制一个对象
     *
     * @return 数据一致
     */
    public MapLatLong copy() {
        MapLatLong mapLatLong = new MapLatLong(name, latitude, longitude, coordType);
        mapLatLong.setBearing(bearing);
        mapLatLong.setInfo(info);
        mapLatLong.setSpeed(speed);
        mapLatLong.setTime(time);
        return mapLatLong;
    }

    /**
     * 坐标平移
     *
     * @param lat 平移经度
     * @param lng 平移纬度
     * @return 平移后的坐标
     */
    public MapLatLong offset(double lat, double lng) {
        this.latitude += lat;
        this.longitude += lng;
        return this;
    }

    /**
     * @return 转为2d坐标
     */
    public LatLng toLatLong2d() {
        // 纬度
        double lat = this.latitude;
        // 经度
        double lng = this.longitude;
        LatLng latLng = new LatLng(lat, lng);
        if (getCoordType() == MapConfig.DEFAULT) {
            return latLng;
        }

        // 坐标转换
        CoordinateConverter converter = new CoordinateConverter();
        switch (getCoordType()) {
            case MapConfig.ALIYU:
                converter.from(CoordinateConverter.CoordType.ALIYUN);
                break;
            case MapConfig.BAIDU:
                converter.from(CoordinateConverter.CoordType.BAIDU);
                break;
            case MapConfig.DEFAULT:
                break;
            case MapConfig.GOOGLE:
                converter.from(CoordinateConverter.CoordType.GOOGLE);
                break;
            case MapConfig.GPS:
                converter.from(CoordinateConverter.CoordType.GPS);
                break;
            case MapConfig.MAPABC:
                converter.from(CoordinateConverter.CoordType.MAPABC);
                break;
            case MapConfig.MAPBAR:
                converter.from(CoordinateConverter.CoordType.MAPBAR);
                break;
            case MapConfig.SOSOMAP:
                converter.from(CoordinateConverter.CoordType.SOSOMAP);
                break;
        }
        converter.coord(latLng);
        return converter.convert();
    }

    /**
     * @param context {@link Context}
     * @return 转为2d坐标
     */
    public com.amap.api.maps.model.LatLng toLatLong3d(Context context) {
        // 纬度
        double lat = this.latitude;
        // 经度
        double lng = this.longitude;
        com.amap.api.maps.model.LatLng latLng =
                new com.amap.api.maps.model.LatLng(lat, lng);
        if (getCoordType() == MapConfig.DEFAULT) {
            return latLng;
        }

        // 坐标转换
        com.amap.api.maps.CoordinateConverter converter =
                new com.amap.api.maps.CoordinateConverter(context);
        switch (getCoordType()) {
            case MapConfig.ALIYU:
                converter.from(com.amap.api.maps.CoordinateConverter.CoordType.ALIYUN);
                break;
            case MapConfig.BAIDU:
                converter.from(com.amap.api.maps.CoordinateConverter.CoordType.BAIDU);
                break;
            case MapConfig.DEFAULT:
                break;
            case MapConfig.GOOGLE:
                converter.from(com.amap.api.maps.CoordinateConverter.CoordType.GOOGLE);
                break;
            case MapConfig.GPS:
                converter.from(com.amap.api.maps.CoordinateConverter.CoordType.GPS);
                break;
            case MapConfig.MAPABC:
                converter.from(com.amap.api.maps.CoordinateConverter.CoordType.MAPABC);
                break;
            case MapConfig.MAPBAR:
                converter.from(com.amap.api.maps.CoordinateConverter.CoordType.MAPBAR);
                break;
            case MapConfig.SOSOMAP:
                converter.from(com.amap.api.maps.CoordinateConverter.CoordType.SOSOMAP);
                break;
        }
        converter.coord(latLng);
        return converter.convert();
    }

    /**
     * 设置定位信息
     *
     * @param aMapLocation 定位对象
     */
    public void setAMapLocation(AMapLocation aMapLocation) {
        if (aMapLocation == null) return;

        name = aMapLocation.getAddress();
        latitude = aMapLocation.getLatitude();
        longitude = aMapLocation.getLongitude();
        coordType = MapConfig.DEFAULT;
        info = aMapLocation.getDescription();
        bearing = aMapLocation.getBearing();
        speed = aMapLocation.getSpeed();
        time = aMapLocation.getTime();
    }

    /**
     * 解析坐标组字符串
     *
     * @param polyline 如："经度1,纬度1;经度2,纬度2"
     *                 --> "111.288137,23.493822;111.285948,23.488194"
     * @return {@link List< MapLatLong >}
     */
    @Nullable
    public static List<MapLatLong> parsePolyline(String polyline) {
        if (!TextUtils.isEmpty(polyline)) {
            List<MapLatLong> mapLatLongs = new ArrayList<>();
            String[] strings = polyline.split(";");
            for (String coord : strings) {
                try {
                    String[] split = coord.split(",");
                    MapLatLong latLong = new MapLatLong(
                            Double.valueOf(split[0]), Double.valueOf(split[1])
                    );
                    // 添加
                    mapLatLongs.add(latLong);
                } catch (NumberFormatException ignore) {
                    error("\"" + coord + "\" 无法转换成坐标");
                } catch (IndexOutOfBoundsException ignore) {
                    error("坐标不合法：正确 -> 111.288137,23.493822");
                }
            }
            // 解析结果
            if (!mapLatLongs.isEmpty()) {
                return mapLatLongs;
            }
        }
        return null;
    }

}
