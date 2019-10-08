package com.dev.voltsoft.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.dev.voltsoft.lib.R;

public class ScrollHeaderLayout extends CoordinatorLayout
{

    private int mToolBarLayoutResourceId;

    private FrameLayout mHeaderContainer;

    public ScrollHeaderLayout(@NonNull Context context)
    {
        super(context);

        init(context, null, 0);
    }

    public ScrollHeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        init(context, attrs, 0);
    }

    public ScrollHeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context c, AttributeSet attrs, int defStyle)
    {
        LayoutInflater.from(c).inflate(R.layout.view_scroll_header_layout_top, this);

        mHeaderContainer = findViewById(R.id.scrollHeaderContainer);

        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ScrollHeaderLayout, defStyle, 0);

        mToolBarLayoutResourceId = a.getResourceId(R.styleable.ScrollHeaderLayout_headerLayoutResource, 0);

        if (mToolBarLayoutResourceId != 0)
        {
            try
            {
                LayoutInflater.from(getContext()).inflate(mToolBarLayoutResourceId, mHeaderContainer);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        a.recycle();

        setWillNotDraw(false);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);


    }
}
