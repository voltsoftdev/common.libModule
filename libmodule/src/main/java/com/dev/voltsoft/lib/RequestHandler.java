package com.dev.voltsoft.lib;

import com.dev.voltsoft.lib.db.query.DBQuery;
import com.dev.voltsoft.lib.model.BaseRequest;
import com.dev.voltsoft.lib.network.NetworkRequest;
import com.dev.voltsoft.lib.session.SessionLogin;
import com.dev.voltsoft.lib.session.SessionLogout;
import com.dev.voltsoft.lib.session.SessionRequestHandler;
import com.dev.voltsoft.lib.session.SessionWait;

import java.util.Observable;

public class RequestHandler implements IRequestHandler<BaseRequest> {

    private static class LazyHolder
    {
        private static RequestHandler mInstance = new RequestHandler();
    }

    public static RequestHandler getInstance()
    {
        return LazyHolder.mInstance;
    }

    @Override
    public void update(Observable observable, Object data)
    {

    }

    @Override
    @SuppressWarnings("unchecked")
    public void request(BaseRequest r)
    {
        if (r != null)
        {
            if (r instanceof SessionLogin ||
                r instanceof SessionLogout ||
                r instanceof SessionWait)
            {
                SessionRequestHandler.getInstance().request(r);
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
        }
    }
}
