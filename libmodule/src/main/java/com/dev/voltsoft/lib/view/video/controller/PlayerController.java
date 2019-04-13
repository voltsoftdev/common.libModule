package com.dev.voltsoft.lib.view.video.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.dev.voltsoft.lib.R;
import com.dev.voltsoft.lib.view.video.constants.VideoStatus;
import com.dev.voltsoft.lib.view.video.player.CommonMediaPlayer;
import com.dev.voltsoft.lib.view.video.utils.PlayerUtil;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class PlayerController extends FrameLayout implements SeekBar.OnSeekBarChangeListener {

    private CommonMediaPlayer mCommonMediaPlayer;

    private FrameLayout mPlayerControllerLayout;
    private PlayerButton        mPlayerButton;
    private PlayerBottomLayout  mPlayerBottomLayout;
    private TextView mPlayerDurationView;

    private VideoStatus mVideoStatus;

    private boolean             mExposed;

    private Runnable mHideController = new Runnable() {
        @Override
        public void run() {
            hideController();
        }
    };

    private Runnable mTimeStamp = new Runnable() {
        @Override
        public void run() {

            int totalDuration = (int) mCommonMediaPlayer.getTimeDuration();
            int currentTime = (int) mCommonMediaPlayer.getCurrentTime();

            TextView textView = getCurrentDurationView();

            if (textView != null) {
                textView.setText(PlayerUtil.getTimeStampString(currentTime));
            }

            SeekBar seekBar = getSeekBar();

            if (seekBar != null && !seekBar.isHovered()) {
                seekBar.setProgress(((currentTime * 100) / totalDuration));
            }

            postDelayed(this, 100);
        }
    };

    public PlayerController(Context context) {
        super(context);

        init(context, null);
    }

    public PlayerController(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public PlayerController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.view_player_controller, this);

        mPlayerControllerLayout = findView(R.id.playerControllerLayout);
        mPlayerButton = findView(R.id.playerButton);
        mPlayerButton.setCommonMediaPlayer(mCommonMediaPlayer);

        mPlayerBottomLayout = findView(R.id.playerBottomLayout);
        mPlayerBottomLayout.setCommonMediaPlayer(mCommonMediaPlayer);
        mPlayerDurationView = findView(R.id.playerTimeDuration);

        SeekBar seekBar = mPlayerBottomLayout.getSeekBar();

        if (seekBar != null) {
            seekBar.setOnSeekBarChangeListener(this);
        }

        mExposed = true;

        mVideoStatus = VideoStatus.LOADING;

        setWillNotDraw(false);
    }

    public void exposeController() {

        if (mPlayerControllerLayout != null) {

            removeCallbacks(mHideController);

            int childCount = mPlayerControllerLayout.getChildCount();

            for (int i = 0; i < childCount; i++) {

                View childView = mPlayerControllerLayout.getChildAt(i);

                if (childView instanceof IPlayerControllerComponent) {

                    mExposed = true;

                    IPlayerControllerComponent playerControllerComponent = (IPlayerControllerComponent) childView;
                    playerControllerComponent.show();
                }
            }

            postDelayed(mHideController, 1000 * 5);
        }
    }

    public void hideController() {

        if (mPlayerControllerLayout != null) {

            int childCount = mPlayerControllerLayout.getChildCount();

            for (int i = 0; i < childCount; i++) {

                View childView = mPlayerControllerLayout.getChildAt(i);

                if (childView instanceof IPlayerControllerComponent) {

                    mExposed = false;

                    IPlayerControllerComponent playerControllerComponent = (IPlayerControllerComponent) childView;
                    playerControllerComponent.hide();
                }
            }
        }
    }

    public void changeLoadingMode() {

    }

    public CommonMediaPlayer getCommonMediaPlayer() {
        return mCommonMediaPlayer;
    }

    public void setCommonMediaPlayer(CommonMediaPlayer commonMediaPlayer) {
        this.mCommonMediaPlayer = commonMediaPlayer;

        if (mPlayerButton != null) {
            mPlayerButton.setCommonMediaPlayer(mCommonMediaPlayer);
        }

        if (mPlayerBottomLayout != null) {
            mPlayerBottomLayout.setCommonMediaPlayer(mCommonMediaPlayer);
        }
    }

    public PlayerButton getPlayerButton() {
        return mPlayerButton;
    }

    public void setPlayerButton(PlayerButton playerButton) {
        this.mPlayerButton = playerButton;
    }

    public boolean isVisible() {
        return mExposed;
    }

    public VideoStatus getVideoStatus() {
        return mVideoStatus;
    }

    public void setVideoStatus(VideoStatus videoStatus) {

        if (mVideoStatus != videoStatus && videoStatus != null) {
            mVideoStatus = videoStatus;

            mPlayerButton.setVideoStatus(mVideoStatus);

            mPlayerBottomLayout.setVideoStatus(mVideoStatus);

            switch (mVideoStatus) {
                case LOADING:
                case PAUSE:
                    hideController();
                    break;

                case PLAYING:
                    post(mTimeStamp);
                    break;
            }
        }
    }

    public SeekBar getSeekBar() {
        return (mPlayerBottomLayout == null ? null :
                mPlayerBottomLayout.getSeekBar());
    }

    public TextView getCurrentDurationView() {
        return (mPlayerBottomLayout == null ? null :
                mPlayerBottomLayout.getCurrentDurationView());
    }

    public TextView getTotalDurationView() {
        return (mPlayerBottomLayout == null ? null :
                mPlayerBottomLayout.getTotalDurationView());
    }

    @SuppressWarnings("unchecked")
    private <V extends View> V findView(int resourceId) {
        return (V) findViewById(resourceId);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (mVideoStatus != VideoStatus.LOADING && fromUser) {

            int visibility = mPlayerDurationView.getVisibility();
            if (visibility != View.VISIBLE) {
                mPlayerDurationView.setVisibility(View.VISIBLE);

                ViewPropertyAnimator.animate(mPlayerDurationView).alpha(1.0f);
                ViewPropertyAnimator.animate(mPlayerButton).alpha(0.0f);
            }

            int totalDuration = (int) mCommonMediaPlayer.getTimeDuration();
            int trackingTime = (totalDuration * progress) / 100;

            String trackingTimeString = PlayerUtil.getTimeStampString(trackingTime);

            mPlayerDurationView.setText(trackingTimeString);

            TextView textView = getCurrentDurationView();
            textView.setText(trackingTimeString);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        removeCallbacks(mHideController);
        removeCallbacks(mTimeStamp);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        if (mCommonMediaPlayer != null) {
            mCommonMediaPlayer.playVideo(seekBar.getProgress());
        }

        postDelayed(mHideController, 1000 * 5);

        int visibility = mPlayerDurationView.getVisibility();
        if (visibility != View.GONE) {
            mPlayerDurationView.setVisibility(View.GONE);

            ViewPropertyAnimator.animate(mPlayerDurationView).alpha(0.0f);
            ViewPropertyAnimator.animate(mPlayerButton).alpha(1.0f);
        }
    }
}
