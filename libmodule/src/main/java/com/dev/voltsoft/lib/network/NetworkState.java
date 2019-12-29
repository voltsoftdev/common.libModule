package com.dev.voltsoft.lib.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dev.voltsoft.lib.utility.UtilityData;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;
import java.util.concurrent.Executors;

public class NetworkState extends Observable {

    private static class LazyHolder
    {
        private static NetworkState mInstance = new NetworkState();
    }

    public static final int NETWORK_STATE = -9999;

    private boolean NetworkAvailable;

    private NetworkStateEnum        mNetworkState;

    private NetworkStatusReceiver   mNetworkStatusReceiver;

    private Queue<NetworkRequest> mPreservedTasks = new LinkedList<>();

    public static NetworkState getInstance()
    {
        return LazyHolder.mInstance;
    }

    public void registerReceiver(Context context)
    {
        try
        {
            if (mNetworkStatusReceiver == null)
            {
                mNetworkStatusReceiver = new NetworkStatusReceiver();

                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                context.registerReceiver(mNetworkStatusReceiver , intentFilter);

                NetworkAvailable = UtilityData.isNetworkAvailable(context);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

            mNetworkStatusReceiver = null;
        }
    }

    public void unregisterReceiver(Context context)
    {
        try
        {
            if (mNetworkStatusReceiver != null)
            {
                context.unregisterReceiver(mNetworkStatusReceiver);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            mNetworkStatusReceiver = null;
        }
    }

    public boolean isNotNetworkAvailable()
    {
        return !isNetworkAvailable();
    }

    public boolean isNetworkAvailable()
    {
        return NetworkAvailable;
    }

    public NetworkStateEnum getNetworkState()
    {
        return mNetworkState;
    }

    public boolean enqueuePreservedNetworkTask(NetworkRequest networkRequest)
    {

        try
        {
            String name = networkRequest.getClass().getSimpleName();

            for (NetworkRequest request : mPreservedTasks)
            {
                String comp = request.getClass().getSimpleName();

                if (comp.equalsIgnoreCase(name)) {
                    return false;
                }
            }

            mPreservedTasks.add(networkRequest);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return false;
        }
    }

    public void dequeuePreservedNetworkTask()
    {
        try
        {
            while (mPreservedTasks.peek() != null)
            {
                Runnable runnable = mPreservedTasks.poll();

                Executors.newFixedThreadPool(1).execute(runnable);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class NetworkStatusReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (activeNetworkInfo == null)
            {
                if (NetworkAvailable)
                {
                    NetworkAvailable = false;

                    setChanged();
                }

                mNetworkState = NetworkStateEnum.NO_NETWORK;

            }
            else
            {
                if (NetworkAvailable != activeNetworkInfo.isAvailable())
                {
                    NetworkAvailable = (activeNetworkInfo.isAvailable());

                    setChanged();
                }

                mNetworkState = (wifiNetworkInfo.isAvailable() ? NetworkStateEnum.WIFI : NetworkStateEnum.LTE);

                if (NetworkAvailable)
                {
                    dequeuePreservedNetworkTask();
                }
            }

            notifyObservers(mNetworkState);
        }
    }
}
