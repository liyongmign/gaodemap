package com.mly.lib.amap.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mly.lib.amap.R;
import com.mly.lib.amap.bean.MapLatLong;
import com.mly.lib.amap.controller.AMapController;
import com.mly.lib.amap.controller.MapAreaFace;
import com.mly.lib.amap.controller.impl.AMapAreaController2DImpl;

/**
 * 地图区域{@link Fragment}
 */
public class AMapAreaFragment extends AMapLocationFragment
        implements MapAreaFace {
    // 地图控制器
    private MapAreaFace mAreaController;
    // 区域坐标列表
    private MapLatLong[] mMapArea;
    // 区域颜色
    private Integer mAreaColor;
    // 区域边框颜色
    private Integer mAreaStrokeColor;
    // 区域边框宽度
    private Float mAreaStrokeWidth;

    @Nullable
    @Override
    public View onCreateCustomView(LayoutInflater inflater, @NonNull ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amap_area, container, false);
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

    @NonNull
    @Override
    protected AMapController createAMap2DController(Context context) {
        AMapAreaController2DImpl controller2D = new AMapAreaController2DImpl(context);
        mAreaController = controller2D;
        return controller2D;
    }

    @NonNull
    @Override
    protected AMapController createAMap3DController(Context context) {
        // TODO: 2018/1/24
        AMapAreaController2DImpl controller3D = new AMapAreaController2DImpl(context);
        mAreaController = controller3D;
        return controller3D;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 设置创建前设置的区域
        setAreaBefore();
    }

    // 设置创建前设置的区域
    private void setAreaBefore() {
        // 绘制区域
        if (mMapArea != null) {
            setMapArea(mMapArea);
        }
        // 区域颜色
        if (mAreaColor != null) {
            setMapAreaColor(mAreaColor);
        }
        // 区域边框颜色
        if (mAreaStrokeColor != null) {
            setMapAreaStrokeColor(mAreaStrokeColor);
        }
        // 区域边框宽度
        if (mAreaStrokeWidth != null) {
            setMapAreaStrokeWidth(mAreaStrokeWidth);
        }
    }

    @Override
    public void setMapAreaColor(int color) {
        if (mAreaController == null) {
            mAreaColor = color;
            return;
        }
        mAreaController.setMapAreaColor(color);
        mAreaColor = null;
    }

    @Override
    public void setMapAreaStrokeColor(int color) {
        if (mAreaController == null) {
            mAreaStrokeColor = color;
            return;
        }
        mAreaController.setMapAreaStrokeColor(color);
        mAreaStrokeColor = null;
    }

    @Override
    public void setMapAreaStrokeWidth(float strokeWidth) {
        if (mAreaController == null) {
            mAreaStrokeWidth = strokeWidth;
            return;
        }
        mAreaController.setMapAreaStrokeWidth(strokeWidth);
        mAreaStrokeWidth = null;
    }

    @Override
    public void setMapArea(MapLatLong... area) {
        if (mAreaController == null) {
            // 记录地图区域
            mMapArea = area;
            return;
        }
        mAreaController.setMapArea(area);
        mMapArea = null;
    }
}
