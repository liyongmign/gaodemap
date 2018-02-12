package app.mly.com.gaode_map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by lym on 2018/1/25.
 * <p>
 * 画三角形view
 */

public class TriangleView extends AppCompatImageView {
    private int mViewWidth, mViewHeight;
    private int mTriangleColor = 0x0;
    private float mApexAngle, mLeftAngle, mRightAngle;
    private Paint mPaint;

    public TriangleView(Context context) {
        this(context, null);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        setTriangleColor(mTriangleColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制三角形
        if (isDrawTriangle()) {
            drawTriangle(canvas);
        }
    }

    // 绘制三角形
    private void drawTriangle(Canvas canvas) {

    }

    // 是否绘制三角形
    private boolean isDrawTriangle() {
        // 顶角必须大于0
        if (mApexAngle <= 0) return false;
        // 其余两角必须有一个大于0
        return !(mLeftAngle <= 0 && mRightAngle <= 0);
    }

    public void setTriangleColor(int triangleColor) {
        this.mTriangleColor = triangleColor;
        mPaint.setColor(triangleColor);
        invalidate();
    }

}
