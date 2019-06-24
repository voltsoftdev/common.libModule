package com.dev.voltsoft.lib.network.base;

import com.dev.voltsoft.lib.model.BaseModel;
import com.dev.voltsoft.lib.model.BaseResponse;
import com.dev.voltsoft.lib.network.exception.NetworkException;

public class NetworkResponse<M extends BaseModel> extends BaseResponse {

    private NetworkException mNetworkException;

    public NetworkException getNetworkException() {
        return mNetworkException;
    }

    public void setNetworkException(NetworkException e) {
        this.mNetworkException = e;
    }

}
