package com.dev.voltsoft.lib.session.kakao;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.dev.voltsoft.lib.session.*;
import com.dev.voltsoft.lib.utility.EasyLog;
import com.kakao.auth.*;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

public class KaKaoSessionSDK extends KakaoAdapter implements ISessionSDK {

    private static class LazyHolder
    {
        private static KaKaoSessionSDK mInstance = new KaKaoSessionSDK();
    }

    private Application         mApplication;

    private AppCompatActivity   mTopActivity;

    public void init(Application application)
    {
        try
        {
            mApplication = application;

            KakaoSDK.init(LazyHolder.mInstance);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
    public void login(SessionLogin sessionLogin)
    {
        EasyLog.LogMessage(">> KaKao login");

        final KaKaoSessionLogin kaKaoSessionLogin = (KaKaoSessionLogin) sessionLogin;

        final ISessionLoginListener<UserProfile> loginListener = kaKaoSessionLogin.getSessionLoginListener();

        mTopActivity = kaKaoSessionLogin.getAppCompatActivity();

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
    public void logout(final SessionLogout sessionLogout)
    {
        EasyLog.LogMessage(">> KaKao logout");

        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout()
            {
                EasyLog.LogMessage(">> KaKao logout success");

                if (sessionLogout.getSessionLogoutListener() != null)
                {
                    sessionLogout.getSessionLogoutListener().onError();
                }
            }
        });
    }

    @Override
    public void handleActivityResult(AppCompatActivity activity, int requestCode, int resultCode, Intent data)
    {
        Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data);
    }
}
