package com.dev.voltsoft.lib.network;

import android.support.annotation.MainThread;
import android.text.TextUtils;
import com.dev.voltsoft.lib.IRequestHandler;
import com.dev.voltsoft.lib.IResponseListener;
import com.dev.voltsoft.lib.model.BaseRequest;
import com.dev.voltsoft.lib.network.base.*;

import java.util.LinkedList;
import java.util.Observable;
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

    public void init()
    {

    }

    private URLGeneratorStrategy mURLGeneratorStrategy;

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
    public void update(Observable observable, Object data)
    {

    }

    @Override
    @SuppressWarnings("unchecked")
    public void request(NetworkRequest r)
    {
        if (NetworkState.getInstance().isNetworkAvailable())
        {
            if (isNetworkThreadIdle(r))
            {
                IResponseListener responseListener = r.getResponseListener();

                if (protocolsQueueHashMap.get(r) == null)
                {
                    protocolsQueueHashMap.put(r , new LinkedList<IResponseListener>());
                }
                protocolsQueueHashMap.get(r).add(responseListener);

                String url = (mURLGeneratorStrategy == null ? null : mURLGeneratorStrategy.create(r));

                if (!TextUtils.isEmpty(url))
                {
                    HttpRequest httpRequest = new HttpRequest();
                    httpRequest.setNetworkRequest(r);
                    httpRequest.setUrlData(url);
                    httpRequest.setHttpMethod(r.getHttpMethod());
                    httpRequest.setRequestHttpHeader(r.getHttpRequestHeader());
                    httpRequest.setParameterValues(r.getHttpRequestParameter());

                    NetworkExecutor networkExecutor = new NetworkExecutor();
                    networkExecutor.setNetworkRequester(httpRequest);
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

                if (responseListener != null) responseListener.onResponseListen(response);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setURLGeneratorStrategy(URLGeneratorStrategy strategy)
    {
        this.mURLGeneratorStrategy = strategy;
    }
}
