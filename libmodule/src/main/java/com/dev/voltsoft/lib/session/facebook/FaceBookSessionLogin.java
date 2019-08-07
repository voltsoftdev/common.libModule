package com.dev.voltsoft.lib.session.facebook;

import com.dev.voltsoft.lib.session.SessionLogin;
import com.dev.voltsoft.lib.session.SessionType;
import com.facebook.GraphResponse;

public class FaceBookSessionLogin extends SessionLogin<GraphResponse>
{

    @Override
    public SessionType getTargetSessionType()
    {
        return SessionType.FACEBOOK;
    }
}
