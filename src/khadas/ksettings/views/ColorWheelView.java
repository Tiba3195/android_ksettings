package com.khadas.ksettings.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class ColorWheelView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private  Paint touchpointpaint= new Paint(Paint.ANTI_ALIAS_FLAG);
    private  Paint currentColorPaint= new Paint(Paint.ANTI_ALIAS_FLAG);

    private int radius;
    private int[] colors = new int[361];

    public void setTouchPoint(PointF touchPoint) {
        this.touchPoint = touchPoint;
    }

    private PointF touchPoint = new PointF(0, 0);

    public boolean isDrawCurrentColor() {
        return drawCurrentColor;
    }

    public void setDrawCurrentColor(boolean drawCurrentColor) {
        this.drawCurrentColor = drawCurrentColor;
    }

    private boolean drawCurrentColor = false;

    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
    }

    private  int currentColor;

    public ColorWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ColorWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);
        touchpointpaint.setStyle(Paint.Style.FILL);
        touchpointpaint.setColor(Color.WHITE);
        currentColorPaint.setStyle(Paint.Style.FILL);
        currentColorPaint.setColor(Color.WHITE);

        for (int i = 0; i < 361; i++) {
            colors[i] = Color.HSVToColor(new float[]{i, 1f, 1f});
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.min(w, h) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(isInEditMode())
        {
            paint.setColor(Color.RED);
            //paint.setShader(null); // Remove the shader if previously set

            // Draw a red circle
            canvas.drawCircle(radius, radius, radius, paint);
        }
        else {

            if(isDrawCurrentColor())
            {
                currentColorPaint.setColor(currentColor);
                canvas.drawCircle(radius, radius, radius, currentColorPaint);
            }
            else {
                SweepGradient sweepGradient = new SweepGradient(radius, radius, colors, null);
                paint.setShader(sweepGradient);
                canvas.drawCircle(radius, radius, radius, paint);
                canvas.drawCircle(touchPoint.x+5, touchPoint.y +5, 10, touchpointpaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }
}