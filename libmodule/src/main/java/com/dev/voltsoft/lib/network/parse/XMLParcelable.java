package com.dev.voltsoft.lib.network.parse;

import com.dev.voltsoft.lib.model.BaseModel;
import com.dev.voltsoft.lib.network.base.NetworkParcelable;

public interface XMLParcelable<M extends BaseModel> extends NetworkParcelable<String, M> {

    @Override
    M parse(String s);
}
