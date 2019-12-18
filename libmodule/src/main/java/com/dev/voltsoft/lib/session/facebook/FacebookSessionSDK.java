package com.dev.voltsoft.lib.session.facebook;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.dev.voltsoft.lib.session.*;
import com.dev.voltsoft.lib.utility.EasyLog;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import org.json.JSONObject;

import java.util.Arrays;

public class FacebookSessionSDK implements ISessionSDK
{

    private CallbackManager mCallbackManager;

    private static class LazyHolder
    {
        private static FacebookSessionSDK mInstance = new FacebookSessionSDK();
    }

    public static FacebookSessionSDK getInstance()
    {
        return LazyHolder.mInstance;
    }

    private FacebookSessionSDK()
    {
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void logout(SessionLogout sessionLogout)
    {
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {

            }

            @Override
            public void onCancel()
            {

            }

            @Override
            public void onError(FacebookException error)
            {

            }
        });

        LoginManager.getInstance().logOut();
    }

    @Override
    public void login(SessionLogin sessionLogin)
    {
        final FaceBookSessionLogin faceBookSessionLogin = (FaceBookSessionLogin) sessionLogin;

        if (faceBookSessionLogin.ProgressView != null)
        {
            faceBookSessionLogin.ProgressView.onLoading();
        }

        final ISessionLoginListener<GraphResponse> loginListener = faceBookSessionLogin.getSessionLoginListener();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                EasyLog.LogMessage(">> FacebookSessionSDK onSuccess");

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        if (faceBookSessionLogin.ProgressView != null)
                        {
                            faceBookSessionLogin.ProgressView.onLoadingEnd();
                        }

                        if (loginListener != null)
                        {
                            loginListener.onLogin(response);
                        }
                    }
                });

                Bundle bundle = new Bundle();
                bundle.putString("fields", "id,name,email,gender,birthday,picture.type(large)");

                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel()
            {
                if (faceBookSessionLogin.ProgressView != null)
                {
                    faceBookSessionLogin.ProgressView.onLoadingEnd();
                }

                EasyLog.LogMessage(">> FacebookSessionSDK onCancel");

                if (loginListener != null)
                {
                    loginListener.onError();
                }
            }

            @Override
            public void onError(FacebookException error)
            {
                if (faceBookSessionLogin.ProgressView != null)
                {
                    faceBookSessionLogin.ProgressView.onLoadingEnd();
                }

                EasyLog.LogMessage(">> FacebookSessionSDK onError ");
                EasyLog.LogMessage(">> FacebookSessionSDK onError " + error.getMessage());
                EasyLog.LogMessage(">> FacebookSessionSDK onError " + error.getLocalizedMessage());

                if (loginListener != null)
                {
                    loginListener.onError();
                }
            }
        });

        LoginManager.getInstance().logInWithReadPermissions(faceBookSessionLogin.getAppCompatActivity() , Arrays.asList("email"));
    }

    @Override
    public void handleActivityResult(AppCompatActivity activity, int requestCode, int resultCode, Intent data)
    {
        if (mCallbackManager != null)
        {
            mCallbackManager.onActivityResult(requestCode , resultCode , data);
        }
    }
}
