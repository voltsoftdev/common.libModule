package com.dev.voltsoft.lib.network.base;

public interface INetworkProgressView
{

    void onLoading();

    void updateProgress(int progress);

    void onLoadingEnd();
}
