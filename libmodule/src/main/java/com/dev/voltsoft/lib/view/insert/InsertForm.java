package com.dev.voltsoft.lib.view.insert;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dev.voltsoft.lib.R;
import com.dev.voltsoft.lib.utility.UtilityUI;

public class InsertForm extends LinearLayout {

    private LinearLayout InsertFormContainer;
    private TextView TitleView;
    private EditText InsertView;

    private String      mTitle;
    private float       mTitleSize;
    private float       mTextSize;
    private int         mTextType;
    private int         mImeOptionType;
    private int         mThemeColor;
    private int         mShapeColor;
    private int         mOrientation;
    private int         mFormBackgroundResource;

    private TextView.OnEditorActionListener     mEditorActionListener;

    public InsertForm(Context context)
    {
        super(context);

        init(context, null, 0);
    }

    public InsertForm(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init(context, attrs, 0);
    }

    public InsertForm(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context c, AttributeSet attrs, int defStyle)
    {
        LayoutInflater.from(c).inflate(R.layout.view_insert_form, this);

        InsertFormContainer = find(R.id.insertFormLayout);
        TitleView = find(R.id.title_view);
        InsertView = find(R.id.insert_form);

        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.InsertForm, defStyle, 0);

        mTitle = a.getString(R.styleable.InsertForm_title);

        float defaultTitleSize = c.getResources().getDimension(R.dimen.dp7);
        float defaultTextSize = c.getResources().getDimension(R.dimen.dp18);
        mTitleSize = a.getDimension(R.styleable.InsertForm_titleSize, defaultTitleSize);
        mTextSize = a.getDimension(R.styleable.InsertForm_insertTextSize, defaultTextSize);
        mThemeColor = a.getColor(R.styleable.InsertForm_themeColor,
                UtilityUI.getColor(c, R.color.insert_form_default_color));
        mShapeColor = a.getColor(R.styleable.InsertForm_themeColor,
                UtilityUI.getColor(c, R.color.color_white));

        mOrientation = a.getInt(R.styleable.InsertForm_orientation, 0);

        mTextType = a.getInt(R.styleable.InsertForm_insertTextType, 0);

        mImeOptionType = a.getInt(R.styleable.InsertForm_imeOptionType, 0);

        a.recycle();

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (mOrientation != 0)
        {
            InsertFormContainer.setOrientation((mOrientation == 1 ? HORIZONTAL : VERTICAL));
        }

        TitleView.setText(mTitle);
        TitleView.setTextColor(mThemeColor);

        InsertView.setOnEditorActionListener(mEditorActionListener);

        GradientDrawable gradientDrawable = (GradientDrawable) InsertView.getBackground();

        gradientDrawable.setColor(mShapeColor);
        gradientDrawable.setStroke(UtilityUI.getDimension(getContext(), R.dimen.dp1) ,mThemeColor);
        gradientDrawable.invalidateSelf();

        switch (mTextType)
        {
            case 0:
                InsertView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;

            case 1:
                InsertView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;

            case 2:
                InsertView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
                break;

            case 3:
                InsertView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
                break;
        }

        switch (mImeOptionType)
        {
            case 0:
                InsertView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                // InsertView.setSingleLine(false);
                break;

            case 1:
                InsertView.setImeOptions(EditorInfo.IME_ACTION_DONE);
                InsertView.setSingleLine();
                break;
        }
    }

    public TextView getTitleView()
    {
        return TitleView;
    }

    public EditText getInsertView()
    {
        return InsertView;
    }

    public String getInsertedText()
    {
        return (InsertView != null ? InsertView.getText().toString() : null);
    }

    @SuppressWarnings("unchecked")
    protected <V extends View> V find(int id)
    {
        return (V) findViewById(id);
    }

    public TextView.OnEditorActionListener getEditorActionListener()
    {
        return mEditorActionListener;
    }

    public void setEditorActionListener(TextView.OnEditorActionListener actionListener)
    {
        this.mEditorActionListener = actionListener;

        invalidate();
    }
}
