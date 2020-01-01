package com.dev.voltsoft.lib;

import android.util.Log;

import com.dev.voltsoft.lib.db.query.DBQuery;
import com.dev.voltsoft.lib.firebase.db.FireBaseDBRequest;
import com.dev.voltsoft.lib.model.BaseRequest;
import com.dev.voltsoft.lib.network.NetworkRequest;
import com.dev.voltsoft.lib.session.SessionLogin;
import com.dev.voltsoft.lib.session.SessionLogout;
import com.dev.voltsoft.lib.session.SessionRequestHandler;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RequestHandler extends ThreadPoolExecutor implements IRequestHandler<BaseRequest>
{

    private static final int CORE_POOL_SIZE = 1;

    private static final int MAXIMUM_POOL_SIZE = 4;

    private static final int KEEP_ALIVE_TIME = 5;

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

    public RequestHandler()
    {
        super(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(BaseRequest r)
    {
        if (r instanceof SessionLogin)
        {
            SessionRequestHandler.getInstance().login((SessionLogin) r);
        }
        else if (r instanceof SessionLogout)
        {
            SessionRequestHandler.getInstance().logout((SessionLogout) r);
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

            execute(thread);
        }
        else if (r instanceof FireBaseDBRequest)
        {
            FireBaseDBRequest fireBaseDBRequest = (FireBaseDBRequest) r;

            Thread thread = new Thread(fireBaseDBRequest);

            execute(thread);
        }
    }
}
