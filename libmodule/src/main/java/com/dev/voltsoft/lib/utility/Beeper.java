package com.dev.voltsoft.lib.utility;

import android.content.Context;
import android.media.MediaPlayer;

public class Beeper
{
	
	MediaPlayer mediaPlayer;
	
	public Beeper(Context context, int id)
	{
		mediaPlayer = MediaPlayer.create(context, id);
	}

	public void play()
	{
		mediaPlayer.seekTo(0);
		mediaPlayer.start();
	}
}
