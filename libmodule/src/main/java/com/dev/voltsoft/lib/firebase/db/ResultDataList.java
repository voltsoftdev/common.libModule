package com.dev.voltsoft.lib.firebase.db;

import com.dev.voltsoft.lib.model.BaseModel;

import java.util.ArrayList;

public class ResultDataList<M> extends BaseModel
{
   public ArrayList<M> modelList = new ArrayList<>();

    protected ArrayList<M> getModelList()
    {
        return modelList;
    }

    public void setModelList(ArrayList<M> list)
    {
        this.modelList = list;
    }

    public void addModel(M baseModel)
    {
        if (modelList == null)
        {
            modelList = new ArrayList<>();
        }
        modelList.add(baseModel);
    }

    public M get(int i)
    {
        return (modelList != null &&
                modelList.size() > i ? modelList.get(i) : null);
    }
}
