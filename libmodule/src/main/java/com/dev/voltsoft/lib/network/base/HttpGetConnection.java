package com.dev.voltsoft.lib.network.base;

import android.content.ContentValues;
import android.text.TextUtils;

import com.dev.voltsoft.lib.model.BaseModel;
import com.dev.voltsoft.lib.network.NetworkRequest;
import com.dev.voltsoft.lib.network.exception.NetworkException;
import com.dev.voltsoft.lib.network.exception.NetworkExceptionEnum;
import com.dev.voltsoft.lib.network.parse.JSONArrayParcelable;
import com.dev.voltsoft.lib.network.parse.JSONParcelable;
import com.dev.voltsoft.lib.network.parse.XMLArrayParcelable;
import com.dev.voltsoft.lib.network.parse.XMLParcelable;
import com.dev.voltsoft.lib.utility.EasyLog;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class HttpGetConnection extends HttpCustomConnection
{
    public HttpGetConnection(NetworkRequest request)
    {
        super(request);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <M extends BaseModel> Object execute() throws NetworkException, NullPointerException
    {
        Object response = null;

        HttpURLConnection connection = null;

        try
        {
            StringBuilder strUrlBuilder = new StringBuilder();
            strUrlBuilder.append(requestCommand.mTargetUrl);
            strUrlBuilder.append(buildParameter());

            EasyLog.LogMessage(this, "++ Http Method HttpGet ");

            EasyLog.LogMessage(this, "++ Http URL = " + requestCommand.mTargetUrl);

            URL url = new URL(strUrlBuilder.toString());

            if (requestCommand.mTargetUrl.startsWith("https"))
            {
                trustAllHosts();

                EasyLog.LogMessage(this, "++ trustAllHosts !");

                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setHostnameVerifier(new HostnameVerifier()
                {
                    @Override
                    public boolean verify(String s, SSLSession sslSession)
                    {
                        return true;
                    }
                });

                connection = httpsURLConnection;
            }
            else
            {
                connection = (HttpURLConnection) url.openConnection();
            }

            connection.setConnectTimeout(DEFAULT_TIMEOUT);
            connection.setRequestMethod(HttpGet);

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");

            if (requestCommand.getHttpRequestHeader() != null)
            {
                ContentValues values = requestCommand.getHttpRequestHeader();

                for (String key : values.keySet())
                {
                    String v = values.getAsString(key);

                    connection.setRequestProperty(key , v);
                }
            }
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();

            if (isValidHttpConnection(connection.getResponseCode()))
            {
                List<String> cookies = (connection.getHeaderFields() != null ? connection.getHeaderFields().get("Set-Cookie") : null);

                int cookiesSize = (cookies == null ? 0 : cookies.size());
                if (cookiesSize > 0)
                {
                    for (String s : cookies)
                    {
                        EasyLog.LogMessage(this, "++ cookie = " + s);
                    }
                }

                if (requestCommand instanceof IFileDownLoader)
                {
                    IFileDownLoader fileDownLoader = (IFileDownLoader) requestCommand;

                    String destinationDirectory = fileDownLoader.getDownloadDirectory();

                    String destinationFileName = fileDownLoader.getDestinationFileName();

                    if (!TextUtils.isEmpty(destinationDirectory) && !TextUtils.isEmpty(destinationFileName))
                    {
                        int downloadSize = connection.getContentLength();

                        if (downloadSize > 0)
                        {
                            File file = new File(destinationDirectory, destinationFileName);

                            if (file.exists())
                            {
                                file.delete();
                            }

                            InputStream inputStream = new BufferedInputStream(url.openStream());

                            FileOutputStream fileOutputStream = new FileOutputStream(file);

                            byte data[] = new byte[1024];

                            long total = 0; int count = 0;

                            while ((count = inputStream.read(data)) != -1)
                            {
                                total += count;

                                int progress = (int) (total * 100 / downloadSize);

                                if (requestCommand.mNetworkProgressView != null)
                                {
                                    requestCommand.mNetworkProgressView.updateProgress(progress);
                                }

                                fileOutputStream.write(data, 0, count);
                            }

                            inputStream.close();

                            fileOutputStream.close();

                            response = file;
                        }
                    }
                }
                else
                {
                    InputStream inputStream = connection.getInputStream();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder stringBuilder = new StringBuilder();

                    while (true)
                    {
                        String stringLine = bufferedReader.readLine();

                        if (stringLine == null) break;

                        stringBuilder.append(stringLine).append('\n');
                    }

                    bufferedReader.close();

                    inputStream.close();

                    NetworkParcelable np = requestCommand.getNetworkParcelable();

                    if (np instanceof JSONParcelable)
                    {
                        JSONParcelable<M> jsonParcelable = (JSONParcelable) np;

                        JSONObject jsonObject = new JSONObject(stringBuilder.toString());

                        response = jsonParcelable.parse(jsonObject);
                    }
                    else if (np instanceof JSONArrayParcelable)
                    {
                        JSONArrayParcelable<M> jsonParcelable = (JSONArrayParcelable) np;

                        JSONObject jsonObject = new JSONObject(stringBuilder.toString());

                        response = jsonParcelable.parse(jsonObject);
                    }
                    else if (np instanceof XMLArrayParcelable)
                    {
                        XMLArrayParcelable<M> xmlParcelable = (XMLArrayParcelable) np;

                        response = xmlParcelable.parse(stringBuilder.toString());
                    }
                    else if (np instanceof XMLParcelable)
                    {
                        XMLParcelable<M> xmlParcelable = (XMLParcelable) np;

                        response = xmlParcelable.parse(stringBuilder.toString());
                    }
                }
            }
            else
            {

                EasyLog.LogMessage("-- ServerResponseError [http error code] " + connection.getResponseCode());
                EasyLog.LogMessage("-- ServerResponseError [http error Message] " + connection.getResponseMessage());

                StringBuilder responseBuilder = new StringBuilder();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                while (true)
                {
                    String stringLine = bufferedReader.readLine();

                    if (stringLine == null) break;

                    responseBuilder.append(stringLine).append('\n');
                }

                printServerLog(responseBuilder.toString());

                bufferedReader.close();

                connection.getErrorStream().close();

                EasyLog.LogMessage("-- ServerResponseError [http error Message] " + responseBuilder.toString());

                NetworkException networkException = new NetworkException();

                networkException.setExceptionEnum(NetworkExceptionEnum.NOFOUND_PARAMETER);

                networkException.setErrorMessage(responseBuilder.toString());

                throw networkException;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }

        return response;
    }
}
