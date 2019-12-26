package com.dev.voltsoft.lib.network.base;

import android.annotation.SuppressLint;
import android.content.ContentValues;

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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class HttpPostConnection extends HttpCustomConnection
{
    public HttpPostConnection(NetworkRequest request)
    {
        super(request);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <M extends BaseModel> Object execute() throws NullPointerException
    {
        Object response = null;

        HttpURLConnection connection = null;

        try
        {
            EasyLog.LogMessage(this, "++ Http Method HttpGet ");

            EasyLog.LogMessage(this, "++ Http URL = " + requestCommand.mTargetUrl);

            StringBuilder strUrlBuilder = new StringBuilder();
            strUrlBuilder.append(requestCommand.mTargetUrl);
            strUrlBuilder.append(buildParameter());

            URL url = new URL(strUrlBuilder.toString());

            if (requestCommand.mTargetUrl.startsWith("https"))
            {
                trustAllHosts();

                EasyLog.LogMessage(this, "++ trustAllHosts !");

                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setHostnameVerifier(new HostnameVerifier()
                {
                    @SuppressLint("BadHostnameVerifier")
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
            connection.setRequestMethod(HttpPost);

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cache-Control", "no-cache");

            if (requestCommand instanceof IFileUpLoader)
            {
                connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            }
            else
            {
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "utf-8");
            }

            if (requestCommand.getHttpRequestHeader() != null)
            {
                ContentValues values = requestCommand.getHttpRequestHeader();

                for (String key : values.keySet())
                {
                    String v = values.getAsString(key);

                    connection.setRequestProperty(key , v);
                }
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            if (requestCommand instanceof IFileUpLoader)
            {
                IFileUpLoader fileUpLoader = (IFileUpLoader) requestCommand;

                String filePath     = fileUpLoader.getFilePath();

                String fileMimeType = fileUpLoader.getFileMimeType();

                String fileField    = fileUpLoader.getFileField();

                String[] q = filePath.split("/");

                int id = q.length - 1;

                String headerParameter = "";
                headerParameter += TWO_HYPENS + BOUNDARY + LINE_END;
                headerParameter += "Content-Disposition: form-data; name=\"" + fileField + "\"; filename=\"" + q[id] + "\"" + LINE_END;
                headerParameter += "Content-Type: " + fileMimeType + LINE_END;
                headerParameter += "Content-Transfer-Encoding: binary" + LINE_END;
                headerParameter += LINE_END;

                outputStream.write(headerParameter.getBytes());

                File file = new File(filePath);

                FileInputStream fileInputStream = new FileInputStream(file);

                int bytesAvailable = fileInputStream.available();

                int bufferSize = Math.min(bytesAvailable, MAX_BUFFER_SIZE);

                byte[] buffer = new byte[bufferSize];

                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0)
                {
                    outputStream.write(buffer, 0, bufferSize);

                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, MAX_BUFFER_SIZE);

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                outputStream.writeBytes(LINE_END);

                outputStream.writeBytes(TWO_HYPENS + BOUNDARY + TWO_HYPENS + LINE_END);

                fileInputStream.close();
            }

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            bufferedWriter.write(buildParameter());
            bufferedWriter.flush();
            bufferedWriter.close();

            outputStream.close();

            if (isValidHttpConnection(connection.getResponseCode()))
            {

                List<String> cookies = (connection.getHeaderFields() != null ?
                        connection.getHeaderFields().get("Set-Cookie") : null);
                int cookiesSize = (cookies == null ? 0 : cookies.size());
                if (cookiesSize > 0)
                {
                    for (String s : cookies)
                    {
                        EasyLog.LogMessage(this, "++ cookie = " + s);
                    }
                }

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
