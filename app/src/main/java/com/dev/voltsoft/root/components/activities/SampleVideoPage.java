package com.dev.voltsoft.root.components.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.lib.view.video.VideoPlayerDecorator;
import com.dev.voltsoft.root.R;

public class SampleVideoPage extends CommonActivity
{
    private static final String SAMPLE_VIDEO = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";

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
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        switch (newConfig.orientation)
        {
            case Configuration.ORIENTATION_LANDSCAPE:
            {
                if (mVideoPlayer != null)
                {
                    LinearLayout.LayoutParams layoutParams0 = (LinearLayout.LayoutParams) mVideoPlayer.getLayoutParams();
                    layoutParams0.weight = 100;
                    mVideoPlayer.setLayoutParams(layoutParams0);
                    mVideoPlayer.requestLayout();
                }

                if (mVideoBottomInfoLayout != null)
                {
                    LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) mVideoBottomInfoLayout.getLayoutParams();
                    layoutParams1.weight = 0;
                    mVideoBottomInfoLayout.setLayoutParams(layoutParams1);
                    mVideoBottomInfoLayout.requestLayout();
                }
                break;
            }

            case Configuration.ORIENTATION_PORTRAIT:
            {
                if (mVideoPlayer != null)
                {
                    LinearLayout.LayoutParams layoutParams0 = (LinearLayout.LayoutParams) mVideoPlayer.getLayoutParams();
                    layoutParams0.weight = 40;
                    mVideoPlayer.setLayoutParams(layoutParams0);
                    mVideoPlayer.requestLayout();
                }

                if (mVideoBottomInfoLayout != null) {
                    LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) mVideoBottomInfoLayout.getLayoutParams();
                    layoutParams1.weight = 60;
                    mVideoBottomInfoLayout.setLayoutParams(layoutParams1);
                    mVideoBottomInfoLayout.requestLayout();
                }
                break;
            }
        }
    }
}
