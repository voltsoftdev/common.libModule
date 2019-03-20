package com.dev.voltsoft.lib.view.video;

import android.util.Pair;


public class ReadyForPlaybackIndicator {

    private static final String TAG = ReadyForPlaybackIndicator.class.getSimpleName();

    private Pair<Integer, Integer> mVideoSize;
    private boolean mSurfaceTextureAvailable;
    private boolean mFailedToPrepareUiForPlayback = false;

    public boolean isVideoSizeAvailable() {
        boolean isVideoSizeAvailable = mVideoSize.first != null && mVideoSize.second != null;

        return isVideoSizeAvailable;
    }

    public boolean isSurfaceTextureAvailable() {
        return mSurfaceTextureAvailable;
    }

    public boolean isReadyForPlayback() {
        boolean isReadyForPlayback = isVideoSizeAvailable() && isSurfaceTextureAvailable();
        return isReadyForPlayback;
    }

    public void setSurfaceTextureAvailable(boolean available) {
        mSurfaceTextureAvailable = available;
    }

    public void setVideoSize(Integer videoHeight, Integer videoWidth) {
        mVideoSize = new Pair<>(videoHeight, videoWidth);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + isReadyForPlayback();
    }

    public void setFailedToPrepareUiForPlayback(boolean failed) {
        mFailedToPrepareUiForPlayback = failed;
    }

    public boolean isFailedToPrepareUiForPlayback() {
        return mFailedToPrepareUiForPlayback;
    }
}
