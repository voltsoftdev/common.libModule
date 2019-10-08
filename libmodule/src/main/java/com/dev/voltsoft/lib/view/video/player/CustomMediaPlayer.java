package com.dev.voltsoft.lib.view.video.player;

import android.content.Context;
import android.media.MediaDataSource;
import android.net.Uri;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

public class CustomMediaPlayer extends CommonMediaPlayer {

    public CustomMediaPlayer(Context context) {
        super(context);
    }

    public CustomMediaPlayer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomMediaPlayer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void playVideo(String path) {

    }

    @Override
    public void playVideo(MediaDataSource mediaDataSource) {

    }

    @Override
    public void playVideo(Uri uri) {

    }

    @Override
    public void playVideo(int progress) {

    }

    @Override
    public void resumeVideo() {

    }

    @Override
    public void pauseVideo() {

    }

    @Override
    public void releaseVideo() {

    }

    @Override
    public long getTimeDuration() {
        return 0;
    }

    @Override
    public long getCurrentTime() {
        return 0;
    }
}
