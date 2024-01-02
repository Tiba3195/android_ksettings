package com.khadas.ksettings.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import android.app.WallpaperColors;

import androidx.annotation.Nullable;

import com.khadas.ksettings.R;


public class ColorPickerView extends LinearLayout {
    private ColorWheelView colorWheelView;
    private int radius;
    private int[] colors;

    private float[] hsv = {0, 1, 1};

    private PointF touchPoint;

    private int CurrentColor=0;
    int getCurrentColor() {
        return CurrentColor;
    }

    private OnColorSelectedListener colorSelectedListener;

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColorPickerView(Context context) {
        super(context);
        init(context);
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.min(w, h) / 2;

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if(!isInEditMode()) {
            colorWheelView.setCurrentColor(Color.HSVToColor(hsv));
        }
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.color_picker_view, this, true);
        colorWheelView = findViewById(R.id.colorWheel);

        colors = new int[361];
        for (int i = 0; i < 361; i++) {
            colors[i] = Color.HSVToColor(new float[]{i, 1f, 1f});
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d("ColorWheelVerbose", "<=======================================>");
        touchPoint = new PointF(event.getX() - radius, event.getY() - radius);
        double touchRadius = Math.sqrt(Math.pow(touchPoint.x, 2) + Math.pow(touchPoint.y, 2));

        Log.d("ColorWheelVerbose", "TouchEvent: Action=" + event.getAction() + ", X=" + event.getX() + ", Y=" + event.getY());
        Log.d("ColorWheelVerbose", "Calculated TouchPoint: " + touchPoint + ", TouchRadius: " + touchRadius + ", WheelRadius: " + radius);

        colorWheelView.setTouchPoint(new PointF(event.getRawX(), event.getRawY()));

        // Check if the touch is within the radius
        if (touchRadius <= radius) {

            float angle = (float) (Math.atan2(touchPoint.y, touchPoint.x) / Math.PI * 180f);
            angle = angle < 0 ? angle + 360f : angle;
            hsv[0] = angle;

            Log.d("ColorWheelVerbose", "Inside wheel. Angle: " + angle + ", HSV[0]: " + hsv[0]);

            if (colorSelectedListener != null) {
                colorSelectedListener.onColorSelected(Color.HSVToColor(hsv));
            }

            Log.d("ColorWheelVerbose", "Color selected: " + Color.HSVToColor(hsv));

            CurrentColor = Color.HSVToColor(hsv);
            colorWheelView.setCurrentColor(CurrentColor);
            invalidate();
            handleColorWheelState(event.getAction());
            Log.d("ColorWheelVerbose", "<=======================================>");
            return true; // Touch was within the color wheel
        }

        handleColorWheelState(event.getAction());
        Log.d("ColorWheelVerbose", "<=======================================>");
        return super.onTouchEvent(event); // Pass the event up if not within the color wheel
    }

    private void handleColorWheelState(int action) {
        boolean drawCurrentColor = action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE;
        colorWheelView.setDrawCurrentColor(drawCurrentColor);
        colorWheelView.invalidate();
        Log.d("ColorWheelVerbose", "Handling State: Action=" + action + ", drawCurrentColor set to " + drawCurrentColor);
    }

    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        this.colorSelectedListener = listener;
    }

    public interface OnColorSelectedListener {
        void onColorSelected(int color);
    }
}