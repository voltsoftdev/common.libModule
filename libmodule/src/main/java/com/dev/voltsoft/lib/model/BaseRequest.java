package com.dev.voltsoft.lib.model;

import com.dev.voltsoft.lib.IResponseListener;

public abstract class BaseRequest<R extends BaseResponse>
{

    private IResponseListener mResponseListener;

    public IResponseListener getResponseListener()
    {
        return mResponseListener;
    }

    public void setResponseListener(IResponseListener responseListener)
    {
        this.mResponseListener = responseListener;
    }

    public <B extends BaseRequest> boolean isRight(Class<B> rClass)
    {
        return getClass().equals(rClass);
    }
}
