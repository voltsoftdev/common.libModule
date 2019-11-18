package com.dev.voltsoft.lib.session;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import com.dev.voltsoft.lib.session.facebook.FacebookSessionSDK;
import com.dev.voltsoft.lib.session.google.GoogleSessionSDK;
import com.dev.voltsoft.lib.session.kakao.KaKaoSessionSDK;
import com.dev.voltsoft.lib.utility.EasyLog;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.security.MessageDigest;
import java.util.LinkedList;

public class SessionRequestHandler implements ISessionSDK {

    private LinkedList<String> HashKeyList = new LinkedList<>();

    private static class LazyHolder
    {
        private static SessionRequestHandler mInstance = new SessionRequestHandler();
    }

    public static SessionRequestHandler getInstance()
    {
        return LazyHolder.mInstance;
    }

    public void init(Application application)
    {


        try
        {
            KaKaoSessionSDK.getInstance().init(application);

            FacebookSdk.sdkInitialize(application);

            AppEventsLogger.activateApp(application);

            PackageManager packageManager = application.getPackageManager();

            String packageName = application.getPackageName();

            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            for (Signature signature : packageInfo.signatures)
            {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());

                String hashKey = Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT);

                EasyLog.LogMessage(this, "++ init hashKey = " + hashKey);

                HashKeyList.add(hashKey);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void login(SessionLogin sessionLogin)
    {
        SessionType sessionType = sessionLogin.getTargetSessionType();

        ISessionSDK iSessionSDK = sessionType.getSessionLoginSDK();
        iSessionSDK.login(sessionLogin);
    }

    @Override
    public void logout(SessionLogout sessionLogout)
    {
        SessionType sessionType = sessionLogout.getTargetSessionType();

        ISessionSDK iSessionSDK = sessionType.getSessionLoginSDK();
        iSessionSDK.logout(sessionLogout);
    }

    @Override
    public void handleActivityResult(AppCompatActivity activity, int requestCode, int resultCode, Intent data)
    {
        KaKaoSessionSDK.getInstance().handleActivityResult(activity, requestCode, resultCode, data);

        FacebookSessionSDK.getInstance().handleActivityResult(activity, requestCode, resultCode, data);

        GoogleSessionSDK.getInstance().handleActivityResult(activity, requestCode, resultCode, data);
    }

    public void pause(AppCompatActivity activity)
    {
        GoogleSessionSDK.getInstance().pause(activity);
    }
}
