package com.dev.voltsoft.lib.session.google;

import com.dev.voltsoft.lib.session.SessionLogout;
import com.dev.voltsoft.lib.session.SessionType;

public class GoogleSessionLogout extends SessionLogout
{
    @Override
    public SessionType getTargetSessionType()
    {
        return SessionType.GOOGLE;
    }
}
