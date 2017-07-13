package com.ellactron.examples.interceptwebview;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.GridLayout;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
    WebView mWebView = null;

    final String hostAddress = "://192.168.10.51:8080";
    final String localResource = "/local/";
    String token = "xxx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addWebView(createWebView());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText urlEditText = (EditText) findViewById(R.id.urlEditText);
                String url = urlEditText.getText().toString();

                if(null != url) {
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    mWebView.loadUrl(url);
                }
            }
        });

    }

    /**
     <WebView
     android:layout_width="match_parent"
     android:layout_height="450dp"
     android:layout_column="0"
     android:layout_row="2"
     android:layout_weight="1"/>
     */

    private void addWebView(WebView mWebView) {
        GridLayout baseLayout = (GridLayout) findViewById(R.id.baseLayout);
        GridLayout.Spec row = GridLayout.spec(1);
        GridLayout.Spec colspan = GridLayout.spec(0);
        GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, colspan);
        baseLayout.addView(mWebView, gridLayoutParam);
    }

    private WebView createWebView() {
        mWebView = new WebView(this);
        mWebView.setWebViewClient(new WebViewClient(){
            /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest req) {
                if(req.getUrl().toString().contains(hostAddress)){
                    req.getRequestHeaders().put("Authorization", "Bearer "+token);
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, req);
            }*/

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest req) {
                if(req.getUrl().toString().contains(hostAddress)){
                    if(req.getUrl().toString().contains(hostAddress + localResource)){
                        return localRequest(req);
                    }
                    else {
                        return interceptRequest(req);
                    }
                }

                return super.shouldInterceptRequest(view, req);
            }
        });

        return mWebView;
    }

    public int getResId(String resName, Class<?> c) {
        try {
            int id = this.getResources().getIdentifier(resName,
                    "drawable",
                    this.getPackageName());
            if (id == 0 && resName.contains(".")){
                id = this.getResources().getIdentifier(resName.substring(0,
                        resName.indexOf(".")),
                        "drawable",
                        this.getPackageName());
            }
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private WebResourceResponse localRequest(WebResourceRequest req) {
        String path = req.getUrl().toString();
        path = path.substring(path.indexOf(localResource)+localResource.length());
        int resourceId = getResId(path, Drawable.class);
        if(0 == resourceId)
            return null;

        InputStream raw = getResources().openRawResource(resourceId);
        return new WebResourceResponse("image/png", "UTF-8", raw);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private WebResourceResponse interceptRequest(WebResourceRequest req) {
        HttpResponse httpReponse = null;

        try {
            DefaultHttpClient client = new DefaultHttpClient();
            switch(req.getMethod().toUpperCase()){
                case "GET":
                    HttpGet httpGet = new HttpGet(req.getUrl().toString());
                    httpGet.setHeader("Authorization", "Bearer "+token);
                    httpReponse = client.execute(httpGet);
                case "PUT":
                    HttpPut httpPut = new HttpPut(req.getUrl().toString());
                    httpPut.setHeader("Authorization", "Bearer "+token);
                    httpReponse = client.execute(httpPut);
                case "POST":
                    HttpPost httpPost = new HttpPost(req.getUrl().toString());
                    httpPost.setHeader("Authorization", "Bearer "+token);
                    httpReponse = client.execute(httpPost);
                case "DELETE":
                    HttpDelete httpDelete = new HttpDelete(req.getUrl().toString());
                    httpDelete.setHeader("Authorization", "Bearer "+token);
                    httpReponse = client.execute(httpDelete);
            }

            Header contentType = httpReponse.getEntity().getContentType();
            Header encoding = httpReponse.getEntity().getContentEncoding();
            InputStream responseInputStream = httpReponse.getEntity().getContent();

            String contentTypeValue = null;
            String encodingValue = null;
            if (contentType != null) {
                contentTypeValue = contentType.getValue();
            }
            if (encoding != null) {
                encodingValue = encoding.getValue();
            }

            return new WebResourceResponse(contentTypeValue, encodingValue, responseInputStream);
        }
        catch(Exception e){
            Log.e(this.getClass().getName(), e.getMessage());
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
