package com.dev.voltsoft.lib.view.sketchbook;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.dev.voltsoft.lib.R;
import com.dev.voltsoft.lib.view.ImageLoaderView;

public class SketchBookView extends FrameLayout 
{
    private ImageLoaderView         BackGroundView;

    public SketchBookView(Context context)
    {
        super(context);

        init(context, null, 0);
    }

    public SketchBookView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init(context, attrs, 0);
    }

    public SketchBookView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }


    private Paint paint;
    private Path path;

    public void init(Context c , AttributeSet attrs, int defStyleAttr)
    {
        LayoutInflater.from(c).inflate(R.layout.view_sketch_book, this);

        BackGroundView = findViewById(R.id.backView);
        BackGroundView.setAlpha(0.1f);


        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5f);

        this.path = new Path();

        setWillNotDraw(false);
        // DrawingView = findViewById(R.id.drawingView);
    }

    public void loadBackGroundImage(String path)
    {
        if (BackGroundView != null)
        {
            BackGroundView.loadImage(path);
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                return true;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                path.lineTo(eventX, eventY);
                break;

            default:
                return false;
        }

        // Schedules a repaint.
        invalidate();
        return true;
    }

    public void clear()
    {
        path.reset();
        invalidate();
    }

    public void setPaintColor(int color)
    {
        paint.setColor(color);
    }

    public int getCurrentPaintColor()
    {
        return paint.getColor();
    }
}
