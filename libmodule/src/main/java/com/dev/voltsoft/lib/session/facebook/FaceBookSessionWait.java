package com.dev.voltsoft.lib.session.facebook;

import android.support.v7.app.AppCompatActivity;
import com.dev.voltsoft.lib.session.ISessionLoginListener;
import com.dev.voltsoft.lib.session.SessionType;
import com.dev.voltsoft.lib.session.SessionWait;
import com.facebook.GraphResponse;

public class FaceBookSessionWait extends SessionWait<GraphResponse>
{

    @Override
    public SessionType getTargetSessionType()
    {
        return SessionType.FACEBOOK;
    }
}
