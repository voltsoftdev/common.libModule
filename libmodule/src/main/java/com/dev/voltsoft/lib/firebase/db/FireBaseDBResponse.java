package com.dev.voltsoft.lib.firebase.db;

import com.dev.voltsoft.lib.model.BaseModel;
import com.dev.voltsoft.lib.model.BaseResponse;

import java.util.ArrayList;

public class FireBaseDBResponse<M> extends BaseResponse<ResultDataList<M>>
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
    public M getFirstResult()
    {
        ResultDataList resultDataList = getResponseModel();

        return (M) resultDataList.get(0);
    }

    public ArrayList<M> resultList()
    {
        return getResponseModel().modelList;
    }

    public FireBaseDBResponse addResult(M m)
    {
        if (getResponseModel().modelList != null)
        {
            getResponseModel().modelList.add(m);
        }

        return this;
    }

}
