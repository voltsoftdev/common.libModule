package com.dev.voltsoft.lib.db.query;

import android.content.Context;
import com.dev.voltsoft.lib.db.DBQueryHandler;
import com.dev.voltsoft.lib.model.BaseRequest;

public abstract class DBQuery extends BaseRequest implements Runnable {

    private Context mContext;
    private DBQueryType         mDBQueryType;

    public DBQuery(DBQueryType queryType)
    {
        mDBQueryType = queryType;
    }

    public Context getContext()
    {
        return mContext;
    }

    public void setContext(Context c)
    {
        this.mContext = c;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run()
    {
        if (mContext != null && mDBQueryType != null)
        {
            DBQueryHandler.getInstance().handle(this);
        }
    }

    public DBQueryType getDbRequestType()
    {
        return mDBQueryType;
    }
}
