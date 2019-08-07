package com.dev.voltsoft.lib.session.google;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.dev.voltsoft.lib.session.ISessionLoginListener;
import com.dev.voltsoft.lib.session.ISessionLogoutListener;
import com.dev.voltsoft.lib.session.ISessionSDK;
import com.dev.voltsoft.lib.utility.EasyLog;
import com.dev.voltsoft.lib.view.ActivityStackCallBackListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class GoogleSessionSDK implements ISessionSDK<GoogleSignInAccount> {

    public static final int GOOGLE_ACCOUNT_REQUEST = 9001;

    private GoogleApiClient         mGoogleApiClient;

    private ISessionLoginListener<GoogleSignInAccount> mSessionLoginListener;

    private static class LazyHolder
    {
        private static GoogleSessionSDK mInstance = new GoogleSessionSDK();
    }

    public static GoogleSessionSDK getInstance()
    {
        return GoogleSessionSDK.LazyHolder.mInstance;
    }

    @Override
    public void logout(final AppCompatActivity compatActivity, final ISessionLogoutListener logoutListener)
    {
        try
        {
            if (mGoogleApiClient.isConnected())
            {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>()
                {
                    @Override
                    public void onResult(@NonNull Status status)
                    {
                        EasyLog.LogMessage(">> doCloseGoogleAccountSession .. onResult");

                        if (logoutListener !=  null)
                        {
                            logoutListener.onLogout();
                        }
                    }
                });
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

            if (logoutListener !=  null)
            {
                logoutListener.onError();
            }
        }
    }

    @Override
    public void login(final AppCompatActivity appCompatActivity, ISessionLoginListener<GoogleSignInAccount> loginListener)
    {
        mSessionLoginListener = loginListener;

        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.stopAutoManage(appCompatActivity);
            mGoogleApiClient.disconnect();
        }

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(appCompatActivity).enableAutoManage(appCompatActivity, new GoogleApiClient.OnConnectionFailedListener()
                {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
                    {
                        EasyLog.LogMessage(">> onConnectionFailed");

                        if (mGoogleApiClient != null)
                        {
                            mGoogleApiClient.stopAutoManage(appCompatActivity);

                            mGoogleApiClient.disconnect();

                            if (mSessionLoginListener != null)
                            {
                                mSessionLoginListener.onError();
                            }
                        }
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API , googleSignInOptions)
                .build();
        mGoogleApiClient.connect();


        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        appCompatActivity.startActivityForResult(signInIntent, GOOGLE_ACCOUNT_REQUEST);

        OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (optionalPendingResult.isDone())
        {
            GoogleSignInResult googleSignInResult = optionalPendingResult.get();

            GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

            if (mSessionLoginListener != null)
            {
                mSessionLoginListener.onLogin(googleSignInAccount);
            }
        }
        else
        {
            optionalPendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {

                    if (googleSignInResult.isSuccess())
                    {
                        EasyLog.LogMessage("++ handleGoogleLoginResult isSuccess");

                        GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

                        if (mSessionLoginListener != null)
                        {
                            mSessionLoginListener.onLogin(googleSignInAccount);
                        }
                    }
                    else
                    {
                        EasyLog.LogMessage("++ handleGoogleLoginResult fail..");

                        if (mGoogleApiClient != null)
                        {
                            mGoogleApiClient.disconnect();
                        }

                        if (mSessionLoginListener != null)
                        {
                            mSessionLoginListener.onError();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void handleActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == GOOGLE_ACCOUNT_REQUEST)
        {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (googleSignInResult.isSuccess())
            {
                EasyLog.LogMessage("++ onHandleGoogleLoginResult isSuccess");

                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

                if (mSessionLoginListener != null)
                {
                    mSessionLoginListener.onLogin(googleSignInAccount);
                }
            }
            else
            {
                EasyLog.LogMessage("++ onHandleGoogleLoginResult fail..");

                if (mGoogleApiClient != null)
                {
                    mGoogleApiClient.disconnect();
                }

                if (mSessionLoginListener != null)
                {
                    mSessionLoginListener.onError();
                }
            }
        }
    }
}
