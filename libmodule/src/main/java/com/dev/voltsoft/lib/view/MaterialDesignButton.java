package com.dev.voltsoft.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import com.dev.voltsoft.lib.R;
import com.dev.voltsoft.lib.utility.UtilityUI;

public class MaterialDesignButton extends androidx.appcompat.widget.AppCompatButton
{
    private int ShapeColor;
    private int StorkeColor;
    private float ConnerRoundRadius;

    public MaterialDesignButton(Context context)
    {
        super(context);

        init(context, null, 0);
    }

    public MaterialDesignButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init(context, attrs, 0);
    }

    public MaterialDesignButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context c, AttributeSet attrs, int defStyle)
    {
        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.MaterialDesignButton, defStyle, 0);

        ShapeColor = a.getColor(R.styleable.MaterialDesignButton_materialBackgroundColor, -1);

        StorkeColor = a.getColor(R.styleable.MaterialDesignButton_materialBackgroundColor, -1);

        if (ShapeColor == -1)
        {
            ShapeColor = UtilityUI.getColor(c, R.color.insert_form_default_color);
        }

        if (StorkeColor == -1)
        {
            StorkeColor = UtilityUI.getColor(c, R.color.insert_form_default_color);
        }

        ConnerRoundRadius = a.getDimension(R.styleable.MaterialDesignButton_connerRoundRadius,
                UtilityUI.getDimension(c, R.dimen.dp1));

        a.recycle();

        setWillNotDraw(false);

        setBackgroundDrawable(UtilityUI.getDrawable(c, R.drawable.shape_material_rect11));
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        GradientDrawable gradientDrawable = (GradientDrawable) getBackground();

        gradientDrawable.setColor(ShapeColor);
        gradientDrawable.setCornerRadius(ConnerRoundRadius);
        gradientDrawable.setStroke(UtilityUI.getDimension(getContext(), R.dimen.dp1) , StorkeColor);
        gradientDrawable.invalidateSelf();

        setBackgroundDrawable(gradientDrawable);

        setWillNotDraw(true);
    }
}
