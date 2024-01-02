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

    private SeekBar seekBarHue, seekBarSaturation, seekBarBrightness;
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
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.color_picker_view, this, true);
        colorWheelView = findViewById(R.id.colorWheel);
        seekBarHue = findViewById(R.id.seekBarHue);
        seekBarSaturation = findViewById(R.id.seekBarSaturation);
        seekBarBrightness = findViewById(R.id.seekBarBrightness);

        colors = new int[361];
        for (int i = 0; i < 361; i++) {
            colors[i] = Color.HSVToColor(new float[]{i, 1f, 1f});
        }

        setupSeekBars();

    }

    private void setupSeekBars() {
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar == seekBarHue) {
                    hsv[0] = progress;
                } else if (seekBar == seekBarSaturation) {
                    hsv[1] = progress / 100f;
                } else if (seekBar == seekBarBrightness) {
                    hsv[2] = progress / 100f;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        seekBarHue.setOnSeekBarChangeListener(listener);
        seekBarSaturation.setOnSeekBarChangeListener(listener);
        seekBarBrightness.setOnSeekBarChangeListener(listener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF touchPoint = new PointF(event.getX() - radius, event.getY() - radius);
        double touchRadius = Math.sqrt(Math.pow(touchPoint.x, 2) + Math.pow(touchPoint.y, 2));

        if (touchRadius <= radius) {
            float angle = (float) (Math.atan2(touchPoint.y, touchPoint.x) / Math.PI * 180f);
            angle = angle < 0 ? angle + 360f : angle;
            hsv[0] = angle;

            if (colorSelectedListener != null) {
                colorSelectedListener.onColorSelected(Color.HSVToColor(hsv));
            }

            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        this.colorSelectedListener = listener;
    }

    public interface OnColorSelectedListener {
        void onColorSelected(int color);
    }
}