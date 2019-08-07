package com.dev.voltsoft.lib.session;

import com.dev.voltsoft.lib.session.facebook.FacebookSessionSDK;
import com.dev.voltsoft.lib.session.google.GoogleSessionSDK;
import com.dev.voltsoft.lib.session.kakao.KaKaoSessionSDK;

public enum SessionType {

    KAKAO(KaKaoSessionSDK.getInstance()),
    FACEBOOK(FacebookSessionSDK.getInstance()),
    GOOGLE(GoogleSessionSDK.getInstance());

    private ISessionSDK mSessionLoginSDK;

    SessionType(ISessionSDK s)
    {
        mSessionLoginSDK = s;
    }

    public ISessionSDK getSessionLoginSDK()
    {
        return mSessionLoginSDK;
    }
}
