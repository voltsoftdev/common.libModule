package com.dev.voltsoft.lib;

import com.dev.voltsoft.lib.model.BaseResponse;

public interface IResponseListener {

    void onThreadResponseListen(BaseResponse response);
}
