package com.dev.voltsoft.root.model.request;

import android.content.ContentValues;
import com.dev.voltsoft.lib.network.NetworkRequest;
import com.dev.voltsoft.lib.network.base.NetworkParcelable;
import com.dev.voltsoft.lib.network.parse.JSONParcelable;
import com.dev.voltsoft.root.model.SampleData001;
import org.json.JSONObject;

public class RequestSampleData001 extends NetworkRequest
{

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
        ContentValues contentValues = new ContentValues();

        return contentValues;
    }


    @Override
    public NetworkParcelable getNetworkParcelable()
    {
        return new JSONParcelable<SampleData001>()
        {
            @Override
            public SampleData001 parse(JSONObject jsonObject)
            {
                SampleData001 sampleData001 = new SampleData001();

                try
                {
                    int responseCode = jsonObject.getInt("ResponseCode");

                    String responseMessage = jsonObject.getString("ResponseMessage");

                    sampleData001.ReponseCode = responseCode;

                    sampleData001.ResponseMessage = responseMessage;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                return sampleData001;
            }
        };
    }
}
