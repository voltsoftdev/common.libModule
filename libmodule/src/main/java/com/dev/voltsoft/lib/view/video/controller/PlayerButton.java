package com.dev.voltsoft.lib.view.video.controller;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import com.dev.voltsoft.lib.R;
import com.dev.voltsoft.lib.view.video.constants.VideoStatus;
import com.dev.voltsoft.lib.view.video.player.CommonMediaPlayer;
import com.dev.voltsoft.lib.view.video.utils.PlayerUtil;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class PlayerButton extends AppCompatImageView implements View.OnClickListener, IPlayerControllerComponent {

    private CommonMediaPlayer mCommonMediaPlayer;

    private VideoStatus mVideoStatus;

    private RectF mRectF;
    private Path mProgressPath;
    private Path mProgressPathBack;
    private Paint mProgressPaint;
    private Paint mProgressBackPaint;
    private int         mProgress = 0;

    private Runnable mProgressUpdate = new Runnable() {
        @Override
        public void run() {
            if (mProgress <= 360 && mProgress >= 0) {
                mProgress += 1;
            } else {
                mProgress = 0;
            }

            invalidate();
        }
    };

    public PlayerButton(Context context) {
        super(context);

        init(context, null);
    }

    public PlayerButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public PlayerButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {

        int[] attrsArray = new int[] {
                android.R.attr.id,
                android.R.attr.background,
                android.R.attr.layout_width,
                android.R.attr.layout_height
        };
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);

        int width = ta.getDimensionPixelSize(2, 0);
        int height = ta.getDimensionPixelSize(3, 0);

        mRectF = new RectF(30, 30, (width - 30), (height - 30
        ));

        mVideoStatus = VideoStatus.LOADING;

        mProgressPathBack = new Path();
        mProgressPathBack.addArc(mRectF, -90, 360);
        mProgressBackPaint = new Paint();
        mProgressBackPaint.setAntiAlias(true);
        mProgressBackPaint.setStyle(Paint.Style.STROKE);
        mProgressBackPaint.setStrokeWidth(30);
        mProgressBackPaint.setColor(Color.GRAY);
        mProgressPathBack.offset(0, 0);
        mProgressPathBack.addArc(mRectF, 0, 360);

        mProgressPath = new Path();
        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(30);
        mProgressPaint.setColor(Color.WHITE);

        setOnClickListener(this);

        setWillNotDraw(false);

        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mVideoStatus != null) {

            switch (mVideoStatus) {
                case LOADING:
                    PlayerUtil.setBackGroundDrawable(this, null);

                    canvas.drawPath(mProgressPathBack, mProgressBackPaint);

                    mProgressPath.reset();
                    mProgressPath.offset(0, 0);
                    mProgressPath.addArc(mRectF, -90, mProgress);

                    canvas.drawPath(mProgressPath, mProgressPaint);

                    post(mProgressUpdate);
                    break;

                case PAUSE:
                    PlayerUtil.setBackGroundDrawable(this, R.drawable.play_btn_n);
                    break;

                case PLAYING:
                    PlayerUtil.setBackGroundDrawable(this, R.drawable.pause_btn_n);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (mVideoStatus != null &&
            mVideoStatus != VideoStatus.LOADING) {

            switch (mVideoStatus) {
                case PAUSE:
                    setVideoStatus(VideoStatus.PLAYING);

                    if (mCommonMediaPlayer != null) {
                        mCommonMediaPlayer.resumeVideo();
                    }
                    break;

                case PLAYING:
                    setVideoStatus(VideoStatus.PAUSE);

                    if (mCommonMediaPlayer != null) {
                        mCommonMediaPlayer.pauseVideo();
                    }
                    break;
            }
        }
    }
    @Override
    public VideoStatus getVideoStatus() {
        return mVideoStatus;
    }

    @Override
    public void setVideoStatus(VideoStatus videoStatus) {

        if (mVideoStatus != videoStatus && videoStatus != null) {
            mVideoStatus = videoStatus;

            removeCallbacks(mProgressUpdate);

            invalidate();
        }
    }

    @Override
    public CommonMediaPlayer getCommonMediaPlayer() {
        return mCommonMediaPlayer;
    }

    @Override
    public void hide() {

        if (mVideoStatus != VideoStatus.LOADING) {
            ViewPropertyAnimator.animate(this).alpha(0.0f);
        }
    }

    @Override
    public void show() {
        ViewPropertyAnimator.animate(this).alpha(1.0f);
    }

    @Override
    public void setCommonMediaPlayer(CommonMediaPlayer commonMediaPlayer) {
        mCommonMediaPlayer = commonMediaPlayer;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;

        invalidate();
    }
}
