package com.dev.voltsoft.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import java.util.HashMap;

/**
 * @author woozie
 */
public class AutoSizingTextView extends AppCompatTextView {

    public float mTextScaleX;

    public HashMap<String, Float> scaleXMap = new HashMap<>();

    public AutoSizingTextView(Context context)
    {
        super(context);
    }

    public AutoSizingTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public AutoSizingTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type)
    {
        mTextScaleX = getCacheScaleX(text.toString());

        setTextScaleX(mTextScaleX);

        super.setText(text, type);
    }

    private float getCacheScaleX(String s)
    {
        try
        {
            return scaleXMap.get(s);
        }
        catch (Exception e)
        {
            return 1.0f;
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int maxLine = getMaxLines();
        if (maxLine == Integer.MAX_VALUE || maxLine == -1)
        {
            maxLine = 1;
        }

        int textLineCount = getLineCount();
        if (textLineCount > maxLine)
        {
            mTextScaleX -= 0.05f;

            setTextScaleX(mTextScaleX);

            invalidate();
        }
        else
        {
            String text = getText().toString();

            scaleXMap.put(text, mTextScaleX);
        }
    }
}
