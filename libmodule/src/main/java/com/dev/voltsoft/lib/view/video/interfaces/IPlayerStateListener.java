package com.dev.voltsoft.lib.view.video.interfaces;

public interface IPlayerStateListener {

    void onVideoLoading();

    void onVideoPaused();

    void onVideoStarted();

    void onVideoTouch();

    void onVideoTracking(int progress);
}
