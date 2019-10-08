package com.dev.voltsoft.lib.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import com.dev.voltsoft.lib.R;

public class TextViewStroke extends AppCompatTextView {

	private boolean stroke = false;  
    private float strokeWidth = 0.0f;  
    private int strokeColor;  
	
	public TextViewStroke(Context context) {
		super(context);
	}

	public TextViewStroke(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initView(context, attrs); 
	}

	public TextViewStroke(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		initView(context, attrs); 
	}

	private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        stroke = a.getBoolean(R.styleable.CustomTextView_textStroke, false);
        strokeWidth = a.getFloat(R.styleable.CustomTextView_textStrokeWidth, 0.0f);  
        strokeColor = a.getColor(R.styleable.CustomTextView_textStrokeColor, 0xffffffff);         
    }  
  
    @Override
    protected void onDraw(Canvas canvas) {
          
        if (stroke) {  
            ColorStateList states = getTextColors();
            getPaint().setStyle(Style.STROKE);
            getPaint().setStrokeWidth(strokeWidth);  
            setTextColor(strokeColor);    
            super.onDraw(canvas);  
              
            getPaint().setStyle(Style.FILL);
            setTextColor(states);  
        }  
          
        super.onDraw(canvas);  
    }
}
