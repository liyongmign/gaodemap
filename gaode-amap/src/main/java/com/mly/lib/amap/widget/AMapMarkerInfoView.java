package com.mly.lib.amap.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mly.lib.amap.R;

/**
 * Created by lym on 2018/1/24.
 * <p>
 * 地图点标记弹窗
 */

public class AMapMarkerInfoView extends FrameLayout {
    // 标题
    private TextView mTleView;
    // 描述
    private TextView mDesView;

    public AMapMarkerInfoView(@NonNull Context context) {
        this(context, null);
    }

    public AMapMarkerInfoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AMapMarkerInfoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_marker_info_window, this);
        // 标题
        mTleView = findViewById(R.id.tv_marker_name);
        mTleView.setVisibility(GONE);
        // 描述
        mDesView = findViewById(R.id.tv_marker_description);
        mDesView.setVisibility(GONE);
    }

    /**
     * @param title 标记标题
     */
    public void setTitle(CharSequence title) {
        mTleView.setText(title);
        mTleView.setVisibility(!TextUtils.isEmpty(title) ?
                VISIBLE : GONE);
    }

    /**
     * @param description 标记描述
     */
    public void setDescription(CharSequence description) {
        mDesView.setText(description);
        mDesView.setVisibility(!TextUtils.isEmpty(description) ?
                VISIBLE : GONE);
    }

}
