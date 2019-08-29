package com.dev.voltsoft.lib.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.*;
import android.text.style.RelativeSizeSpan;
import android.view.*;
import android.widget.*;
import com.dev.voltsoft.lib.R;
import com.dev.voltsoft.lib.utility.UtilityUI;

public abstract class CommonDialog extends Dialog
{
    protected static final int NONE_FRAME = -1;

    protected Context mContext;

    protected LinearLayout mDialogTitleLayout;

    protected TextView mDialogTitle;

    protected TextView mDialogMessage;

    protected FrameLayout mCloseButtonFrame;

    protected ImageView mDialogClose;

    protected ScrollView mScrollView;

    protected LinearLayout mDialogFrameLayout;

    private int mDialogResourceLayoutId;

    protected String mDialogTitleText;

    protected String mDialogMessageText;

    private boolean mDialogCanceled = true;

    public CommonDialog(Context context , int dialogResourceId)
    {
        super(context);

        mContext = context;

        mDialogResourceLayoutId = dialogResourceId;

        initBeanDialog();
    }

    public void show(Rect rect)
    {
        if (rect != null)
        {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT ,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(rect.left , rect.top , 0 , 0);
            mDialogFrameLayout.setLayoutParams(layoutParams);
            mDialogFrameLayout.requestLayout();
        }

        show();
    }

    private void initBeanDialog()
    {
        if (getWindow() != null)
        {
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.windowAnimations = R.style.CustomDialogAnimation;
            layoutParams.dimAmount = 0.5f;

            getWindow().setAttributes(layoutParams);
        }

        setCanceledOnTouchOutside(false);

        setContentView(R.layout.layout_dialog_scroll);

        mDialogTitleLayout = findViewById(R.id.bean_dialog_title_layout);

        mDialogTitle = findViewById(R.id.bean_dialog_title);
        mDialogTitle.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String text = mDialogTitle.getText().toString();

                int visibility = (TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);

                mDialogTitleLayout.setVisibility(visibility);
                mDialogTitle.setVisibility(visibility);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        mCloseButtonFrame = findViewById(R.id.bean_dialog_closeFrame);

        mDialogClose = findViewById(R.id.bean_dialog_close);
        mDialogClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setCancelable(true);

                dismiss();
            }
        });
        mDialogMessage = findViewById(R.id.bean_dialog_message);
        mDialogMessage.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String text = mDialogMessage.getText().toString();

                int visibility = (TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);

                mDialogMessage.setVisibility(visibility);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        mScrollView = findViewById(R.id.resizeableFrame);
        mScrollView.getLayoutParams().width = UtilityUI.getScreenWidth(getContext());

        mDialogFrameLayout = findViewById(R.id.bean_dialog_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (mDialogResourceLayoutId != -1)
        {
            LayoutInflater.from(mContext).inflate(mDialogResourceLayoutId, mDialogFrameLayout);
        }
    }

    public void addButtonLayout(Button button)
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT ,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(layoutParams);

        mDialogFrameLayout.addView(button);
    }

    public void addConfirmButton(View.OnClickListener onClickListener)
    {
        if (mContext != null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.view_confirm_button, null);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT ,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5 , 5 , 5 , 5);
            view.setLayoutParams(layoutParams);
            mDialogFrameLayout.addView(view);

            Button confirmButton = (Button) view.findViewById(R.id.buttonConfirm);
            UtilityUI.setClickListener(confirmButton , onClickListener);
        }
    }

    public void addDefaultPairButton(final View.OnClickListener onClickListener)
    {
        if (mContext != null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.view_confirm_cancel_button, null);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT ,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5 , 5 , 5 , 5);
            view.setLayoutParams(layoutParams);
            mDialogFrameLayout.addView(view);

            Button button1 = view.findViewById(R.id.buttonConfirm);

            UtilityUI.setClickListener(button1 , onClickListener);

            Button button2 = view.findViewById(R.id.buttonConfirm);

            UtilityUI.setClickListener(button2, new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (onClickListener != null)
                    {
                        onClickListener.onClick(v);
                    }

                    setCancelable(true);

                    dismiss();
                }
            });
        }
    }

    public void addButtonLayout(View button)
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT ,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0 , 5 , 0 , 5);
        button.setLayoutParams(layoutParams);

        mDialogFrameLayout.addView(button);
    }

    public void addButtonLayout(View ... buttons)
    {
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParams0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT ,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams0);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        mDialogFrameLayout.addView(linearLayout);

        for (View button : buttons)
        {
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT ,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.weight = 1;

            button.setLayoutParams(layoutParams1);
            linearLayout.addView(button);
        }
    }

    public void addTitleText(String strTitletText)
    {
        if (!TextUtils.isEmpty(strTitletText))
        {
            mDialogTitleLayout.setVisibility(View.VISIBLE);
            mDialogTitle.setText(strTitletText);
        }
    }

    public void addTitleText(int titleTextResource) {

        String title = getContext().getString(titleTextResource);

        if (!TextUtils.isEmpty(title))
        {
            mDialogTitleLayout.setVisibility(View.VISIBLE);
            mDialogTitle.setText(title);
        }
    }

    public void addMessageText(String strMessageText)
    {
        if (!TextUtils.isEmpty(strMessageText))
        {
            String strCurrentMessage = mDialogMessage.getText().toString();

            String strSetttingMessage = strCurrentMessage + strMessageText;

            mDialogMessage.setVisibility(View.VISIBLE);
            mDialogMessage.setText(strSetttingMessage);
        }
    }

    public void addMessageText(String strMessageText , float textSize)
    {
        if (!TextUtils.isEmpty(strMessageText))
        {
            String strCurrentMessage = (TextUtils.isEmpty(mDialogMessage.getText()) ? "" : mDialogMessage.getText().toString());

            String message = strCurrentMessage + strMessageText;

            SpannableString spannableString = new SpannableString(strMessageText);
            spannableString.setSpan(new RelativeSizeSpan(textSize) , 0 , message.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            mDialogMessage.setVisibility(View.VISIBLE);
            mDialogMessage.setText(spannableString);
        }
    }

    public boolean isDialogCanceled()
    {
        return mDialogCanceled;
    }

    public void setDialogCanceled(boolean dialogCanceled)
    {
        this.mDialogCanceled = dialogCanceled;
    }

    public LinearLayout getDialogFrame()
    {
        return mDialogFrameLayout;
    }

    public ImageView getCloseButton()
    {
        return mDialogClose;
    }

    public FrameLayout getCloseButtonFrame()
    {
        return mCloseButtonFrame;
    }
}
