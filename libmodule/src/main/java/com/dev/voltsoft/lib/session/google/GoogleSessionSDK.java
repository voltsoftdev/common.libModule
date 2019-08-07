package com.dev.voltsoft.lib.session.google;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.dev.voltsoft.lib.R;
import com.dev.voltsoft.lib.session.*;
import com.dev.voltsoft.lib.utility.EasyLog;
import com.dev.voltsoft.lib.view.ActivityStackCallBackListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

import static com.facebook.GraphRequest.TAG;

public class GoogleSessionSDK implements ISessionSDK
{

    public static final int GOOGLE_ACCOUNT_REQUEST = 9001;

    private GoogleApiClient mGoogleApiClient;

    private ISessionLoginListener<GoogleSignInAccount> mLoginListener;

    private static class LazyHolder
    {
        private static GoogleSessionSDK mInstance = new GoogleSessionSDK();
    }

    public static GoogleSessionSDK getInstance()
    {
        return GoogleSessionSDK.LazyHolder.mInstance;
    }

    @Override
    public void logout(final SessionLogout sessionLogout)
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

                        if (sessionLogout.getSessionLogoutListener() != null)
                        {
                            sessionLogout.getSessionLogoutListener().onLogout();
                        }
                    }
                });
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

            if (sessionLogout.getSessionLogoutListener() != null)
            {
                sessionLogout.getSessionLogoutListener().onError();
            }
        }
    }

    @Override
    public void login(SessionLogin sessionLogin)
    {
        final GoogleSessionLogin googleSessionLogin = (GoogleSessionLogin) sessionLogin;

        final AppCompatActivity appCompatActivity = googleSessionLogin.getAppCompatActivity();

        mLoginListener = googleSessionLogin.getSessionLoginListener();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(googleSessionLogin.Token)
                .requestEmail()
                .build();

        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.stopAutoManage(appCompatActivity);
            mGoogleApiClient.disconnect();
        }

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

                            if (mLoginListener != null)
                            {
                                mLoginListener.onError();
                            }
                        }
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API , googleSignInOptions)
                .build();
        mGoogleApiClient.connect();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        appCompatActivity.startActivityForResult(signInIntent, GOOGLE_ACCOUNT_REQUEST);
    }

    @Override
    public void handleActivityResult(AppCompatActivity activity, int requestCode, int resultCode, Intent data)
    {
        if (requestCode == GOOGLE_ACCOUNT_REQUEST)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try
            {
                final GoogleSignInAccount account = task.getResult(ApiException.class);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            if (mLoginListener != null)
                            {
                                mLoginListener.onLogin(account);
                            }
                        }
                        else
                        {
                            if (mLoginListener != null)
                            {
                                mLoginListener.onError();
                            }
                        }
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
