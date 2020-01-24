package com.dev.voltsoft.lib.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dev.voltsoft.lib.R;
import com.dev.voltsoft.lib.utility.UtilityUI;

public class StrokeTextView extends AutoSizingTextView
{
    public float mTextScaleX;

    public boolean mbStroke = false;

    public int StrokeColor = Color.parseColor("#a85ebd");

    public StrokeTextView(@NonNull Context context)
    {
        super(context);

        init(context, null, -1);
    }

    public StrokeTextView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        init(context, attrs, -1);
    }

    public StrokeTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context c, AttributeSet attrs, int defStyle)
    {
        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.StrokeTextView, defStyle, 0);

        StrokeColor = a.getColor(R.styleable.StrokeTextView_strokeColor, UtilityUI.getColor(c, R.color.color_c3c3c3));

        a.recycle();
    }

    @Override
    public void setText(CharSequence text, BufferType type)
    {
        mTextScaleX = 1.0f;

        setTextScaleX(mTextScaleX);

        super.setText(text, type);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (mbStroke)
        {
            ColorStateList states = getTextColors(); // text color 값 저장

            // stroke 그리기
            getPaint().setStyle(Paint.Style.STROKE);
            getPaint().setStrokeWidth(4.0f);
            setTextColor(StrokeColor);

            super.onDraw(canvas);

            // stroke 위에 그리기
            getPaint().setStyle(Paint.Style.FILL);
            setTextColor(states);
            setTextColor(Color.BLACK);

            super.onDraw(canvas);
        }
        else
        {
            super.onDraw(canvas);
        }
    }

    public void setStroke(boolean b)
    {
        this.mbStroke = b;

        invalidate();
    }
}
