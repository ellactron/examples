package com.ellactron.examples.signinsignup;

import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获得 FaceBook 登录信息
                Profile profile = Profile.getCurrentProfile();
                AccessToken accessToken = AccessToken.getCurrentAccessToken();

                Snackbar.make(view, profile.getFirstName() + " is logging out...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // 模拟登出，这样下次再次进入程序时就会出现登录界面。
                LoginManager.getInstance().logOut();
            }
        });
    }
}
