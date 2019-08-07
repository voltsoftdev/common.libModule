package com.dev.voltsoft.lib.component;

import android.app.Application;
import com.dev.voltsoft.lib.network.NetworkState;
import com.dev.voltsoft.lib.session.SessionRequestHandler;
import com.dev.voltsoft.lib.utility.CommonPreference;

public abstract class CommonApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        CommonPreference.init(this);

        NetworkState.getInstance().registerReceiver(this);

        SessionRequestHandler.getInstance().init(this);
    }
}
