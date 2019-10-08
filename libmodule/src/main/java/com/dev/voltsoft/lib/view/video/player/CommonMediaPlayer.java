package com.dev.voltsoft.lib.view.video.player;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.media.MediaDataSource;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.dev.voltsoft.lib.view.video.interfaces.IPlayerStateListener;

public abstract class CommonMediaPlayer extends FrameLayout
        implements Application.ActivityLifecycleCallbacks, View.OnTouchListener {

    private String mBelongActivityName;

    private IPlayerStateListener mPlayerStateListener;

    public CommonMediaPlayer(Context context) {
        super(context);

        init(context, null);
    }

    public CommonMediaPlayer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public CommonMediaPlayer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {

        if (context != null &&
            context instanceof Activity) {

            Activity activity = (Activity) context;
            activity.getApplication().registerActivityLifecycleCallbacks(this);

            mBelongActivityName = activity.getClass().getSimpleName();

            setOnTouchListener(this);
        }
    }

    public abstract void playVideo(String path);

    public abstract void playVideo(MediaDataSource mediaDataSource);

    public abstract void playVideo(Uri uri);

    public abstract void playVideo(int progress);

    public abstract void resumeVideo();

    public abstract void pauseVideo();

    public abstract void releaseVideo();

    public abstract long getTimeDuration();

    public abstract long getCurrentTime();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

        if (isBelongActivity(activity)) {

            resumeVideo();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

        if (isBelongActivity(activity)) {

            pauseVideo();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

        if (isBelongActivity(activity)) {

            releaseVideo();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mPlayerStateListener != null) {
            mPlayerStateListener.onVideoTouch();
        }

        return false;
    }

    protected boolean isBelongActivity(@NonNull Activity activity) {
        return (!TextUtils.isEmpty(mBelongActivityName) &&
                mBelongActivityName.equalsIgnoreCase(activity.getClass().getSimpleName()));
    }

    public IPlayerStateListener getPlayerStateListener() {
        return mPlayerStateListener;
    }

    public void setPlayerStateListener(IPlayerStateListener playerStateListener) {
        this.mPlayerStateListener = playerStateListener;
    }
}
