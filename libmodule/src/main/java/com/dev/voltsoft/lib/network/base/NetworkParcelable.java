package com.dev.voltsoft.lib.network.base;

import com.dev.voltsoft.lib.model.BaseModel;

public interface NetworkParcelable<R, M extends BaseModel> {

    M parse(R r);
}
