package com.dev.voltsoft.lib.session;

import androidx.appcompat.app.AppCompatActivity;
import com.dev.voltsoft.lib.model.BaseRequest;

public abstract class SessionLogout extends BaseRequest {

    private AppCompatActivity mAppCompatActivity;

    private ISessionLogoutListener mSessionListener;

    public ISessionLogoutListener getSessionLogoutListener()
    {
        return mSessionListener;
    }

    public AppCompatActivity getAppCompatActivity()
    {
        return mAppCompatActivity;
    }

    public void setActivity(AppCompatActivity activity)
    {
        this.mAppCompatActivity = activity;
    }

    public void setSessionListner(ISessionLogoutListener listener)
    {
        this.mSessionListener = listener;
    }

    public abstract SessionType getTargetSessionType();
}
