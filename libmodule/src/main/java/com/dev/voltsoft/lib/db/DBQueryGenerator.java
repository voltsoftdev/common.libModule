package com.dev.voltsoft.lib.db;

import com.dev.voltsoft.lib.model.BaseRequest;

public interface DBQueryGenerator
{
    String create(BaseRequest request);
}
