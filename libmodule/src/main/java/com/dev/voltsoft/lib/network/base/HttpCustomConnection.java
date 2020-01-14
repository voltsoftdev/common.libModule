package com.dev.voltsoft.lib.network.base;

import android.content.ContentValues;
import androidx.annotation.WorkerThread;
import android.text.TextUtils;

import com.dev.voltsoft.lib.model.BaseModel;
import com.dev.voltsoft.lib.network.NetworkRequest;
import com.dev.voltsoft.lib.network.exception.NetworkException;
import com.dev.voltsoft.lib.utility.EasyLog;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;

public abstract class HttpCustomConnection implements NetworkConstant
{

    protected final static int DEFAULT_TIMEOUT = 5 * 1000;

    protected final static String TWO_HYPENS  = "--";

    protected final static String BOUNDARY    = "*************************";

    protected final static String LINE_END    = "\r\n";

    protected final static int MAX_BUFFER_SIZE = 1024 * 1024;

    protected final NetworkRequest requestCommand;

    public HttpCustomConnection(NetworkRequest request)
    {
        requestCommand = request;
    }

    @WorkerThread
    public abstract <M extends BaseModel> Object execute() throws NetworkException, NullPointerException;

    public NetworkRequest getNetworkRequest()
    {
        return requestCommand;
    }

    protected String buildParameter() throws UnsupportedEncodingException
    {
        StringBuilder stringBuilder = new StringBuilder();

        if (requestCommand.getHttpRequestParameter() != null)
        {
            ContentValues values = requestCommand.getHttpRequestParameter();

            for (Map.Entry<String, Object> entry : values.valueSet())
            {
                String parameterName = URLEncoder.encode(entry.getKey() , "UTF-8");

                Object o = entry.getValue();

                if (o != null)
                {
                    String parameterValue = URLEncoder.encode(o.toString() , "UTF-8");

                    if (!TextUtils.isEmpty(stringBuilder.toString()))
                    {
                        stringBuilder.append("&");
                    }

                    stringBuilder.append(parameterName);
                    stringBuilder.append("=");
                    stringBuilder.append(parameterValue);
                }
            }

            EasyLog.LogMessage("*********************************************************************");
            EasyLog.LogMessage(">> URL :" , requestCommand.mTargetUrl);
            EasyLog.LogMessage(">> parameter :", stringBuilder.toString());
            EasyLog.LogMessage("*********************************************************************");
        }

        return stringBuilder.toString();
    }

    protected void printServerLog(String responseData)
    {
        EasyLog.LogMessage("*********************************************************************");
        EasyLog.LogMessage(">> URL :", requestCommand.mTargetUrl);
        EasyLog.LogMessage(">> responseData :", responseData);
        EasyLog.LogMessage("*********************************************************************");
    }

    protected boolean isValidHttpConnection(int httpResponseCode)
    {
        switch (httpResponseCode)
        {
            case HttpURLConnection.HTTP_OK:
            case HttpURLConnection.HTTP_CREATED:
                return true;

            default:
                return false;
        }
    }

    protected static void trustAllHosts()
    {

        // Create a trust manager that does not validate certificate chains

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager()
        {

            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return new java.security.cert.X509Certificate[]{};
            }


            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException
            {

            }


            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException
            {

            }

        }};

        try
        {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
