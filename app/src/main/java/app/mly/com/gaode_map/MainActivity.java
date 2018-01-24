package app.mly.com.gaode_map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mly.lib.amap.bean.MapConfig;
import com.mly.lib.amap.fragment.AMapAreaFragment;
import com.mly.lib.amap.fragment.AMapLocationFragment;
import com.mly.lib.amap.fragment.AMapMarkerFragment;
import com.mly.lib.amap.fragment.AMapPolylineFragment;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_LOCATION = 1;
    private static final boolean checkPermission = false;
    static final int location = 0, marker = 1, area = 2, polyline = 3;

    private int flags = marker;
    private static final int mapType = MapConfig.MAP_2D;
    private boolean addMapFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_location).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkPermission(location);
                    }
                }
        );
        findViewById(R.id.button_marker).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkPermission(marker);
                    }
                }
        );
        findViewById(R.id.button_area).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkPermission(area);
                    }
                }
        );
        findViewById(R.id.button_polyline).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkPermission(polyline);
                    }
                }
        );
    }

    private void checkPermission(int flags) {
        this.flags = flags;
        if (checkPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, PERMISSION_LOCATION);
        } else {
            addMapFragment = false;
            showMapContent(flags);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (addMapFragment) {
            showMapContent(flags);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_LOCATION && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {
            addMapFragment = true;
        }
    }

    // 显示地图
    private void showMapContent(int flags) {
        this.flags = flags;
        switch (flags) {
            case location:
                // 地图定位
                setMapLocationContent();
                break;
            case marker:
                // 地图标记
                setMapMarkerContent();
                break;
            case area:
                // 地图区域
                setMapAreaContent();
                break;
            case polyline:
                // 地图轨迹
                setMapPolylineContent();
                break;
        }
    }

    // 地图定位
    private void setMapLocationContent() {
        FragmentManager manager = getSupportFragmentManager();
        AMapLocationFragment fragment = MapConfig.newMapFragment(AMapLocationFragment.class, mapType);
        // 连续定位
        fragment.showMyLocation(false);
        manager.beginTransaction()
                .replace(R.id.fl_root_container, fragment)
                .commit();
    }

    // 地图轨迹
    private void setMapPolylineContent() {
        FragmentManager manager = getSupportFragmentManager();
        AMapPolylineFragment fragment = MapConfig.newMapFragment(AMapPolylineFragment.class, mapType);
        // 设置轨迹
        fragment.setPolyline(
                MapConfig.BEIJING,
                MapConfig.XIAN,
                MapConfig.ZHENGZHOU,
                MapConfig.NANNING,
                MapConfig.CHENGDU
        );
        manager.beginTransaction()
                .replace(R.id.fl_root_container, fragment)
                .commit();
    }

    // 地图区域
    private void setMapAreaContent() {
        FragmentManager manager = getSupportFragmentManager();
        AMapAreaFragment fragment = MapConfig.newMapFragment(AMapAreaFragment.class, mapType);
        // 设置地图区域
        fragment.setMapArea(
                MapConfig.NANNING,
                MapConfig.BEIJING,
                MapConfig.XIAN,
                MapConfig.ZHENGZHOU,
                MapConfig.CHENGDU
        );
        manager.beginTransaction()
                .replace(R.id.fl_root_container, fragment)
                .commit();
    }

    // 地图标记
    private void setMapMarkerContent() {
        FragmentManager manager = getSupportFragmentManager();
        AMapMarkerFragment fragment = MapConfig.newMapFragment(AMapMarkerFragment.class, mapType);
        // 设置图标
//        fragment.setMarkerIcons(R.mipmap.ic_launcher_round);
        // 添加标记
        fragment.setMarkers(
                MapConfig.NANNING,
                MapConfig.BEIJING,
                MapConfig.XIAN,
                MapConfig.ZHENGZHOU,
                MapConfig.CHENGDU
        );
        manager.beginTransaction()
                .replace(R.id.fl_root_container, fragment)
                .commit();
    }
}
