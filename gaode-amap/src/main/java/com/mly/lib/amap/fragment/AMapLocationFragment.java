package com.mly.lib.amap.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mly.lib.amap.R;
import com.mly.lib.amap.bean.MapConfig;
import com.mly.lib.amap.bean.MapLatLong;
import com.mly.lib.amap.controller.AMapController;
import com.mly.lib.amap.controller.MapFace;
import com.mly.lib.amap.controller.impl.AMapController2DImpl;

/**
 * 基础地图d定位{@link Fragment}
 */
public class AMapLocationFragment extends Fragment implements MapFace {
    // 当前定位图标
    private Integer mLcIcon;
    // 当前单次定位
    private Boolean singleLocation;

    /**
     * 创建地图 {@link Fragment}
     *
     * @param cls     需要创建的{@link Fragment#getClass()}
     * @param mapType 使用地图类型，见{@link MapConfig.MapType}
     * @param <F>     需要创建的{@link Fragment}
     * @return 创建的{@link Fragment}，null则创建失败
     */
    public static <F extends AMapLocationFragment> F
    newMapFragment(Class<F> cls, @MapConfig.MapType int mapType) {
        return MapConfig.newMapFragment(cls, mapType);
    }

    /**
     * 使用地图类型
     *
     * @param mapFragment 使用地图的{@link Fragment}
     * @param <F>         使用地图的{@link Fragment}
     * @return 地图类型，见{@link MapConfig.MapType}
     */
    @MapConfig.MapType
    public static <F extends AMapLocationFragment> int
    getMapType(F mapFragment) {
        return MapConfig.getMapType(mapFragment);
    }

    // 地图控制
    private AMapController mMapController;

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        FrameLayout root = (FrameLayout) inflater.inflate(
                R.layout.fragment_base_amap, container, false);
        // 初始化地图控制器
        final Context context = root.getContext();
        switch (getMapType(this)) {
            case MapConfig.MAP_2D:
                mMapController = createAMap2DController(context);
                break;
            case MapConfig.MAP_3D:
                mMapController = createAMap3DController(context);
                break;
        }
        // 创建地图view
        View mapView = mMapController.onCreateMapView(inflater, root, savedInstanceState);
        if (mapView != null && mapView.getParent() == null) {
            if (mapView.getLayoutParams() == null) {
                mapView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
            }
            root.addView(mapView);
        }
        // 添加自定义view，覆盖地图绘制view上层
        View customView = onCreateCustomView(inflater, root, savedInstanceState);
        if (customView != null && customView.getParent() == null) {
            if (customView.getLayoutParams() == null) {
                customView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
            }
            root.addView(customView);
        }
        return root;
    }

    /**
     * @return 3D地图控制器
     */
    @NonNull
    protected AMapController createAMap3DController(Context context) {
        // TODO: 2018/1/24
        return new AMapController2DImpl(context);
    }

    /**
     * @return 2D地图控制器
     */
    @NonNull
    protected AMapController createAMap2DController(Context context) {
        return new AMapController2DImpl(context);
    }

    /**
     * 添加自定义view，覆盖地图绘制view上层
     */
    @Nullable
    public View onCreateCustomView(LayoutInflater inflater, @NonNull ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amap_location, container, false);
        // 当前定位
        final ImageView mLcView = view.findViewById(R.id.iv_my_location_button);
        mLcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示当前定位
                showMyLocation();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 创建地图
        mMapController.onCreate(savedInstanceState);
        // 初始化创建前设置的数据
        onCheckSettingBefore();
    }

    // 初始化创建前设置的数据
    private void onCheckSettingBefore() {
        // 当前定位图标
        if (mLcIcon != null) {
            setMyLocationIcon(mLcIcon);
        }
        // 当前单次定位
        if (singleLocation != null) {
            showMyLocation(singleLocation);
            singleLocation = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 销毁地图
        mMapController.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 暂停地图的绘制
        mMapController.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 重新绘制加载地图
        onRefreshMap();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存地图当前的状态
        mMapController.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 内存不足
        mMapController.onLowMemory();
    }

    @Override
    public void setMyLocationIcon(int icResId) {
        if (mMapController == null) {
            mLcIcon = icResId;
            return;
        }
        mMapController.setMyLocationIcon(icResId);
        mLcIcon = null;
    }

    @Override
    public void showMyLocation() {
        showMyLocation(true);
    }

    @Override
    public void showMyLocation(boolean single) {
        if (mMapController == null) {
            singleLocation = single;
            return;
        }
        mMapController.showMyLocation(single);
    }

    @Nullable
    @Override
    public MapLatLong getMyLocation() {
        if (mMapController == null) return null;
        return mMapController.getMyLocation();
    }

    @Override
    public void onRefreshMap() {
        if (mMapController == null) return;
        // 重新绘制加载地图
        mMapController.onResume();
    }

}
