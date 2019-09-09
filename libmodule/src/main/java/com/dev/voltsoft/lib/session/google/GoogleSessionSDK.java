package com.dev.voltsoft.lib.session.google;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.dev.voltsoft.lib.R;
import com.dev.voltsoft.lib.session.*;
import com.dev.voltsoft.lib.utility.EasyLog;
import com.dev.voltsoft.lib.view.ActivityStackCallBackListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

import static com.facebook.GraphRequest.TAG;

public class GoogleSessionSDK implements ISessionSDK, GoogleApiClient.OnConnectionFailedListener {

    public static final int GOOGLE_ACCOUNT_REQUEST = 9001;

    private GoogleSignInOptions mGoogleSignInOptions;

    private GoogleApiClient     mGoogleApiClient;

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
            FirebaseAuth.getInstance().signOut();
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

        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(appCompatActivity)
                .enableAutoManage(appCompatActivity , this)
                .addApi(Auth.GOOGLE_SIGN_IN_API , mGoogleSignInOptions)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        appCompatActivity.startActivityForResult(signInIntent, GOOGLE_ACCOUNT_REQUEST);

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        if (mLoginListener != null)
        {
            mLoginListener.onError();
        }
    }

    @Override
    public void handleActivityResult(AppCompatActivity activity, int requestCode, int resultCode, Intent data)
    {
        if (requestCode == GOOGLE_ACCOUNT_REQUEST)
        {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try
            {

                if (googleSignInResult.isSuccess())
                {
                    GoogleSignInAccount account = googleSignInResult.getSignInAccount();

                    if (account != null)
                    {
                        if (mLoginListener != null)
                        {
                            mLoginListener.onLogin(account);
                        }
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
            catch (Exception e)
            {
                e.printStackTrace();

                if (mLoginListener != null)
                {
                    mLoginListener.onError();
                }
            }
        }
    }
}
