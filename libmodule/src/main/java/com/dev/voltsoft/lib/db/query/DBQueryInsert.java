package com.dev.voltsoft.lib.db.query;

import com.dev.voltsoft.lib.model.BaseModel;

import java.util.ArrayList;

public class DBQueryInsert<M extends BaseModel> extends DBQuery
{
    private ArrayList<M> mTargetInstanceList;

    public DBQueryInsert()
    {
        super(DBQueryType.QUERY_INSERT);

        mTargetInstanceList = new ArrayList<>();
    }

    public ArrayList<M> getTargetInstanceList() {
        return mTargetInstanceList;
    }

    public void addTargetInstance(M m)
    {
        if (mTargetInstanceList != null)
        {
            mTargetInstanceList.add(m);
        }
    }
}
