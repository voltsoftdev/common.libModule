package com.dev.voltsoft.lib.view.video.controller;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.nineoldandroids.util.Property;

public class CircleProgressDrawable extends Drawable {

    private RectF mRectF;
    private Path mProgressPath;
    private Paint mStrokePaint;
    private int     mRadius;
    private int     mProgress = 80;

    private Property<CircleProgressDrawable, Integer> mSweepProperty
            = new Property<CircleProgressDrawable, Integer>(Integer.class, "progress") {
        @Override
        public Integer get(CircleProgressDrawable object) {
            return object.getProgress();
    }

        @Override
        public void set(CircleProgressDrawable object, Integer value) {
            object.setProgress(value);
        }
    };

    public CircleProgressDrawable(int radius) {

        mRadius = radius;

        mProgressPath = new Path();

        mStrokePaint = new Paint();
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(10);
        mStrokePaint.setColor(Color.WHITE);

        mRectF = new RectF(0, 0, (radius * 2), (radius * 2));
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        mProgressPath.reset();
        mProgressPath.offset(0, 0);

        int progressAngle = (int) (mProgress * (3.6 * 2));
        if (progressAngle < 360 && progressAngle >= 0) {

            mProgressPath.addArc(mRectF, -90, progressAngle);

            canvas.drawPath(mProgressPath, mStrokePaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mStrokePaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mStrokePaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int mProgress) {
        this.mProgress = mProgress;

        invalidateSelf();
    }
}
