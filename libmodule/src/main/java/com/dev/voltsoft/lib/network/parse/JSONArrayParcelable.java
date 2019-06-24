package com.dev.voltsoft.lib.network.parse;

import com.dev.voltsoft.lib.model.BaseModel;
import com.dev.voltsoft.lib.network.base.NetworkParcelable;
import org.json.JSONObject;

import java.util.ArrayList;

public interface JSONArrayParcelable<M extends BaseModel> extends NetworkParcelable<JSONObject, ArrayList<M>> {

    @Override
    ArrayList<M> parse(JSONObject jsonObject);
}
