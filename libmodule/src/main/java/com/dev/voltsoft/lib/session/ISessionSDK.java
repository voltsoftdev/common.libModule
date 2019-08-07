package com.dev.voltsoft.lib.session;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public interface ISessionSDK<R> {

    void login(AppCompatActivity a, ISessionLoginListener<R> loginListener);

    void logout(AppCompatActivity a, ISessionLogoutListener logoutListener);

    void handleActivityResult(int requestCode, int resultCode, Intent data);
}
