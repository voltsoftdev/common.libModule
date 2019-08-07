package com.dev.voltsoft.lib.session.google;

import android.support.v7.app.AppCompatActivity;
import com.dev.voltsoft.lib.session.ISessionLoginListener;
import com.dev.voltsoft.lib.session.SessionType;
import com.dev.voltsoft.lib.session.SessionWait;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class GoogleSessionWait extends SessionWait<GoogleSignInAccount>
{
    @Override
    public SessionType getTargetSessionType()
    {
        return SessionType.GOOGLE;
    }
}
