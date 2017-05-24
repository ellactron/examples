package com.ellactron.examples.signinsignup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.concurrent.Callable;

public class SocialSigninActivity extends AppCompatActivity {
    CallbackManager mFacebookCallbackManager;

    protected void handleSignInResult(Callable logoutCallable) {
        if(null != logoutCallable) {
            startActivity(new Intent(SocialSigninActivity.this, HomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化认证SDK 并检查登录状态
        if(initialOAuth2Sdk())
            return;

        // 初始化OAuth2认证界面，这必须在 OAuth2 认证 SDK 初始化和回调管理器生成之后执行。
        setContentView(R.layout.activity_social_oauth2);

        // 设置认证按钮，这必须在 OAuth2 认证界面初始化完成之后
        LoginButton mFacebookSignInButton = (LoginButton)findViewById(R.id.facebook_sign_in_button);
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
                        });
                    }

                    @Override
                    public void onCancel() {
                        handleSignInResult(null);
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(SocialSigninActivity.class.getCanonicalName(), error.getMessage());
                        handleSignInResult(null);
                    }
                }
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private boolean initialOAuth2Sdk() {
        if(initialFacebookSdk())
            return true;

        return false;
    }

    private boolean initialFacebookSdk() {
        // 1) 初始化认证SDK
        FacebookSdk.sdkInitialize(getApplicationContext());

        // 2) 检查登录状态
        Profile profile = Profile.getCurrentProfile().getCurrentProfile();
        if(null != profile){
            startActivity(new Intent(SocialSigninActivity.this, HomeActivity.class));
            finish();
            return true;
        }

        // 3) 如果未登录，建立回调管理器
        mFacebookCallbackManager = CallbackManager.Factory.create();

        // 返回为登录状态
        return false;
    }
}
