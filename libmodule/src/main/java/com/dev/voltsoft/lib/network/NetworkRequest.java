package com.dev.voltsoft.lib.network;

import android.content.ContentValues;
import com.dev.voltsoft.lib.model.BaseRequest;
import com.dev.voltsoft.lib.network.base.NetworkConstant;
import com.dev.voltsoft.lib.network.base.NetworkParcelable;
import com.dev.voltsoft.lib.network.base.NetworkResponse;

public abstract class NetworkRequest extends BaseRequest<NetworkResponse> implements Runnable, NetworkConstant
{
    public abstract String getHttpMethod();

    public abstract ContentValues getHttpRequestHeader();

    public abstract ContentValues getHttpRequestParameter();

    public abstract NetworkParcelable getNetworkParcelable();

    @SuppressWarnings("unchecked")
    @Override
    public void run()
    {
        try
        {
            NetworkRequestHandler.getInstance().request(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
