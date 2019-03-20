package com.dev.voltsoft.lib.component;

import android.app.Application;
import com.dev.voltsoft.lib.network.NetworkRequestHandler;
import com.dev.voltsoft.lib.network.NetworkState;
import com.dev.voltsoft.lib.network.base.URLGeneratorStrategy;
import com.dev.voltsoft.lib.session.SessionRequestHandler;
import com.dev.voltsoft.lib.utility.CommonPreference;

public abstract class CommonApplication extends Application implements URLGeneratorStrategy {

    @Override
    public void onCreate()
    {
        super.onCreate();

        CommonPreference.init(this);

        NetworkState.getInstance().registerReceiver(this);

        NetworkRequestHandler.getInstance().setURLGeneratorStrategy(this);

        SessionRequestHandler.getInstance().init(this);
    }
}
