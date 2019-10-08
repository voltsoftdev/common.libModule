package com.dev.voltsoft.root.model.request;

import android.content.ContentValues;
import android.util.Log;
import com.dev.voltsoft.lib.network.NetworkRequest;
import com.dev.voltsoft.lib.network.base.NetworkParcelable;
import com.dev.voltsoft.lib.network.parse.JSONParcelable;
import com.dev.voltsoft.root.model.LottoPrize;
import org.json.JSONObject;

public class RequestLottoPrize extends NetworkRequest implements JSONParcelable<LottoPrize>
{
    public int number;

    public RequestLottoPrize()
    {
        setTargetUrl("https://www.dhlottery.co.kr/common.do?");
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

        ContentValues contentValues = new ContentValues();

        contentValues.put("method", "getLottoNumber");
        contentValues.put("drwNo", number);

        return contentValues;
    }

    @Override
    public NetworkParcelable getNetworkParcelable()
    {
        return this;
    }

    @Override
    public LottoPrize parse(JSONObject jsonObject)
    {
        Log.d("woozie", ">> RequestLottoPrize response = " + jsonObject.toString());

        return null;
    }
}
