package com.ellactron.examples.signinsignup.facebook;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ellactron.examples.signinsignup.HomeActivity;
import com.ellactron.examples.signinsignup.SocialSigninActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.concurrent.Callable;

/**
 * Created by ji.wang on 2017-05-26.
 */

public class FacebookSignIn {
    CallbackManager mFacebookCallbackManager;
    FragmentActivity activity;

    public FacebookSignIn(FragmentActivity activity) {
        this.activity = activity;
        initialFacebookSdk();
    }

    public boolean facebookInitiated(){
        if(null != activity) {
            // 1) 初始化认证SDK
            FacebookSdk.sdkInitialize(activity.getApplicationContext());

            // 2) 检查登录状态
            Profile profile = Profile.getCurrentProfile().getCurrentProfile();
            if(null != profile){
                //activity.startActivity(new Intent(activity, HomeActivity.class));
                //activity.finish();
                return true;
            }
        }

        return false;
    }

    private boolean initialFacebookSdk() {
        // 1) 初始化认证SDK
        FacebookSdk.sdkInitialize(activity.getApplicationContext());

        // 2) 检查登录状态
        Profile profile = Profile.getCurrentProfile().getCurrentProfile();
        if(null != profile){
            //activity.startActivity(new Intent(activity, HomeActivity.class));
            //activity.finish();
            return true;
        }

        // 3) 如果未登录，建立回调管理器
        mFacebookCallbackManager = CallbackManager.Factory.create();

        // 返回为登录状态
        return false;
    }

    public void registerSignInButton(LoginButton mFacebookSignInButton, final Class intentActivity) {
        mFacebookSignInButton.registerCallback(mFacebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        // Use the Profile class to get information about the current user.
                        handleSignInResult(new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                LoginManager.getInstance().logOut();
                                return null;
                            }
                        }, intentActivity);
                    }

                    @Override
                    public void onCancel() {
                        handleSignInResult(null, intentActivity);
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(SocialSigninActivity.class.getCanonicalName(), error.getMessage());
                        handleSignInResult(null, intentActivity);
                    }
                }
        );
    }

    protected void handleSignInResult(Callable logoutCallable, Class intentActivity) {
        if(null != logoutCallable) {
            activity.startActivity(new Intent(activity, intentActivity));
            activity.finish();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
