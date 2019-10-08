package com.dev.voltsoft.lib.session;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public interface ISessionSDK {

    void login(SessionLogin sessionLogin);

    void logout(SessionLogout sessionLogout);

    void handleActivityResult(AppCompatActivity activity, int requestCode, int resultCode, Intent data);
}
