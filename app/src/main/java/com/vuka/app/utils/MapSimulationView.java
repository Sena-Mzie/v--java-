package com.vuka.app.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom map simulation view for demonstration purposes.
 *
 * IMPORTANT: This is a visual simulation only.
 * It does NOT represent real GPS coordinates or actual routes.
 * Always labeled "MAP SIMULATION — Not real GPS" in the UI.
 */
public class MapSimulationView extends View {

    private Paint gridPaint;
    private Paint routePaint;
    private Paint vehiclePaint;
    private Paint labelPaint;
    private Paint dotPaint;
    private Paint deviationPaint;

    private float vehicleX;
    private float vehicleY;
    private boolean animateVehicle = true;
    private boolean showDeviation = false;
    private float animationProgress = 0f;
    private float deviationOffset = 0f;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable animRunnable;

    public MapSimulationView(Context context) {
        super(context);
        init();
    }

    public MapSimulationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MapSimulationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(Color.parseColor("#1A2254"));
        gridPaint.setStrokeWidth(1f);
        gridPaint.setStyle(Paint.Style.STROKE);

        routePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        routePaint.setColor(Color.parseColor("#F5A623")); // VUKA gold
        routePaint.setStrokeWidth(6f);
        routePaint.setStyle(Paint.Style.STROKE);
        routePaint.setStrokeCap(Paint.Cap.ROUND);
        routePaint.setStrokeJoin(Paint.Join.ROUND);

        deviationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        deviationPaint.setColor(Color.parseColor("#FF6D00")); // orange deviation
        deviationPaint.setStrokeWidth(4f);
        deviationPaint.setStyle(Paint.Style.STROKE);
        deviationPaint.setStrokeCap(Paint.Cap.ROUND);

        vehiclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        vehiclePaint.setColor(Color.parseColor("#F5A623"));

        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setColor(Color.parseColor("#00C853"));

        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setColor(Color.parseColor("#B0B8D4"));
        labelPaint.setTextSize(24f);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    private void startAnimation() {
        if (!animateVehicle) return;
        animRunnable = new Runnable() {
            @Override
            public void run() {
                animationProgress += 0.005f;
                if (animationProgress > 1f) animationProgress = 0f;
                invalidate();
                handler.postDelayed(this, 50);
            }
        };
        handler.post(animRunnable);
    }

    private void stopAnimation() {
        if (animRunnable != null) {
            handler.removeCallbacks(animRunnable);
        }
    }

    public void setShowDeviation(boolean deviation) {
        this.showDeviation = deviation;
        invalidate();
    }

    public void setAnimateVehicle(boolean animate) {
        this.animateVehicle = animate;
        if (animate) startAnimation();
        else stopAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();

        if (w == 0 || h == 0) return;

        // Background (already set by XML)
        canvas.drawColor(Color.parseColor("#0D1A3A"));

        // Grid lines (city streets simulation)
        drawGrid(canvas, w, h);

        // Main route
        drawRoute(canvas, w, h);

        // Deviation route if applicable
        if (showDeviation) {
            drawDeviation(canvas, w, h);
        }

        // Animated vehicle marker
        drawVehicle(canvas, w, h);

        // Destination marker
        drawDestination(canvas, w, h);
    }

    private void drawGrid(Canvas canvas, int w, int h) {
        // Horizontal lines
        for (int y = 0; y < h; y += h / 5) {
            canvas.drawLine(0, y, w, y, gridPaint);
        }
        // Vertical lines
        for (int x = 0; x < w; x += w / 4) {
            canvas.drawLine(x, 0, x, h, gridPaint);
        }
    }

    private void drawRoute(Canvas canvas, int w, int h) {
        Path routePath = new Path();
        routePath.moveTo(w * 0.1f, h * 0.8f);
        routePath.cubicTo(
                w * 0.3f, h * 0.7f,
                w * 0.5f, h * 0.4f,
                w * 0.9f, h * 0.2f
        );
        canvas.drawPath(routePath, routePaint);
    }

    private void drawDeviation(Canvas canvas, int w, int h) {
        Path deviationPath = new Path();
        // Show a detour branch
        float progressX = w * (0.1f + animationProgress * 0.5f);
        float progressY = h * (0.8f - animationProgress * 0.3f);
        deviationPath.moveTo(progressX, progressY);
        deviationPath.quadTo(
                w * 0.6f, h * 0.7f,
                w * 0.75f, h * 0.5f
        );
        canvas.drawPath(deviationPath, deviationPaint);
    }

    private void drawVehicle(Canvas canvas, int w, int h) {
        // Interpolate vehicle position along the route
        vehicleX = w * (0.1f + animationProgress * 0.8f);
        // Approximate route curve
        float t = animationProgress;
        float startY = h * 0.8f;
        float endY = h * 0.2f;
        vehicleY = startY + (endY - startY) * t;

        // Vehicle glow
        Paint glowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        glowPaint.setColor(Color.parseColor("#33F5A623"));
        canvas.drawCircle(vehicleX, vehicleY, 20f, glowPaint);

        // Vehicle dot
        canvas.drawCircle(vehicleX, vehicleY, 10f, vehiclePaint);
    }

    private void drawDestination(Canvas canvas, int w, int h) {
        float destX = w * 0.9f;
        float destY = h * 0.2f;

        dotPaint.setColor(Color.parseColor("#00C853"));
        canvas.drawCircle(destX, destY, 12f, dotPaint);

        // Label
        labelPaint.setTextSize(22f);
        canvas.drawText("📍", destX - 10f, destY - 20f, labelPaint);
    }
}
