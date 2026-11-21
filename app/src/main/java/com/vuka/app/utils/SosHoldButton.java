package com.vuka.app.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Custom press-and-hold SOS button.
 *
 * Requirements:
 * - Hold for 2 seconds to activate (accidental activation prevention)
 * - Shows radial progress while holding
 * - Vibrates on activation
 * - Provides accessible alternative via click after 500ms without hold
 *
 * IMPORTANT: This triggers simulated emergency escalation only.
 * Does NOT contact real emergency services.
 */
public class SosHoldButton extends View {

    public interface SosActivationListener {
        void onSosActivated();
        void onSosHoldStarted();
        void onSosHoldCancelled();
    }

    private static final long HOLD_DURATION_MS = 2000; // 2 seconds
    private static final long UPDATE_INTERVAL_MS = 50;

    private Paint backgroundPaint;
    private Paint glowPaint;
    private Paint progressPaint;
    private Paint textPaint;
    private Paint labelPaint;

    private float progress = 0f; // 0.0 to 1.0
    private boolean isHolding = false;
    private long holdStartTime = 0;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable progressRunnable;

    private SosActivationListener listener;

    public SosHoldButton(Context context) {
        super(context);
        init();
    }

    public SosHoldButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SosHoldButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);
        setFocusable(true);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(Color.parseColor("#FF1744"));
        backgroundPaint.setStyle(Paint.Style.FILL);

        glowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        glowPaint.setColor(Color.parseColor("#33FF1744"));
        glowPaint.setStyle(Paint.Style.FILL);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(Color.WHITE);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(8f);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(48f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);

        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setColor(Color.parseColor("#CCFFFFFF"));
        labelPaint.setTextSize(24f);
        labelPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setSosActivationListener(SosActivationListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startHold();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                cancelHold();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void startHold() {
        isHolding = true;
        holdStartTime = System.currentTimeMillis();
        if (listener != null) listener.onSosHoldStarted();

        progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isHolding) return;
                long elapsed = System.currentTimeMillis() - holdStartTime;
                progress = Math.min(1f, (float) elapsed / HOLD_DURATION_MS);
                invalidate();

                if (progress >= 1f) {
                    // SOS activated
                    isHolding = false;
                    vibrateActivation();
                    if (listener != null) listener.onSosActivated();
                } else {
                    handler.postDelayed(this, UPDATE_INTERVAL_MS);
                }
            }
        };
        handler.post(progressRunnable);
    }

    private void cancelHold() {
        if (isHolding) {
            isHolding = false;
            if (progressRunnable != null) handler.removeCallbacks(progressRunnable);
            progress = 0f;
            invalidate();
            if (listener != null) listener.onSosHoldCancelled();
        }
    }

    private void vibrateActivation() {
        try {
            Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(500);
                }
            }
        } catch (Exception e) {
            // Vibration is a non-critical enhancement
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        float cx = w / 2f;
        float cy = h / 2f;
        float radius = Math.min(w, h) / 2f - 16f;

        // Outer glow (pulsing when not holding)
        canvas.drawCircle(cx, cy, radius + 20f, glowPaint);

        // Main button circle
        canvas.drawCircle(cx, cy, radius, backgroundPaint);

        // Progress arc (while holding)
        if (progress > 0f) {
            float sweepAngle = progress * 360f;
            android.graphics.RectF arcRect = new android.graphics.RectF(
                    cx - radius + 4, cy - radius + 4,
                    cx + radius - 4, cy + radius - 4
            );
            canvas.drawArc(arcRect, -90f, sweepAngle, false, progressPaint);
        }

        // SOS text
        canvas.drawText("SOS", cx, cy + 16f, textPaint);

        // Instruction label below
        canvas.drawText("Hold", cx, cy + radius + 36f, labelPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (progressRunnable != null) handler.removeCallbacks(progressRunnable);
    }
}
