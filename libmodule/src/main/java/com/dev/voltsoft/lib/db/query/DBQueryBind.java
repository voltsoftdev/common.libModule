package com.dev.voltsoft.lib.db.query;

import com.dev.voltsoft.lib.model.BaseModel;

public class DBQueryBind<M extends BaseModel> extends DBQuerySelect<M>
{
    public DBQueryBind()
    {
        mDBQueryType = DBQueryType.BIND;
    }
}
