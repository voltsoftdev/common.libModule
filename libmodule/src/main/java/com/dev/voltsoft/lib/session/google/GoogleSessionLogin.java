package com.dev.voltsoft.lib.session.google;

import com.dev.voltsoft.lib.session.SessionLogin;
import com.dev.voltsoft.lib.session.SessionType;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class GoogleSessionLogin extends SessionLogin<GoogleSignInAccount>
{
    @Override
    public SessionType getTargetSessionType()
    {
        return SessionType.GOOGLE;
    }
}
