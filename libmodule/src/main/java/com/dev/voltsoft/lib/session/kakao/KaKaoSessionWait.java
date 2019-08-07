package com.dev.voltsoft.lib.session.kakao;

import com.dev.voltsoft.lib.session.SessionType;
import com.dev.voltsoft.lib.session.SessionWait;
import com.kakao.usermgmt.response.model.UserProfile;

public class KaKaoSessionWait extends SessionWait<UserProfile>
{
    @Override
    public SessionType getTargetSessionType()
    {
        return SessionType.KAKAO;
    }
}
