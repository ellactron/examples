package com.ellactron.examples.restclient.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ji.wang on 2017-06-08.
 */

public class VolleyJSONObjectRequest extends JsonObjectRequest {
    Map<String, String> headers = new HashMap<String, String>();

    public VolleyJSONObjectRequest(int method,
                                   String url,
                                   Map<String, String> headers,
                                   JSONObject jsonRequest,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);

        this.headers.put("Accept", "application/json; charset=utf-8");
        if(null != headers)
            this.headers.putAll(headers);

        VolleyLog.d("Adding request: %s", url);
    }

    public VolleyJSONObjectRequest(int method,
                                   String url,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {
        this(method, url, null, null, listener, errorListener);
    }

    public VolleyJSONObjectRequest(String url,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {
        this(Request.Method.GET, url, null, null, listener, errorListener);
    }

    public VolleyJSONObjectRequest(String url,
                                   JSONObject jsonRequest,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {
        this(Request.Method.POST, url, null, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        headers.putAll(super.getHeaders());
        return headers;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        // here you can write a custom retry policy
        return super.getRetryPolicy();
    }
}
