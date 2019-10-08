package com.dev.voltsoft.lib.db.query;

import androidx.annotation.NonNull;
import com.dev.voltsoft.lib.model.BaseModel;

import java.util.ArrayList;
import java.util.Arrays;

public class DBQueryInsert<M extends BaseModel> extends DBQuery
{
    public ArrayList<M> mTargetInstanceList;

    public DBQueryInsert()
    {
        super(DBQueryType.QUERY_INSERT);

        mTargetInstanceList = new ArrayList<>();
    }

    public void addInstance(M ... m)
    {
        if (mTargetInstanceList != null)
        {
            mTargetInstanceList.addAll(Arrays.asList(m));
        }
    }
}
