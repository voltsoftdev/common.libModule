package com.dev.voltsoft.lib.network.base;

import com.dev.voltsoft.lib.model.BaseModel;

public interface NetworkParcelable<R, M> {

    M parse(R r);
}
