package com.dev.voltsoft.lib.network.parse;

import com.dev.voltsoft.lib.model.BaseModel;
import com.dev.voltsoft.lib.network.base.NetworkParcelable;
import org.json.JSONObject;

public interface JSONParcelable<M extends BaseModel> extends NetworkParcelable<JSONObject, M> {

    @Override
    M parse(JSONObject jsonObject);
}
