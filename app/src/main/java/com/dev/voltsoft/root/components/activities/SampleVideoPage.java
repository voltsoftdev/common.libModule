package com.dev.voltsoft.root.components.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.LinearLayout;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.lib.view.video.VideoPlayerDecorator;
import com.dev.voltsoft.root.R;

public class SampleVideoPage extends CommonActivity
{
    private static final String SAMPLE_VIDEO = "https://www.radiantmediaplayer.com/media/bbb-360p.mp4";

    private static final String CURRENT_POSITION = "CURRENT_POSITION";

    private VideoPlayerDecorator    mVideoPlayer;
    private LinearLayout            mVideoBottomInfoLayout;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.page_video);

        mVideoPlayer = findViewById(R.id.player);
        mVideoPlayer.playVideo(SAMPLE_VIDEO);

        mVideoBottomInfoLayout = findViewById(R.id.playerBottomLayout);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
    {
        super.onSaveInstanceState(outState, outPersistentState);

        mVideoPlayer.pauseVideo();
    }


    @Override
    protected void onPause()
    {
        super.onPause();

        mVideoPlayer.pauseVideo();
    }

    @Override
    protected void onDestroy()
    {
        mVideoPlayer.releaseVideo();

        super.onDestroy();
    }
}
