package com.dev.voltsoft.lib.session;

import androidx.appcompat.app.AppCompatActivity;
import com.dev.voltsoft.lib.model.BaseRequest;

public abstract class SessionLogin<R> extends BaseRequest<SessionResponse> {

    private AppCompatActivity mAppCompatActivity;

    private ISessionLoginListener<R> mSessionListener;

    public ISessionLoginListener<R> getSessionLoginListener()
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

    public void setSessionListner(ISessionLoginListener<R> listener)
    {
        this.mSessionListener = listener;
    }

    public abstract SessionType getTargetSessionType();
}
