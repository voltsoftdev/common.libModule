package com.dev.voltsoft.lib.component;

import android.app.Application;

import com.dev.voltsoft.lib.db.DBQueryHandler;
import com.dev.voltsoft.lib.network.NetworkState;
import com.dev.voltsoft.lib.session.SessionRequestHandler;
import com.dev.voltsoft.lib.utility.CommonPreference;
import com.dev.voltsoft.lib.utility.RuntimePermissionHelper;

public abstract class CommonApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        CommonPreference.init(this);

        NetworkState.getInstance().registerReceiver(this);

        SessionRequestHandler.getInstance().init(this);

        DBQueryHandler.getInstance().init(this);

        RuntimePermissionHelper.PERMISSIONS_NECESSARY = getRuntimePermissions();
    }

    public abstract String[] getRuntimePermissions();

    public abstract int getApplicationDBVersion();
}
