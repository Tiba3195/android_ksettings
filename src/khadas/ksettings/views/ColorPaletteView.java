package com.khadas.ksettings.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.khadas.util.ColorScheme;
import com.khadas.util.TonalPalette;

import java.util.List;

public class ColorPaletteView extends View {
    private ColorScheme colorScheme;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int colorWidth;
    private int maxColorsInRow;

    public ColorPaletteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setColorScheme(ColorScheme scheme) {
        this.colorScheme = scheme;
        updateMaxColorsInRow();
        this.colorWidth = getWidth() / maxColorsInRow;
        invalidate();
    }

    private void updateMaxColorsInRow() {
        int maxHues = colorScheme.getAllHues().stream().mapToInt(hue -> hue.getAllShades().size()).max().orElse(0);
        int maxAccents = colorScheme.getAllAccentColors().size();
        int maxNeutrals = colorScheme.getAllNeutralColors().size();
        maxColorsInRow = Math.max(Math.max(maxHues, maxAccents), maxNeutrals);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (colorScheme == null) return;

        int y = 0;

        // Draw Hues
        for (TonalPalette hue : colorScheme.getAllHues()) {
            drawColorRow(canvas, hue.getAllShades(), y);
            y += colorWidth;
        }

        // Draw Accent Colors
        drawColorRow(canvas, colorScheme.getAllAccentColors(), y);
        y += colorWidth;

        // Draw Neutral Colors
        drawColorRow(canvas, colorScheme.getAllNeutralColors(), y);
    }

    private void drawColorRow(Canvas canvas, List<Integer> colors, int yPos) {
        int x = 0;
        for (Integer color : colors) {
            paint.setColor(color);
            canvas.drawRect(x, yPos, x + colorWidth, yPos + colorWidth, paint);
            x += colorWidth;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (colorScheme != null) {
            colorWidth = w / maxColorsInRow;
        }
    }
}
