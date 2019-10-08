package com.dev.voltsoft.lib.view;

import android.content.Context;
import android.graphics.Rect;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.dev.voltsoft.lib.R;
import com.dev.voltsoft.lib.utility.UtilityUI;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class PageIndicator extends FrameLayout {

    private ViewPager mViewPager;
    private View mIndicator;

    private int mSelectedPage;
    private float mIndicatorLeft;
    private float mIndicatorWidth;

    private Runnable mInitRunnable;

    public PageIndicator(Context context) {
        super(context);

        init(context , null);
    }

    public PageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context , attrs);
    }

    public PageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context , attrs);
    }

    private void init(Context context , AttributeSet attrs) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_pageindicator_layout , this);

        mIndicator = view.findViewById(R.id.pageindicator);

        for (int i = 0 ; i < 3 ; i++) {
            int pageResId = getResources().getIdentifier("pagetitle" + i , "id" , getContext().getPackageName());
            TextView pageView = (TextView) findViewById(pageResId);
            pageView.setTag(i);
            pageView.setTextColor(getResources().getColor(R.color.color_white50));
            pageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int page = (int) v.getTag();

                    if (getViewPager() != null) {
                        getViewPager().setCurrentItem(page);
                    }
                }
            });
        }
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;

        if (mViewPager != null) {
            mInitRunnable = new Runnable() {
                @Override
                public void run() {
                    int selectedPage = mViewPager.getCurrentItem();
                    onPageSelected(selectedPage);
                }
            };
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        if (mInitRunnable != null) {
            post(mInitRunnable);
        }
    }

    public void onPageSelected(int position) {
        mSelectedPage = position;

        for (int i = 0 ; i < 3 ; i++) {
            int pageResId = getResources().getIdentifier("pagetitle" + i , "id" , getContext().getPackageName());
            TextView pageView = (TextView) findViewById(pageResId);

            if (i == mSelectedPage) {
                pageView.setTextColor(getResources().getColor(R.color.color_white));

                Rect rect = new Rect();
                pageView.getGlobalVisibleRect(rect);

                mIndicatorLeft = rect.left;
                mIndicatorLeft -= 27;
                mIndicatorWidth = rect.width();
                mIndicatorWidth += 60;

                if (mIndicatorWidth != 0) {
                    mIndicator.getLayoutParams().width = (int) mIndicatorWidth;
                    mIndicator.requestLayout();
                }
                ViewPropertyAnimator.animate(mIndicator).x(mIndicatorLeft).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        UtilityUI.setVisibility(mIndicator , View.VISIBLE);
                    }
                });
            } else {
                pageView.setTextColor(getResources().getColor(R.color.color_white50));
            }
        }
    }
}
