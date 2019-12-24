package com.dev.voltsoft.root.model.request;

import android.content.ContentValues;

import com.dev.voltsoft.lib.network.NetworkRequest;
import com.dev.voltsoft.lib.network.base.IFileUpLoader;
import com.dev.voltsoft.lib.network.base.NetworkParcelable;

public class PostFaceImage extends NetworkRequest implements IFileUpLoader
{
    public int home_id;

    public String tel_num;

    public String user_id;

    public String filePath;

    public PostFaceImage()
    {
        setTargetUrl("http://woozie.iptime.org:9080/parabApp.do?mode=registSelfie&");
    }

    @Override
    public String getHttpMethod()
    {
        return HttpPost;
    }

    @Override
    public ContentValues getHttpRequestHeader()
    {
        return null;
    }

    @Override
    public ContentValues getHttpRequestParameter()
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put("home_id", home_id);
        contentValues.put("tel_num", tel_num);
        contentValues.put("user_id", user_id);

        return contentValues;
    }

    @Override
    public NetworkParcelable getNetworkParcelable()
    {
        return null;
    }

    @Override
    public String getFilePath()
    {
        return filePath;
    }

    @Override
    public String getFileField()
    {
        return "selfie";
    }

    @Override
    public String getFileMimeType()
    {
        return null;
    }
}
