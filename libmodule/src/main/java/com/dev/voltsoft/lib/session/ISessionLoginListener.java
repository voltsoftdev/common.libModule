package com.dev.voltsoft.lib.session;

public interface ISessionLoginListener<R> {

    void onLogin(R r);

    void onError(Object... o);
}
