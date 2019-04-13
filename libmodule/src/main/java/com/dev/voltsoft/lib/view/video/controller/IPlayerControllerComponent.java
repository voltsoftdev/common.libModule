package com.dev.voltsoft.lib.view.video.controller;

import com.dev.voltsoft.lib.view.video.constants.VideoStatus;
import com.dev.voltsoft.lib.view.video.player.CommonMediaPlayer;

public interface IPlayerControllerComponent {

    void setVideoStatus(VideoStatus videoStatus);

    VideoStatus getVideoStatus();

    void setCommonMediaPlayer(CommonMediaPlayer commonMediaPlayer);

    CommonMediaPlayer getCommonMediaPlayer();

    void hide();

    void show();
}
