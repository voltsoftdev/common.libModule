package com.dev.voltsoft.lib.view;

import android.content.Context;
import android.os.Handler;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.dev.voltsoft.lib.R;
import com.dev.voltsoft.lib.utility.UtilityUI;


public class ProgressDotView extends FrameLayout {

    private static final String ID = "id";

    private Handler mHandler;
    private View mProgressDotFrame;

    private int mTargetDot = 0;

    private TextView mProgressMessageView;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run()
        {
            if (mTargetDot >= 4)
            {
                mTargetDot = 0;
            }

            String packageName = getContext().getPackageName();

            int dotResourceId = getResources().getIdentifier("progressDot" + mTargetDot , ID , packageName);

            View progressDot = mProgressDotFrame.findViewById(dotResourceId);

            if (progressDot != null)
            {
                UtilityUI.setAnimationEffectBounce(progressDot);
            }

            mTargetDot += 1;

            mHandler.postDelayed(mRunnable , 600);
        }
    };

    public ProgressDotView(@NonNull Context context) {
        super(context);

        init(context , null);
    }

    public ProgressDotView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context , attrs);
    }

    public ProgressDotView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context , attrs);
    }

    private void init(Context context , AttributeSet attrs)
    {
        mProgressDotFrame = inflate(context, R.layout.view_progress_dot, this);
        mProgressDotFrame.setVisibility(View.VISIBLE);

        mProgressMessageView = findViewById(R.id.progressDotMessage);

        mHandler = new Handler(context.getMainLooper());
    }

    public void startProgress(String ... messages)
    {
        mHandler.post(mRunnable);

        String message = (messages != null && messages.length > 0 ? messages[0] : null);

        if (!TextUtils.isEmpty(message))
        {
            mProgressMessageView.setText(message);
        }
        else
        {
            mProgressMessageView.setVisibility(View.GONE);
        }

        UtilityUI.setVisibility(mProgressDotFrame , View.VISIBLE);
    }

    public void stopProgress()
    {
        mHandler.removeCallbacks(mRunnable);

        UtilityUI.setVisibility(mProgressDotFrame , View.GONE);
    }
}
