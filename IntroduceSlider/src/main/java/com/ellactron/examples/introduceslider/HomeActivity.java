package com.ellactron.examples.introduceslider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Reset "first time launch" flag for next time test
        onDestroy();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        prefManager = new PrefManager(this);
        prefManager.editor.clear();
        prefManager.editor.commit();
        super.onDestroy();
    }
}
