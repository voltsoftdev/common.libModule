package com.dev.voltsoft.root.components.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.dev.voltsoft.lib.IResponseListener;
import com.dev.voltsoft.lib.RequestHandler;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.lib.model.BaseResponse;
import com.dev.voltsoft.lib.network.base.INetworkProgressView;
import com.dev.voltsoft.lib.network.base.NetworkResponse;
import com.dev.voltsoft.lib.utility.EasyLog;
import com.dev.voltsoft.root.BuildConfig;
import com.dev.voltsoft.root.R;
import com.dev.voltsoft.root.model.request.DownloadAPK;

import java.io.File;
import java.util.Locale;
import java.util.Objects;

public class PageDownload extends CommonActivity implements INetworkProgressView
{
    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.page_pic_upload);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DownloadAPK downloadAPK = new DownloadAPK();
                downloadAPK.DownloadDirectory = getCacheDir().toString();
                downloadAPK.fileName = "application.apk";
                downloadAPK.setNetworkProgressView(PageDownload.this);
                downloadAPK.setResponseListener(new IResponseListener()
                {
                    @Override
                    public void onResponseListen(BaseResponse response)
                    {
                        NetworkResponse networkResponse = (NetworkResponse) response;

                        File file = (File) networkResponse.getResponseModel();

                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        Uri apkUri = null;

                        if (Build.VERSION.SDK_INT >= 24)
                        {
                            Uri uri = FileProvider.getUriForFile(PageDownload.this, BuildConfig.APPLICATION_ID + ".provider", file);

                            intent.setDataAndType(uri, "application/vnd.android.package-archive");

                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);

                            intent.setData(uri);

                            startActivity(intent);
                        }
                        else
                        {
                            apkUri = Uri.fromFile(file);

                            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            getApplicationContext().startActivity(intent);
                        }
                    }
                });

                RequestHandler.getInstance().handle(downloadAPK);

            }
        });
    }

    @Override
    public void onLoading()
    {

    }

    @Override
    public void updateProgress(int progress)
    {
        EasyLog.LogMessage(">>downloadAPK .. progress = " + progress);
    }

    @Override
    public void onLoadingEnd() {

    }
}
