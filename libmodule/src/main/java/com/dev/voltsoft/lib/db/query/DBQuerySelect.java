package com.dev.voltsoft.lib.db.query;

import com.dev.voltsoft.lib.db.DBQueryParcelable;
import com.dev.voltsoft.lib.model.BaseModel;

public abstract class DBQuerySelect extends DBQuery implements DBQueryParcelable
{
    private String mDBQuery;

    private Class<?> mClass;

    @SuppressWarnings("unchecked")
    public <M extends BaseModel> DBQuerySelect(Class<M> c)
    {
        super(DBQueryType.QUERY_SELECT);

        mClass = c;
    }

    public String getDBQuery()
    {
        return mDBQuery;
    }

    public void setDBQuery(String s)
    {
        this.mDBQuery = s;
    }

    @SuppressWarnings("unchecked")
    public <M extends BaseModel> Class<M> getTargetClass()
    {
        return (Class<M>) mClass;
    }
}
