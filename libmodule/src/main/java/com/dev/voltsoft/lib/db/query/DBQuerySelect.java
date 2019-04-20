package com.dev.voltsoft.lib.db.query;

import android.content.ContentValues;
import com.dev.voltsoft.lib.db.DBQueryGenerator;
import com.dev.voltsoft.lib.db.DBQueryParcelable;
import com.dev.voltsoft.lib.model.BaseModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class DBQuerySelect<M extends BaseModel> extends DBQuery
{
    private String mDBQuery;

    private Class<M> mClass;

    private ContentValues mWhereClause;

    private DBQueryParcelable   mDBQueryParcelable;

    @SuppressWarnings("unchecked")
    public DBQuerySelect(Class<M> c)
    {
        super(DBQueryType.QUERY_SELECT);

        mClass = c;

    }

    public DBQueryParcelable getDBQueryParcelable() {
        return mDBQueryParcelable;
    }

    public void setDBQueryParcelable(DBQueryParcelable queryParcelable) {
        this.mDBQueryParcelable = queryParcelable;
    }

    public String getDBQuery()
    {
        return mDBQuery;
    }

    public void setDBQuery(String s)
    {
        this.mDBQuery = s;
    }

    public Class<M> getTargetClass()
    {
        return mClass;
    }

    public ContentValues getWhereClause()
    {
        return mWhereClause;
    }
}
