package com.dev.voltsoft.lib.db.query;

import com.dev.voltsoft.lib.model.BaseModel;

public class DBQueryUpdate<M extends BaseModel> extends DBQuery
{
    private M   mTargetInstance;

    public DBQueryUpdate()
    {
        super(DBQueryType.QUERY_UPDATE);
    }

    public M getTargetInstance() {
        return mTargetInstance;
    }

    public void setTargetInstance(M targetInstance) {
        this.mTargetInstance = targetInstance;
    }
}
