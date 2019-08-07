package com.dev.voltsoft.lib.session.kakao;

import com.dev.voltsoft.lib.session.SessionLogin;
import com.dev.voltsoft.lib.session.SessionType;
import com.kakao.usermgmt.response.model.UserProfile;

public class KaKaoSessionLogin extends SessionLogin<UserProfile>
{
    @Override
    public SessionType getTargetSessionType()
    {
        return SessionType.KAKAO;
    }
}
