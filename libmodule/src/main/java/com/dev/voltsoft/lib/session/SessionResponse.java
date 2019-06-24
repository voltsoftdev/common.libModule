package com.dev.voltsoft.lib.session;


import com.dev.voltsoft.lib.model.BaseModel;
import com.dev.voltsoft.lib.model.BaseResponse;

public class SessionResponse<M extends BaseModel> extends BaseResponse
{
    private SessionType mSessionType;

    public SessionType getSessionType() {
        return mSessionType;
    }

    public void setSessionType(SessionType sessionType) {
        mSessionType = sessionType;
    }
}
