package com.ellactron.examples.signinsignup.facebook;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ellactron.examples.signinsignup.R;
import com.ellactron.examples.signinsignup.SocialSigninActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.concurrent.Callable;

/**
 * Created by ji.wang on 2017-05-26.
 */

public class FacebookSignIn {
    CallbackManager mFacebookCallbackManager;
    FragmentActivity activity;

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

    public void registerSignInButton(final Callable<Void> onSuccessCallback) {
        // 设置认证按钮，这必须在 OAuth2 认证界面初始化完成之后
        LoginButton mFacebookSignInButton = (LoginButton)activity.findViewById(R.id.facebook_sign_in_button);

        mFacebookSignInButton.registerCallback(mFacebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        // TODO: Use the Profile class to get information about the current user.
                        //

                        try {
                            onSuccessCallback.call();
                        } catch (Exception e) {
                            Log.d(this.getClass().getCanonicalName(), e.getMessage());
                        }
                    }

                    @Override
                    public void onCancel() {
                        Log.d(SocialSigninActivity.class.getCanonicalName(), "Action cancelled");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(SocialSigninActivity.class.getCanonicalName(), error.getMessage());
                    }
                }
        );
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
