package com.dev.voltsoft.lib.model;

public abstract class BaseResponse
{

    private int mResponseCode;

    private boolean ResponseSuccess;

    private BaseRequest mSourceRequest;

    private Object mResponseModel;

    public Object getResponseModel()
    {
        return mResponseModel;
    }

    public void setResponseModel(Object m)
    {
        mResponseModel = m;
    }

    public BaseRequest getSourceRequest() {
        return mSourceRequest;
    }

    public void setSourceRequest(BaseRequest sourceRequest) {
        mSourceRequest = sourceRequest;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(int responseCode) {
        mResponseCode = responseCode;
    }

    public boolean isResponseSuccess()
    {
        return ResponseSuccess;
    }

    public void setResponseSuccess(boolean s)
    {
        ResponseSuccess = s;
    }
}
