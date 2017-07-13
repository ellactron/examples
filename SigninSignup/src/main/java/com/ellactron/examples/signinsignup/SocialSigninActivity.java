package com.ellactron.examples.signinsignup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ellactron.examples.signinsignup.facebook.FacebookSignIn;

import java.util.concurrent.Callable;

public class SocialSigninActivity extends AppCompatActivity {
    static FacebookSignIn fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化认证 SDK 并检查登录状态
        initialOAuth2Sdk();

        if(isUserLoggedIn()) {
            showMainWindow();
        }
        else {
            // 初始化OAuth2认证界面，这必须在 OAuth2 认证 SDK 初始化和回调管理器生成之后执行。
            setContentView(R.layout.activity_social_oauth2);

            // 注册登录按钮
            registerLogInButon();
        }
    }

    // 添加 Facebook login 回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(null != fb)
            fb.onActivityResult(requestCode, resultCode, data);
    }

    private void initialOAuth2Sdk() {
        if(null == fb) {
            fb = new FacebookSignIn();
        }
        fb.initialFacebookSdk(this);
    }

    private boolean isUserLoggedIn() {
        return (null == fb)?false:(null!=fb.getFacebookProfile());
    }

    private void registerLogInButon() {
        // 注册登录按钮
        if (null != fb) {
            fb.registerSignInButton(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    showMainWindow();
                    return null;
                }
            });
        }
    }

    public void showMainWindow(){
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}

