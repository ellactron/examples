package com.ellactron.examples.restclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ellactron.examples.restclient.volley.VolleyJSONObjectRequest;
import com.ellactron.examples.restclient.volley.VolleyRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.AbsListView.CHOICE_MODE_SINGLE;

public class MainActivity extends AppCompatActivity {
    private RequestQueue mQueue;
    int restMethod;

    private void initMethodMenu() {
        String[] values = new String[] { "GET", "PUT", "POST", "DELETE" };
        ListView methodList = (ListView)findViewById(R.id.methodList);
        methodList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values));
        methodList.setChoiceMode(CHOICE_MODE_SINGLE);
        methodList.setFocusable(true);
        methodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                restMethod = getMethod((String) adapter.getItemAtPosition(position));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initMethodMenu();

        // Initial request queue
        getRequestQueue();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getUrl();
                String queryString = getQueryString();

                Snackbar.make(view, "Sending rest request to " + url, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                mQueue.add(getVolleyRequest(url + "/" + queryString));
            }
        });
    }

    protected int getMethod(String methodName) {
        switch(methodName){
            case "GET":
                return Request.Method.GET;
            case "PUT":
                return Request.Method.PUT;
            case "POST":
                return Request.Method.POST;
            case "DELETE":
                return Request.Method.DELETE;
            default:
                return Request.Method.GET;
        }
    }

    protected VolleyJSONObjectRequest getVolleyRequest(String url) {
        VolleyLog.DEBUG = true;

        final VolleyJSONObjectRequest jsonRequest = new VolleyJSONObjectRequest(
                restMethod,
                url,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        outputResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        outputError(error);
                    }
                });

        /*
          > C:\Downloads\devapps\android\sdk\android-sdk-windows\platform-tools\adb shell
            $ setprop log.tag.REQUEST_TAG VERBOSE
            $ logcat
            or
            $ logcat *:V
         */
        jsonRequest.setTag("REQUEST_TAG");

        return jsonRequest;
    }

    protected String getUrl() {
        EditText urlEditBox = (EditText) findViewById(R.id.inputEditBox);
        return urlEditBox.getEditableText().toString();
    }

    protected String getQueryString() {
        EditText editBox = (EditText) findViewById(R.id.queryEditText);
        return editBox.getEditableText().toString();
    }

    protected void outputResponse(Object response) {
        try {
            TextView resultTextView = (TextView) findViewById(R.id.resultTextView);
            resultTextView.setText(((JSONObject) response).toString());
        } catch (Exception e) {
            Log.d(this.getClass().getName(), e.getMessage());
        }
    }

    protected void outputError(VolleyError error) {
        TextView resultTextView = (TextView) findViewById(R.id.resultTextView);
        if(null == error.networkResponse)
        {
            resultTextView.setText("Error message: " + error.getMessage());
        }
        else {
            resultTextView.setText("Error code: " + error.networkResponse.statusCode
                    + "\nError message: " + error.getMessage());
        }
    }

    protected void getRequestQueue() {
        mQueue = VolleyRequestQueue.getInstance(this).getRequestQueue();
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
