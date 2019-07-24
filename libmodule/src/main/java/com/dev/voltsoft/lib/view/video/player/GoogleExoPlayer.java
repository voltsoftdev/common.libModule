package com.dev.voltsoft.lib.view.video.player;

import android.content.Context;
import android.media.MediaDataSource;
import android.net.Uri;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.widget.FrameLayout;
import com.dev.voltsoft.lib.view.video.interfaces.IPlayerStateListener;
import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.*;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

public class GoogleExoPlayer extends CommonMediaPlayer
        implements Player.EventListener, SimpleExoPlayer.VideoListener, VideoRendererEventListener {

    private static final String TAG = "CustomExoPlayer";

    private SimpleExoPlayerView     mSimpleExoPlayerView;
    private SimpleExoPlayer         mSimpleExoPlayer;

    private TrackSelection.Factory          mTrackSelectionFactory;
    private TrackSelector                   mTrackSelector;

    public GoogleExoPlayer(Context context) {
        super(context);

        init(context, null);
    }

    public GoogleExoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public GoogleExoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {

        mSimpleExoPlayerView = new SimpleExoPlayerView(context);
        FrameLayout.LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mSimpleExoPlayerView.setLayoutParams(layoutParams);
        addView(mSimpleExoPlayerView);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        mTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);

        mTrackSelector = new DefaultTrackSelector(mTrackSelectionFactory);

        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, mTrackSelector);
        mSimpleExoPlayer.addListener(this);
        mSimpleExoPlayer.addVideoListener(this);
        mSimpleExoPlayer.setVideoDebugListener(this);

        mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);
        mSimpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        mSimpleExoPlayerView.setUseController(false);
    }

    @Override
    public void playVideo(String path)
    {

        if (!TextUtils.isEmpty(path))
        {
            playVideo(Uri.parse(path));
        }
    }

    @Override
    public void playVideo(MediaDataSource mediaDataSource)
    {

    }

    @Override
    public void playVideo(Uri uri)
    {

        Context c = getContext();

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
                c, Util.getUserAgent(c, TAG), bandwidthMeter);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(
                uri, dataSourceFactory, extractorsFactory, null, null);

        if (mSimpleExoPlayer != null)
        {
            mSimpleExoPlayer.prepare(mediaSource);
            mSimpleExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void playVideo(int progress) {

        if (mSimpleExoPlayer != null)
        {
            int position = (int) ((mSimpleExoPlayer.getDuration() * progress) / 100);

            mSimpleExoPlayer.seekTo(position);
        }
    }

    @Override
    public void resumeVideo()
    {
        if (mSimpleExoPlayer != null)
        {
            mSimpleExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void pauseVideo()
    {
        if (mSimpleExoPlayer != null)
        {
            mSimpleExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void releaseVideo()
    {
        if (mSimpleExoPlayer != null)
        {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
        }
    }

    @Override
    public long getTimeDuration()
    {
        return (mSimpleExoPlayer == null ? 0 :
                mSimpleExoPlayer.getDuration());
    }

    @Override
    public long getCurrentTime() {
        return (mSimpleExoPlayer == null ? 0 :
                mSimpleExoPlayer.getCurrentPosition());
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest)
    {

    }

    @Override
    public void onTracksChanged
            (TrackGroupArray trackGroups, TrackSelectionArray trackSelections)
    {

    }

    @Override
    public void onLoadingChanged(boolean isLoading)
    {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState)
    {

        IPlayerStateListener playerStateListener = getPlayerStateListener();

        switch (playbackState) {

            case PlaybackStateCompat.STATE_BUFFERING:
                if (playerStateListener != null)
                {
                    playerStateListener.onVideoLoading();
                }
                break;

            case PlaybackStateCompat.STATE_PLAYING:
                if (playerStateListener != null)
                {
                    playerStateListener.onVideoStarted();
                }
                break;

            case PlaybackStateCompat.STATE_PAUSED:
                if (playerStateListener != null)
                {
                    playerStateListener.onVideoLoading();
                }
                break;

            default:
                if (playerStateListener != null)
                {
                    playerStateListener.onVideoLoading();
                }
                break;
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters)
    {

    }

    @Override
    public void onVideoEnabled(DecoderCounters counters)
    {

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs)
    {

    }

    @Override
    public void onVideoInputFormatChanged(Format format)
    {

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs)
    {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio)
    {

    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {

    }

    @Override
    public void onRenderedFirstFrame()
    {
        IPlayerStateListener playerStateListener = getPlayerStateListener();

        if (playerStateListener != null)
        {
            playerStateListener.onVideoLoading();
        }
    }
}
