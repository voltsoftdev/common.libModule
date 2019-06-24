package com.dev.voltsoft.lib.firebase.db;

import com.dev.voltsoft.lib.model.BaseModel;
import com.dev.voltsoft.lib.model.BaseResponse;

import java.util.ArrayList;

public class FireBaseDBResponse<M> extends BaseResponse
{
    private boolean ResponseSuccess;

    public FireBaseDBResponse()
    {
        setResponseModel(new ResultDataList<M>());
    }


    public boolean isResponseSuccess()
    {
        return ResponseSuccess;
    }

    public void setResponseSuccess(boolean s)
    {
        ResponseSuccess = s;
    }

    @SuppressWarnings("unchecked")
    public <M extends BaseModel> M getFirstResult()
    {
        ResultDataList resultDataList = (ResultDataList) getResponseModel();

        return (M) resultDataList.get(0);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<M> resultList()
    {
        Object o = getResponseModel();

        return (o instanceof ResultDataList ? ((ResultDataList<M>) o).modelList : null);
    }

    public FireBaseDBResponse addResult(M m)
    {
        if (resultList() != null)
        {
            resultList().add(m);
        }

        return this;
    }

}
