package com.dev.voltsoft.lib.db.query;

import android.content.ContentValues;
import com.dev.voltsoft.lib.model.BaseModel;

public class DBQuerySelect<M extends BaseModel> extends DBQuery
{
    public String            DBQuery;

    public Class<M>          TargetClass;

    public ContentValues     WhereClause = new ContentValues();

    @SuppressWarnings("unchecked")
    public DBQuerySelect()
    {
        super(DBQueryType.QUERY_SELECT);
    }
}
