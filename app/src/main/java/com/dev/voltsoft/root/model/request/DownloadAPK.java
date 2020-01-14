package com.dev.voltsoft.root.model.request;

import android.content.ContentValues;

import com.dev.voltsoft.lib.network.NetworkRequest;
import com.dev.voltsoft.lib.network.base.IFileDownLoader;
import com.dev.voltsoft.lib.network.base.NetworkParcelable;

public class DownloadAPK extends NetworkRequest implements IFileDownLoader {

    public String DownloadDirectory;

    public String fileName;

    public DownloadAPK()
    {
        setTargetUrl("http://voltsoftware.co.kr:8080/smartParave/app-release.apk");
    }

    @Override
    public String getHttpMethod()
    {
        return HttpGet;
    }

    @Override
    public ContentValues getHttpRequestHeader()
    {
        return null;
    }

    @Override
    public ContentValues getHttpRequestParameter()
    {
        return null;
    }

    @Override
    public NetworkParcelable getNetworkParcelable()
    {
        return null;
    }

    @Override
    public String getDownloadDirectory()
    {
        return DownloadDirectory;
    }

    @Override
    public String getDestinationFileName()
    {
        return fileName;
    }
}
