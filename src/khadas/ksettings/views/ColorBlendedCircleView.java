package com.khadas.ksettings.views;

import static com.khadas.util.MathHelper.MapValueToRange;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ColorBlendedCircleView extends View {
    private Paint paint;
    private Paint ringPaint;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private Canvas outputBitmapCanvas;
    private Bitmap outputBitmap;
    private int primaryColor = 0xFFFF0000; // Default Red
    private int secondaryColor = 0xFF00FF00; // Default Green
    private int tertiaryColor = 0xFF0000FF; // Default Blue

    public float getPrimaryColorAmount() {
        return primaryColorAmount;
    }

    public void setPrimaryColorAmount(float primaryColorAmount) {
        this.primaryColorAmount = MapValueToRange(0.0f,1.0f,0.1f,0.8f,primaryColorAmount) ;
        colorDistributions[0] = this.primaryColorAmount;
        invalidate();
    }

    public float getSecondaryColorAmount() {
        return secondaryColorAmount;
    }

    public void setSecondaryColorAmount(float secondaryColorAmount) {
        this.secondaryColorAmount =MapValueToRange(0.0f,1.0f,0.5f,2.5f,secondaryColorAmount) ;
        colorDistributions[1] = this.secondaryColorAmount;
        invalidate();
    }

    public float getTertiaryColorAmount() {
        return tertiaryColorAmount;
    }

    public void setTertiaryColorAmount(float tertiaryColorAmount) {
        this.tertiaryColorAmount = MapValueToRange(0.0f,1.0f,4.0f,10.0f,tertiaryColorAmount) ;
        colorDistributions[2] = this.tertiaryColorAmount;
        invalidate();
    }
    private float[] colorDistributions = new float[]{0.50f, 0.8f, 6.9f}; // Distribution of each color

    void setColorDistributions(float primaryColorAmount, float secondaryColorAmount, float tertiaryColorAmount) {
        this.primaryColorAmount = primaryColorAmount;
        this.secondaryColorAmount = secondaryColorAmount;
        this.tertiaryColorAmount = tertiaryColorAmount;
        colorDistributions[0] = primaryColorAmount;
        colorDistributions[1] = secondaryColorAmount;
        colorDistributions[2] = tertiaryColorAmount;
        invalidate();
    }

    private float primaryColorAmount = 0.50f; // Default Red
    private float secondaryColorAmount  = 0.8f; // Default Green
    private float tertiaryColorAmount  = 6.9f; // Default Blue

    public ColorBlendedCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setStyle(Paint.Style.STROKE);
    }

    public void setColors(int primaryColor, int secondaryColor, int tertiaryColor) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.tertiaryColor = tertiaryColor;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (bitmap == null || bitmapCanvas == null) {
            bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(bitmap);
        }

        drawColors(bitmapCanvas,getWidth(),getHeight());  // Draw the colors on the bitmap canvas
        canvas.drawBitmap(bitmap, 0, 0, null);  // Draw the bitmap on the view canvas
    }
    private void drawColors(Canvas canvas,int width , int height) {
        int radius = Math.min(width, height) / 2;

        // Draw the main circle with blended colors
        //RadialGradient gradient = new RadialGradient(width / 2f, height / 2f, radius,
        //        new int[]{primaryColor, secondaryColor, tertiaryColor, primaryColor,secondaryColor},
       //         new float[]{0.2f, 0.63f, 0.67f, 1f,0.5f}, Shader.TileMode.CLAMP);
       // paint.setShader(gradient);
        //canvas.drawCircle(width / 2f, height / 2f, radius, paint);

        // Draw the ring
        float ringWidth = radius * 0.25f; // Adjust the ring width as needed
        ringPaint.setStrokeWidth(ringWidth);
        float halfRingWidth = ringWidth / 2f;
        LinearGradient ringGradient = new LinearGradient(0,  0, 0, height,
                new int[]{primaryColor, secondaryColor, tertiaryColor},
                colorDistributions, Shader.TileMode.CLAMP);
        ringPaint.setShader(ringGradient);
        canvas.drawCircle(width / 2f, height / 2f, radius - ringWidth / 2, ringPaint);
    }

    public Bitmap getBitmap() {
        if (outputBitmap == null ) {
            outputBitmap = Bitmap.createBitmap(16, 16, Bitmap.Config.ARGB_8888);
            outputBitmapCanvas = new Canvas(outputBitmap);
        }

        drawColors(outputBitmapCanvas,16,16);
        return outputBitmap;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Invalidate bitmap and canvas on size change
        bitmap = null;
        bitmapCanvas = null;
    }
}
