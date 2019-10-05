package com.dev.voltsoft.lib.firebase.db;

import com.dev.voltsoft.lib.model.BaseModel;
import com.dev.voltsoft.lib.model.BaseResponse;

import java.util.ArrayList;
import java.util.HashMap;

public class FireBaseDBResponse extends BaseResponse
{
    public HashMap<String, Object> modelList = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <M extends BaseModel> M getFirstResult()
    {
        return (M) (new ArrayList<>(modelList.values())).get(0);
    }

    @SuppressWarnings("unchecked")
    public <M extends BaseModel> M getLastResult()
    {
        int size = modelList.values().size();
        if (size > 0)
        {
            return (M) (new ArrayList<>(modelList.values())).get(((size - 1)));
        }
        else
        {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <M extends BaseModel> ArrayList<M> resultList()
    {
        Object o = getResponseModel();

        return new ArrayList(modelList.values());
    }

    public <M extends BaseModel> FireBaseDBResponse addResult(String key, M m)
    {
        if (modelList != null)
        {
            modelList.put(key, m);
        }

        return this;
    }

}
