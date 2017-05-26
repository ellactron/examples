package com.ellactron.examples.signinsignup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ellactron.examples.signinsignup.facebook.FacebookSignIn;
import com.facebook.login.widget.LoginButton;

public class SocialSigninActivity extends AppCompatActivity {
    FacebookSignIn fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化认证SDK 并检查登录状态
        if(initialOAuth2Sdk()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        // 初始化OAuth2认证界面，这必须在 OAuth2 认证 SDK 初始化和回调管理器生成之后执行。
        setContentView(R.layout.activity_social_oauth2);

        // 设置认证按钮，这必须在 OAuth2 认证界面初始化完成之后
        LoginButton mFacebookSignInButton = (LoginButton)findViewById(R.id.facebook_sign_in_button);
        if(null != fb) {
            fb.registerSignInButton(mFacebookSignInButton, HomeActivity.class);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(null != fb)
            fb.onActivityResult(requestCode, resultCode, data);
    }

    private boolean initialOAuth2Sdk() {
        if(initialFacebookSdk())
            return true;

        return false;
    }

    private boolean initialFacebookSdk() {
        if(null == fb) {
            fb = new FacebookSignIn(this);
        }
        return fb.facebookInitiated();
    }
}
