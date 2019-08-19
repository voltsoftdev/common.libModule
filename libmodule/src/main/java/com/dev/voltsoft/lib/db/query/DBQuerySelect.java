package com.dev.voltsoft.lib.db.query;

import android.database.Cursor;
import com.dev.voltsoft.lib.model.BaseModel;

public class DBQuerySelect<M extends BaseModel> extends DBQuery
{
    private String mDBQuery;

    private Class<M> mClass;

    @SuppressWarnings("unchecked")
    public DBQuerySelect()
    {
        super(DBQueryType.QUERY_SELECT);
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
    public Class<M> getTargetClass()
    {
        return mClass;
    }

    public void setTargetClass(Class<M> c)
    {
        mClass = c;
    }

    public M parse(Cursor cursor)
    {
        try
        {
            M m = mClass.newInstance();

            m.matchingCursor(cursor);

            return m;
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return null;
        }
    }
}
