package com.dev.voltsoft.lib.network;

import android.content.ContentValues;
import com.dev.voltsoft.lib.model.BaseRequest;
import com.dev.voltsoft.lib.network.base.INetworkProgressView;
import com.dev.voltsoft.lib.network.base.NetworkConstant;
import com.dev.voltsoft.lib.network.base.NetworkParcelable;
import com.dev.voltsoft.lib.network.base.NetworkResponse;

public abstract class NetworkRequest extends BaseRequest<NetworkResponse> implements Runnable, NetworkConstant
{
    public abstract String getHttpMethod();

    public abstract ContentValues getHttpRequestHeader();

    public abstract ContentValues getHttpRequestParameter();

    public abstract NetworkParcelable getNetworkParcelable();

    public String mTargetUrl;

    public INetworkProgressView mNetworkProgressView;

    @SuppressWarnings("unchecked")
    @Override
    public void run()
    {
        try
        {
            NetworkRequestHandler.getInstance().handle(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getTargetUrl()
    {
        return mTargetUrl;
    }

    public void setTargetUrl(String s)
    {
        mTargetUrl = s;
    }

    public INetworkProgressView getNetworkProgressView()
    {
        return mNetworkProgressView;
    }

    public void setNetworkProgressView(INetworkProgressView view)
    {
        this.mNetworkProgressView = view;
    }
}
