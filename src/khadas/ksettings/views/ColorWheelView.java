package com.khadas.ksettings.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ColorWheelView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int radius;
    private int[] colors = new int[361];

    public boolean isDrawCurrentColor() {
        return drawCurrentColor;
    }

    public void setDrawCurrentColor(boolean drawCurrentColor) {
        this.drawCurrentColor = drawCurrentColor;
    }

    private boolean drawCurrentColor = false;

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
                paint.setColor(Color.HSVToColor(new float[]{getTag().equals("hue") ? 0 : 360, 1f, 1f}));
                canvas.drawCircle(radius, radius, radius, paint);
            }
            else {
                SweepGradient sweepGradient = new SweepGradient(radius, radius, colors, null);
                paint.setShader(sweepGradient);
                canvas.drawCircle(radius, radius, radius, paint);
            }
        }
    }
}