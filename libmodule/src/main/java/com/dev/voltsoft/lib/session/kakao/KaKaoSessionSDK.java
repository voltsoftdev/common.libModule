package com.dev.voltsoft.lib.session.kakao;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import com.dev.voltsoft.lib.session.ISessionLoginListener;
import com.dev.voltsoft.lib.session.ISessionLogoutListener;
import com.dev.voltsoft.lib.session.ISessionSDK;
import com.dev.voltsoft.lib.utility.EasyLog;
import com.kakao.auth.*;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

public class KaKaoSessionSDK extends KakaoAdapter implements ISessionSDK<UserProfile> {

    private static class LazyHolder
    {
        private static KaKaoSessionSDK mInstance = new KaKaoSessionSDK();
    }

    private Application         mApplication;

    private AppCompatActivity   mTopActivity;

    public void init(Application application)
    {
        mApplication = application;

        KakaoSDK.init(LazyHolder.mInstance);
    }

    public static KaKaoSessionSDK getInstance()
    {
        return LazyHolder.mInstance;
    }

    @Override
    public IApplicationConfig getApplicationConfig()
    {
        return new IApplicationConfig()
        {

            @Override
            public Activity getTopActivity()
            {
                return mTopActivity;
            }

            @Override
            public Context getApplicationContext()
            {
                return mApplication;
            }
        };
    }

    @Override
    public ISessionConfig getSessionConfig()
    {
        return new ISessionConfig() {
            @Override
            public AuthType[] getAuthTypes()
            {
                return new AuthType[]{AuthType.KAKAO_TALK};
            }

            @Override
            public boolean isUsingWebviewTimer()
            {
                return false;
            }

            @Override
            public ApprovalType getApprovalType()
            {
                return ApprovalType.INDIVIDUAL;
            }

            @Override
            public boolean isSaveFormData()
            {
                return true;
            }
        };
    }

    @Override
    public void login(final AppCompatActivity activity , final ISessionLoginListener<UserProfile> loginListener)
    {
        EasyLog.LogMessage(">> KaKao login");

        mTopActivity = activity;

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult)
            {
                EasyLog.LogMessage(">> KaKao onSessionClosed");

                ErrorCode errorCode = ErrorCode.valueOf(errorResult.getErrorCode());

                if (errorCode == ErrorCode.CLIENT_ERROR_CODE)
                {
                    EasyLog.LogMessage("-- KaKao onSessionClosed ErrorCode.CLIENT_ERROR_CODE");

                    if (loginListener != null)
                    {
                        loginListener.onError();
                    }
                }
            }

            @Override
            public void onNotSignedUp()
            {
                EasyLog.LogMessage(">> KaKao onNotSignedUp");

                if (loginListener != null)
                {
                    loginListener.onError();
                }
            }

            @Override
            public void onSuccess(UserProfile userProfile)
            {
                EasyLog.LogMessage(">> KaKao onSuccess");

                if (loginListener != null)
                {
                    loginListener.onLogin(userProfile);
                }
            }

            @Override
            public void onFailure(ErrorResult errorResult)
            {
                super.onFailure(errorResult);

                if (loginListener != null)
                {
                    loginListener.onError();
                }
            }
        });
    }

    @Override
    public void logout(AppCompatActivity a, final ISessionLogoutListener listener)
    {
        EasyLog.LogMessage(">> KaKao logout");

        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout()
            {
                EasyLog.LogMessage(">> KaKao logout success");

                if (listener != null)
                {
                    listener.onError();
                }
            }
        });
    }

    @Override
    public void handleActivityResult(int requestCode, int resultCode, Intent data)
    {
        Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data);
    }
}
