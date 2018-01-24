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
import android.widget.RadioButton;

import com.mly.lib.amap.R;
import com.mly.lib.amap.bean.MapConfig;
import com.mly.lib.amap.bean.MapLatLong;
import com.mly.lib.amap.controller.AMapController;
import com.mly.lib.amap.controller.MapPolylineFace;
import com.mly.lib.amap.controller.impl.AMapPolylineController2D;

/**
 * 显示地图轨迹{@link Fragment}
 */
public class AMapPolylineFragment extends AMapLocationFragment
        implements MapPolylineFace {
    // 轨迹控制器
    private MapPolylineFace mPolylineController;

    // 轨迹列表
    private MapLatLong[] mPolyline;
    // 轨迹颜色
    private Integer mPolylineColor = MapConfig.POLYLINE_COLOR;
    // 起点图标
    private Integer mStartIcon = 0;
    // 终点图标
    private Integer mEndIcon = 0;
    // 轨迹线条颜色
    private Float mPolylineWidth;

    @Nullable
    @Override
    public View onCreateCustomView(LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amap_polyline, container, false);
        // 当前定位
        final ImageView mLcView = view.findViewById(R.id.iv_my_location_button);
        mLcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示当前定位
                showMyLocation();
            }
        });
        // 查看起点
        final RadioButton mStView = view.findViewById(R.id.rb_poly_start_button);
        mStView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPolylineStart();
            }
        });
        // 查看终点
        final RadioButton mEdView = view.findViewById(R.id.rb_poly_end_button);
        mEdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPolylineEnd();
            }
        });
        return view;
    }

    @NonNull
    @Override
    protected AMapController createAMap2DController(Context context) {
        AMapPolylineController2D controller2D = new AMapPolylineController2D(context);
        mPolylineController = controller2D;
        return controller2D;
    }

    @NonNull
    @Override
    protected AMapController createAMap3DController(Context context) {
        // TODO: 2018/1/24
        AMapPolylineController2D controller3D = new AMapPolylineController2D(context);
        mPolylineController = controller3D;
        return controller3D;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 创建前设置的数据初始化
        setPolylineBefore();
    }

    // 创建前设置的数据初始化
    private void setPolylineBefore() {
        // 轨迹坐标列表
        if (mPolyline != null) {
            setPolyline(mPolyline);
        }
        // 轨迹颜色
        if (mPolylineColor != null) {
            setPolylineColor(mPolylineColor);
        }
        // 起点图标
        if (mStartIcon != null) {
            setPolylineStartIcon(mStartIcon);
        }
        // 终点图标
        if (mEndIcon != null) {
            setPolylineEndIcon(mEndIcon);
        }
        // 轨迹线条颜色
        if (mPolylineWidth != null) {
            setPolylineWidth(mPolylineWidth);
        }
    }

    @Override
    public void setPolylineStartIcon(int mStartIcon) {
        if (mPolylineController == null) {
            this.mStartIcon = mStartIcon;
            return;
        }
        mPolylineController.setPolylineStartIcon(mStartIcon);
        this.mStartIcon = null;
    }

    @Override
    public void setPolylineEndIcon(int mEndIcon) {
        if (mPolylineController == null) {
            this.mEndIcon = mEndIcon;
            return;
        }
        mPolylineController.setPolylineEndIcon(mEndIcon);
        this.mEndIcon = null;
    }

    @Override
    public void showPolylineStart() {
        mPolylineController.showPolylineStart();
    }

    @Override
    public void showPolylineEnd() {
        mPolylineController.showPolylineEnd();
    }

    @Override
    public void setPolylineColor(int color) {
        if (mPolylineController == null) {
            mPolylineColor = color;
            return;
        }
        mPolylineController.setPolylineColor(color);
        mPolylineColor = null;
    }

    @Override
    public void setPolylineWidth(float polylineWidth) {
        if (mPolylineController == null) {
            mPolylineWidth = polylineWidth;
            return;
        }
        mPolylineController.setPolylineWidth(polylineWidth);
        mPolylineWidth = null;
    }

    @Override
    public void setPolyline(MapLatLong... polyline) {
        if (mPolylineController == null) {
            mPolyline = polyline;
            return;
        }
        mPolylineController.setPolyline(polyline);
        mPolyline = null;
    }

}
