package com.dev.voltsoft.lib.db.query;

import com.dev.voltsoft.lib.model.BaseModel;

public class DBQueryDelete<M extends BaseModel> extends DBQuery
{
    private M   mTargetInstance;

    public DBQueryDelete()
    {
        super(DBQueryType.QUERY_DELETE);
    }

    public M getTargetInstance() {
        return mTargetInstance;
    }

    public void setTargetInstance(M targetInstance) {
        this.mTargetInstance = targetInstance;
    }
}
