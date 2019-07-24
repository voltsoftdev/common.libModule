package com.dev.voltsoft.lib.view.video;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.MediaDataSource;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import com.dev.voltsoft.lib.R;
import com.dev.voltsoft.lib.view.video.constants.PlayerType;
import com.dev.voltsoft.lib.view.video.constants.VideoStatus;
import com.dev.voltsoft.lib.view.video.controller.PlayerController;
import com.dev.voltsoft.lib.view.video.interfaces.IPlayerStateListener;
import com.dev.voltsoft.lib.view.video.player.CommonMediaPlayer;
import com.dev.voltsoft.lib.view.video.player.GoogleExoPlayer;

public class VideoPlayerDecorator extends CommonMediaPlayer implements IPlayerStateListener {

    private CommonMediaPlayer mCommonMediaPlayer;

    private PlayerController mPlayerController;

    private PlayerType mPlayerType;

    private FrameLayout mPlayerContainer;

    public VideoPlayerDecorator(Context context) {
        super(context);

        init(context, null);
    }

    public VideoPlayerDecorator(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public VideoPlayerDecorator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        inflate(context, R.layout.view_player_container, this);

        mPlayerContainer = findView(R.id.playerContainer);

        mPlayerController = findView(R.id.playerController);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VideoPlayerDecorator);

        int playerTypeValue = 0;

        if (typedArray.hasValue(R.styleable.VideoPlayerDecorator_playerType))
        {
            playerTypeValue = typedArray.getInt(R.styleable.VideoPlayerDecorator_playerType, 0);
        }

        setPlayer(playerTypeValue);

        typedArray.recycle();

        setWillNotDraw(false);
    }

    private void setPlayer(int playerTypeValue)
    {
        PlayerType playerType = null;

        switch (playerTypeValue)
        {
            case 0 : {
                playerType = PlayerType.GOOGLE_EXO_PLAYER;
                break;
            }

            case 1 : {
                playerType = PlayerType.FFMPEG_PLAYER;
                break;
            }

            case 2 : {
                playerType = PlayerType.DEFAULT_PLAYER;
                break;
            }
        }

        if (playerType != null) {

            setPlayer(playerType);
        }
    }

    public void setPlayer(PlayerType playerType)
    {
        Context c = getContext();

        if (c != null)
        {

            if (mPlayerType != playerType && playerType != null)
            {
                mPlayerType = playerType;

                if (mCommonMediaPlayer != null)
                {
                    mPlayerContainer.removeView(mCommonMediaPlayer);
                }

                switch (mPlayerType)
                {
                    case GOOGLE_EXO_PLAYER:
                        mCommonMediaPlayer = new GoogleExoPlayer(c);
                        break;

                    case CUSTOM_EXO_PLAYER:
                        break;

                    case FFMPEG_PLAYER:
                        break;

                    case DEFAULT_PLAYER:
                        break;
                }

                if (mCommonMediaPlayer != null) {

                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT);
                    mCommonMediaPlayer.setLayoutParams(layoutParams);
                    mCommonMediaPlayer.setPlayerStateListener(this);

                    mPlayerController.setCommonMediaPlayer(mCommonMediaPlayer);

                    mPlayerContainer.addView(mCommonMediaPlayer);
                }
            }
        }
    }

    public CommonMediaPlayer getCommonMediaPlayer() {
        return mCommonMediaPlayer;
    }

    public void setCommonMediaPlayer(CommonMediaPlayer commonMediaPlayer)
    {
        this.mCommonMediaPlayer = commonMediaPlayer;
    }

    @Override
    public void playVideo(String path)
    {

        if (mCommonMediaPlayer != null)
        {
            mCommonMediaPlayer.playVideo(path);
        }
    }

    @Override
    public void playVideo(MediaDataSource mediaDataSource)
    {

        if (mCommonMediaPlayer != null)
        {
            mCommonMediaPlayer.playVideo(mediaDataSource);
        }
    }

    @Override
    public void playVideo(Uri uri)
    {

        if (mCommonMediaPlayer != null)
        {
            mCommonMediaPlayer.playVideo(uri);
        }
    }

    @Override
    public void playVideo(int progress)
    {

        if (mCommonMediaPlayer != null)
        {
            mCommonMediaPlayer.playVideo(progress);
        }
    }

    @Override
    public void resumeVideo()
    {

        if (mCommonMediaPlayer != null)
        {
            mCommonMediaPlayer.resumeVideo();
        }
    }

    @Override
    public void pauseVideo()
    {

        if (mCommonMediaPlayer != null)
        {
            mCommonMediaPlayer.pauseVideo();
        }
    }

    @Override
    public void releaseVideo()
    {

        if (mCommonMediaPlayer != null)
        {
            mCommonMediaPlayer.releaseVideo();
        }
    }

    @Override
    public long getTimeDuration()
    {
        return (mCommonMediaPlayer == null ? 0 :
                mCommonMediaPlayer.getTimeDuration());
    }

    @Override
    public long getCurrentTime()
    {
        return (mCommonMediaPlayer == null? 0 :
                mCommonMediaPlayer.getCurrentTime());
    }

    @Override
    public void onVideoLoading()
    {
        if (mPlayerController != null)
        {
            mPlayerController.setVideoStatus(VideoStatus.LOADING);
        }
    }

    @Override
    public void onVideoPaused()
    {
        if (mPlayerController != null)
        {
            mPlayerController.setVideoStatus(VideoStatus.PAUSE);
        }
    }

    @Override
    public void onVideoStarted()
    {
        if (mPlayerController != null)
        {
            mPlayerController.setVideoStatus(VideoStatus.PLAYING);
        }
    }

    @Override
    public void onVideoTouch() {

        if (mPlayerController != null)
        {
            if (mPlayerController.isVisible())
            {
                mPlayerController.hideController();
            }
            else
            {
                mPlayerController.exposeController();
            }
        }
    }

    @Override
    public void onVideoTracking(int progress)
    {

    }

    @SuppressWarnings("unchecked")
    private <V extends View> V findView(int resourceId) {
        return (V) findViewById(resourceId);
    }

}
