package com.dev.voltsoft.lib.network.base;

import android.os.AsyncTask;
import com.dev.voltsoft.lib.model.BaseModel;
import com.dev.voltsoft.lib.network.NetworkRequestHandler;
import com.dev.voltsoft.lib.network.NetworkState;
import com.dev.voltsoft.lib.network.exception.NetworkException;
import com.dev.voltsoft.lib.network.exception.NetworkExceptionEnum;


public class NetworkExecutor extends AsyncTask<Object , Integer , Object> {

    private static final String NETWORK_ERROR_MESSAGE = "네트워크가 상태 확인 필요";

    private HttpRequest         mNetworkRequester;
    private NetworkException    mException = null;

    private INetworkProgressView mProgressView;

    @Override
    protected Object doInBackground(Object... os)
    {
        try {

            if (NetworkState.getInstance().isNetworkAvailable())
            {
                publishProgress();

                return mNetworkRequester.execute();
            }
            else
            {
                NetworkException networkException = new NetworkException();
                networkException.setExceptionEnum(NetworkExceptionEnum.NETWORK_NOT_AVAILABLE);
                networkException.setErrorMessage(NETWORK_ERROR_MESSAGE);

                throw networkException;
            }

        }
        catch (NetworkException e)
        {
            setWorkerException(e);

            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values)
    {
        super.onProgressUpdate(values);


        if (mProgressView != null)
        {
            int updateProgress = (values.length == 0 ? 1 : values[0]);
            if (updateProgress <= 1)
            {
                mProgressView.onLoading();
            }
            else
            {
                mProgressView.updateProgress(updateProgress);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onPostExecute(Object m)
    {
        NetworkResponse networkResponse = new NetworkResponse();
        networkResponse.setSourceRequest(mNetworkRequester.getNetworkRequest());

        if (mException == null)
        {
            networkResponse.setResponseCode(1);
            networkResponse.setResponseModel(m);
        }
        else
        {
            networkResponse.setResponseCode(mException.getErrorCode());
            networkResponse.setNetworkException(mException);
        }

        NetworkRequestHandler.getInstance().receiveResponse(networkResponse);

        if (mProgressView != null)
        {
            mProgressView.onLoadingEnd();
        }
    }

    public HttpRequest getNetworkRequestor()
    {
        return mNetworkRequester;
    }

    public void setNetworkRequester(HttpRequest r)
    {
        this.mNetworkRequester = r;
    }

    public NetworkException getWorkerException()
    {
        return mException;
    }

    public void setWorkerException(NetworkException e)
    {
        this.mException = e;
    }

    public void setProgressView(INetworkProgressView progressView)
    {
        this.mProgressView = progressView;
    }
}
