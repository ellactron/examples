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

    public FacebookSignIn(/*FragmentActivity activity*/) {
        //initialFacebookSdk(activity);
    }

    public Profile getFacebookProfile(){
        // 1) 初始化认证SDK
        if(!FacebookSdk.isInitialized())
            FacebookSdk.sdkInitialize(activity.getApplicationContext());

        // 2) 检查登录状态
        Profile profile = Profile.getCurrentProfile().getCurrentProfile();

        return profile;
    }

    public void initialFacebookSdk(FragmentActivity activity) {
        this.activity = activity;

        // 1) 初始化认证SDK
        if(!FacebookSdk.isInitialized())
            FacebookSdk.sdkInitialize(activity.getApplicationContext());

        // 2) 检查登录状态
        Profile profile = Profile.getCurrentProfile().getCurrentProfile();

        // 3) 如果未登录，建立回调管理器
        if(null == profile){
            mFacebookCallbackManager = CallbackManager.Factory.create();
        }
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
            ((SocialSigninActivity)activity).showMainWindow();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
