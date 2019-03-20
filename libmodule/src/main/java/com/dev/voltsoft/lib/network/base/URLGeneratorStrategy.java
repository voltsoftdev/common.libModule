package com.dev.voltsoft.lib.network.base;

import com.dev.voltsoft.lib.model.BaseRequest;

public interface URLGeneratorStrategy
{
    String create(BaseRequest request);
}
