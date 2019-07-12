package com.dev.voltsoft.lib.network.parse;

import com.dev.voltsoft.lib.model.BaseModel;
import com.dev.voltsoft.lib.network.base.NetworkParcelable;

import java.util.ArrayList;

public interface XMLArrayParcelable<M extends BaseModel> extends NetworkParcelable<String, ArrayList<M>> {

    @Override
    ArrayList<M> parse(String s);
}
