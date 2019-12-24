package com.dev.voltsoft.lib.network;

import android.util.Log;
import androidx.annotation.MainThread;
import android.text.TextUtils;
import com.dev.voltsoft.lib.IRequestHandler;
import com.dev.voltsoft.lib.IResponseListener;
import com.dev.voltsoft.lib.model.BaseRequest;
import com.dev.voltsoft.lib.network.base.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkRequestHandler implements NetworkConstant, IRequestHandler<NetworkRequest> {

    private static class LazyHolder
    {
        private static NetworkRequestHandler mInstance = new NetworkRequestHandler();
    }

    public static NetworkRequestHandler getInstance()
    {
        return LazyHolder.mInstance;
    }

    private ConcurrentHashMap<BaseRequest, Queue<IResponseListener>> protocolsQueueHashMap = new ConcurrentHashMap<>();

    public boolean isAnyNetworkThreadProcess()
    {
        boolean networkInProgress = false;

        for (BaseRequest request : protocolsQueueHashMap.keySet())
        {
            Queue<IResponseListener> queue = protocolsQueueHashMap.get(request);

            if (queue.size() > 0)
            {
                networkInProgress = true;
                break;
            }
        }

        return networkInProgress;
    }

    public boolean isNetworkThreadProcess(NetworkRequest r)
    {
        Queue<IResponseListener> queue = protocolsQueueHashMap.get(r);

        return (queue != null && queue.size() > 0);
    }

    public boolean isNetworkThreadIdle(NetworkRequest r) {
        return !isNetworkThreadProcess(r);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(NetworkRequest r)
    {
        Log.d("woozie", ">> NetworkRequestHandler isNetworkAvailable = " + NetworkState.getInstance().isNetworkAvailable());

        if (NetworkState.getInstance().isNetworkAvailable())
        {
            Log.d("woozie", ">> NetworkRequestHandler isNetworkThreadIdle = " + isNetworkThreadIdle(r));

            if (isNetworkThreadIdle(r))
            {
                IResponseListener responseListener = r.getResponseListener();

                if (protocolsQueueHashMap.get(r) == null)
                {
                    protocolsQueueHashMap.put(r , new LinkedList<IResponseListener>());
                }
                protocolsQueueHashMap.get(r).add(responseListener);

                String url = r.getTargetUrl();

                if (!TextUtils.isEmpty(url))
                {
                    String method = (TextUtils.isEmpty(r.getHttpMethod()) ? HttpGet : r.getHttpMethod());

                    HttpCustomConnection httpCustomConnection = (HttpGet.equalsIgnoreCase(method) ?
                            new HttpGetConnection(r) :
                            new HttpPostConnection(r));

                    NetworkExecutor networkExecutor = new NetworkExecutor();
                    networkExecutor.setNetworkRequester(httpCustomConnection);
                    networkExecutor.setProgressView(r.mNetworkProgressView);
                    networkExecutor.execute();
                }
            }
        }
        else
        {
            NetworkState.getInstance().enqueuePreservedNetworkTask(r);
        }
    }

    /**
     * @param response {@link NetworkResponse}
     */
    @SuppressWarnings("unchecked")
    @MainThread
    public void receiveResponse(NetworkResponse response)
    {
        try
        {
            BaseRequest request = response.getSourceRequest();

            Queue<IResponseListener> queue = protocolsQueueHashMap.get(request);

            if (queue != null)
            {
                IResponseListener responseListener = queue.poll();

                if (responseListener != null)
                {
                    responseListener.onResponseListen(response);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
