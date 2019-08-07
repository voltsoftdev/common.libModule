package com.dev.voltsoft.lib;

import com.dev.voltsoft.lib.db.query.DBQuery;
import com.dev.voltsoft.lib.firebase.db.FireBaseDBRequest;
import com.dev.voltsoft.lib.model.BaseRequest;
import com.dev.voltsoft.lib.network.NetworkRequest;
import com.dev.voltsoft.lib.session.SessionLogin;
import com.dev.voltsoft.lib.session.SessionLogout;
import com.dev.voltsoft.lib.session.SessionRequestHandler;

public class RequestHandler implements IRequestHandler<BaseRequest>
{

    private static class LazyHolder
    {
        private static RequestHandler mInstance = new RequestHandler();
    }

    public static RequestHandler getInstance()
    {
        return LazyHolder.mInstance;
    }

    public void handle(BaseRequest ... requests)
    {
        if (requests != null)
        {
            for (BaseRequest r : requests)
            {
                handle(r);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(BaseRequest r)
    {
        if (r instanceof SessionLogin)
        {
            SessionRequestHandler.getInstance().handle(r);
        }
        else if (r instanceof SessionLogout)
        {
            SessionRequestHandler.getInstance().handle(r);
        }
        else if (r instanceof NetworkRequest)
        {
            NetworkRequest networkRequest = (NetworkRequest) r;

            Thread thread = new Thread(networkRequest);
            thread.start();
        }
        else if (r instanceof DBQuery)
        {
            DBQuery dbQuery = (DBQuery) r;

            Thread thread = new Thread(dbQuery);
            thread.start();
        }
        else if (r instanceof FireBaseDBRequest)
        {
            FireBaseDBRequest fireBaseDBRequest = (FireBaseDBRequest) r;

            Thread thread = new Thread(fireBaseDBRequest);
            thread.start();
        }
    }
}
