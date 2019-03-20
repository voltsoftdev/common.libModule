package com.dev.voltsoft.lib;

import com.dev.voltsoft.lib.model.BaseRequest;

import java.util.Observer;

public interface IRequestHandler<R extends BaseRequest> extends Observer
{
    void request(R r);
}
