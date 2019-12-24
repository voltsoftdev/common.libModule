package com.dev.voltsoft.lib.view.insert;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.voltsoft.lib.R;
import com.dev.voltsoft.lib.utility.UtilityUI;
import com.dev.voltsoft.lib.view.VisibleTextWatcher;

public class InsertForm extends LinearLayout
{
    private LinearLayout InsertFormContainer;

    private TextView TitleView;
    private TextView ErrorView;

    private TextView DesciptionView;

    private EditText InsertView;

    private String mTitle;
    private String mFormHint;
    private String mDescription;

    private float  mTitleSize;
    private float  mTextSize;
    private float  mFormPaddingLeft;
    private float  mFormPaddingTop;
    private float  mFormPaddingBottom;

    private int mTextType;
    private int mImeOptionType;
    private int mThemeColor;
    private int mShapeColor;
    private int mFormHintColor;
    private int mOrientation;

    private int mFormBackgroundResource;

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

        InsertView = find(R.id.insertForm);
        InsertView.setOnFocusChangeListener(new OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                InsertView.setSelected(hasFocus);
            }
        });

        TitleView = find(R.id.insertFormTitle);
        TitleView.addTextChangedListener(new VisibleTextWatcher(TitleView));

        DesciptionView = find(R.id.descriptionView);
        DesciptionView.addTextChangedListener(new VisibleTextWatcher(DesciptionView));

        ErrorView = find(R.id.errorNotificationView);
        ErrorView.addTextChangedListener(new VisibleTextWatcher(ErrorView));

        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.InsertForm, defStyle, 0);

        mTitle = a.getString(R.styleable.InsertForm_formTitle);

        TitleView.addTextChangedListener(new VisibleTextWatcher(TitleView));
        TitleView.setText(mTitle);

        mFormHint = a.getString(R.styleable.InsertForm_formHint);

        InsertView.setHint(mFormHint);

        mDescription = a.getString(R.styleable.InsertForm_formDescription);

        DesciptionView.setText(mDescription);

        float defaultTitleSize = c.getResources().getDimensionPixelSize(R.dimen.dp14);
        float defaultTextSize = c.getResources().getDimensionPixelSize(R.dimen.dp14);

        mTitleSize = a.getDimensionPixelSize(R.styleable.InsertForm_formTitleSize, (int) defaultTitleSize);

        TitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);

        mTextSize = a.getDimensionPixelSize(R.styleable.InsertForm_formInsertTextSize, (int) defaultTextSize);

        InsertView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

        mFormPaddingBottom = a.getDimension(R.styleable.InsertForm_formInsertPaddingBottom, c.getResources().getDimension(R.dimen.dp10));
        mFormPaddingTop = a.getDimension(R.styleable.InsertForm_formInsertPaddingTop, c.getResources().getDimension(R.dimen.dp10));
        mFormPaddingLeft = a.getDimension(R.styleable.InsertForm_formInsertPaddingLeft, c.getResources().getDimension(R.dimen.dp10));

        InsertView.setPadding((int) mFormPaddingLeft, (int) mFormPaddingTop, 0, (int) mFormPaddingBottom);

        mThemeColor = a.getColor(R.styleable.InsertForm_formThemeColor, UtilityUI.getColor(c, R.color.color_white));
        mShapeColor = a.getColor(R.styleable.InsertForm_formThemeColor, UtilityUI.getColor(c, R.color.color_white));

        mFormHintColor = a.getColor(R.styleable.InsertForm_formHintColor, UtilityUI.getColor(c, R.color.color_c3c3c3));

        InsertView.setHintTextColor(mFormHintColor);

        mOrientation = a.getInt(R.styleable.InsertForm_formOrientation, 0);

        if (mOrientation != 0)
        {
            InsertFormContainer.setOrientation((mOrientation == 1 ? HORIZONTAL : VERTICAL));
        }

        mTextType = a.getInt(R.styleable.InsertForm_formInsertTextType, 0);

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

            case 4:
                InsertView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                break;
        }

        mImeOptionType = a.getInt(R.styleable.InsertForm_formImeOptionType, 0);

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

        mFormBackgroundResource = a.getResourceId(R.styleable.InsertForm_formInsertFormDrawable, -1);

        if (mFormBackgroundResource != -1)
        {
            InsertView.setBackgroundResource(mFormBackgroundResource);
        }

        TitleView.setText(mTitle);

        int titleColor = a.getColor(R.styleable.InsertForm_formTitleColor, Color.BLACK);

        TitleView.setTextColor(titleColor);

        DesciptionView.setTextColor(mThemeColor);

        a.recycle();

        setWillNotDraw(false);
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

        InsertView.setOnEditorActionListener(mEditorActionListener);

        invalidate();
    }

    public void setErrorMessage(String s)
    {
        ErrorView.setText(s);
    }
}
