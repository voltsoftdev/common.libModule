package com.dev.voltsoft.lib.db.query;

import com.dev.voltsoft.lib.db.DBQueryGenerator;
import com.dev.voltsoft.lib.db.DBQueryParcelable;

public class DBQuerySelect extends DBQuery
{
    private DBQueryGenerator    mDBQueryGenerator;
    private DBQueryParcelable mDBQueryParcelable;

    public DBQuerySelect()
    {
        super(DBQueryType.QUERY_SELECT);
    }

    public DBQueryParcelable getDBQueryParcelable() {
        return mDBQueryParcelable;
    }

    public void setDBQueryParcelable(DBQueryParcelable queryParcelable) {
        this.mDBQueryParcelable = queryParcelable;
    }

    public DBQueryGenerator getDbQueryGenerator()
    {
        return mDBQueryGenerator;
    }

    public void setDbQueryGenerator(DBQueryGenerator queryGenerator)
    {
        this.mDBQueryGenerator = queryGenerator;
    }
}
