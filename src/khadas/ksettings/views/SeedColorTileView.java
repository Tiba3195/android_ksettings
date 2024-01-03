package com.khadas.ksettings.views;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.khadas.ksettings.R;

public class SeedColorTileView extends ConstraintLayout {
    private View seedColorView;

    public String getSeedColorText() {
        return (String) seedColorTextView.getText();
    }

    public void setSeedColorTextView(TextView seedColorTextView) {
        this.seedColorTextView = seedColorTextView;
    }

    private TextView seedColorTextView;

    public int getColor() {
        return color;
    }

    private   int color;

    private SeedColorTileView.OnColorSelectedListener colorSelectedListener;

    public SeedColorTileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        // Inflate the layout
        LayoutInflater.from(context).inflate(R.layout.seed_color_tile_layout, this, true);

        // Initialize components
        seedColorView = findViewById(R.id.seed_color);
        seedColorTextView = findViewById(R.id.text_seed_color_value);
    }

    public void setSeedColor(String colorHex) {
        color =  Color.parseColor(colorHex);
        seedColorView.setBackgroundColor(color);
        seedColorTextView.setText(colorHex);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (colorSelectedListener != null) {
                colorSelectedListener.onColorSelected(getColor());
            }
            return true; // Touch was within the color wheel
        }

        return super.onTouchEvent(event); // Pass the event up if not within the color wheel
    }

    public void setOnColorSelectedListener(SeedColorTileView.OnColorSelectedListener listener) {
        this.colorSelectedListener = listener;
    }

    public interface OnColorSelectedListener {
        void onColorSelected(int color);
    }
}