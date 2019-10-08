package com.dev.voltsoft.lib.view.video.controller;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.dev.voltsoft.lib.R;
import com.dev.voltsoft.lib.view.video.constants.VideoStatus;
import com.dev.voltsoft.lib.view.video.player.CommonMediaPlayer;
import com.dev.voltsoft.lib.view.video.utils.PlayerUtil;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class PlayerBottomLayout extends FrameLayout implements IPlayerControllerComponent {

    private CommonMediaPlayer mCommonMediaPlayer;

    private VideoStatus mVideoStatus;

    private TextView mCurrentDurationView;
    private TextView mTotalDurationView;
    private SeekBar mSeekBar;
    private Button mToggleButton;

    public PlayerBottomLayout(@NonNull Context context) {
        super(context);

        init(context, null);
    }

    public PlayerBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public PlayerBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        inflate(context, R.layout.view_player_controller_bottom, this);

        mCurrentDurationView = findView(R.id.playerDurationView);
        mTotalDurationView = findView(R.id.playerTotalDurationView);
        mToggleButton = findView(R.id.playerOrientationToggle);
        mToggleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getContext() != null && getContext() instanceof Activity) {
                    Activity activity = (Activity) getContext();

                    if (v.isSelected()) {
                        v.setSelected(false);

                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    } else {
                        v.setSelected(true);

                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                }
            }
        });

        mSeekBar = findView(R.id.playerSeekBar);

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mVideoStatus != null) {

            switch (mVideoStatus) {
                case PLAYING:
                    long duration = mCommonMediaPlayer.getTimeDuration();
                    mTotalDurationView.setText(PlayerUtil.getTimeStampString(duration));

                    long currentTime = mCommonMediaPlayer.getCurrentTime();
                    mCurrentDurationView.setText(PlayerUtil.getTimeStampString(currentTime));
                    break;
            }
        }
    }

    @Override
    public void setVideoStatus(VideoStatus videoStatus) {

        if (mVideoStatus != videoStatus && videoStatus != null) {
            mVideoStatus = videoStatus;

            invalidate();
        }
    }

    @Override
    public VideoStatus getVideoStatus() {
        return mVideoStatus;
    }

    @Override
    public CommonMediaPlayer getCommonMediaPlayer() {
        return mCommonMediaPlayer;
    }

    @Override
    public void hide() {
        ViewPropertyAnimator.animate(this).alpha(0.0f);
    }

    @Override
    public void show() {
        ViewPropertyAnimator.animate(this).alpha(1.0f);
    }

    @Override
    public void setCommonMediaPlayer(CommonMediaPlayer commonMediaPlayer) {
        mCommonMediaPlayer = commonMediaPlayer;
    }

    public SeekBar getSeekBar() {
        return mSeekBar;
    }

    public void setSeekBar(SeekBar seekBar) {
        this.mSeekBar = seekBar;
    }

    public TextView getCurrentDurationView() {
        return mCurrentDurationView;
    }

    public TextView getTotalDurationView() {
        return mTotalDurationView;
    }

    @SuppressWarnings("unchecked")
    private <V extends View> V findView(int resourceId) {
        return (V) findViewById(resourceId);
    }
}
