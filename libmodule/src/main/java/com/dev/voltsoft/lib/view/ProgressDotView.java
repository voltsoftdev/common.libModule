package com.dev.voltsoft.lib.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.dev.voltsoft.lib.R;
import com.dev.voltsoft.lib.utility.UtilityUI;


public class ProgressDotView extends FrameLayout {

    private static final String ID = "id";

    private Handler mHandler;
    private View mProgressDotFrame;

    private int mTargetDot = 0;

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

    private void init(Context context , AttributeSet attrs) {
        mProgressDotFrame = inflate(context, R.layout.view_progress_dot, this);
        mProgressDotFrame.setVisibility(View.GONE);

        mHandler = new Handler(context.getMainLooper());
    }

    public void startProgress() {
        mHandler.post(mRunnable);
    }

    public void stopProgress() {
        mHandler.removeCallbacks(mRunnable);

        UtilityUI.setVisibility(mProgressDotFrame , View.GONE);
    }
}
