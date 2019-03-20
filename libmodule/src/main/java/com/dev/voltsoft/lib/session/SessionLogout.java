package com.dev.voltsoft.lib.session;

import android.support.v7.app.AppCompatActivity;
import com.dev.voltsoft.lib.model.BaseRequest;

public abstract class SessionLogout extends BaseRequest<SessionResponse> {

    public abstract SessionType getTargetSessionType();

    public abstract ISessionLogoutListener getSessionLogoutListener();

    public abstract AppCompatActivity getAppCompatActivity();
}
