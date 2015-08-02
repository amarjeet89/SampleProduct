/**
 * BaseSync class is  service class to make get and post
 * request to the server.
 * <p/>
 * Copyright 2014 � Brillio
 * All rights reserved
 *
 * @version 1.0
 * @author Brillio
 */

package com.prafull.product.services;

import android.os.AsyncTask;

import com.prafull.product.util.CommonUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class BaseSync extends AsyncTask<String, Void, String> {
    private String mUrl = null;// post data
    JSONObject mJsonobj;
    private int timeout = 1000;
    private boolean isGet = false;
    private String methodType="POST";

    public interface OnTaskCompleted {
        void onTaskCompleted(String str);

        void onTaskFailure(String str);
    }

    private OnTaskCompleted listener;

    public BaseSync(OnTaskCompleted listener, String url, JSONObject obj, String methodType) {
        this.listener = listener;
        mUrl = url;
        this.mJsonobj = obj;
        this.methodType = methodType;
    }

    public BaseSync(OnTaskCompleted listener, String url, JSONObject obj) {
        this.listener = listener;
        mUrl = url;
        this.mJsonobj = obj;
    }

    @Override
    protected String doInBackground(String... params) {
        String output = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpResponse httpresponse = null;
        try {

            if (methodType== CommonUtil.HTTP_POST) {
                StringEntity se = new StringEntity(mJsonobj.toString());
                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                HttpPost httpPostReq = new HttpPost(mUrl);
                httpPostReq.setEntity(se);
                httpresponse = httpclient.execute(httpPostReq);
            }
            if(methodType==CommonUtil.HTTP_PUT) {
                StringEntity se = new StringEntity(mJsonobj.toString());
                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                HttpPut httpPut = new HttpPut(mUrl);
                httpPut.setEntity(se);
                httpresponse = httpclient.execute(httpPut);
            }
            if (methodType==CommonUtil.HTTP_GET){
                HttpGet getRequest = new HttpGet(mUrl);
                httpresponse = httpclient.execute(getRequest);
            }
            HttpEntity entity = httpresponse.getEntity();
            InputStream is = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                 sb.append(line);
            }
            is.close();
            output = sb.toString();
        } catch (UnsupportedEncodingException e1) {
            listener.onTaskFailure(e1.getMessage());
        } catch (ClientProtocolException e1) {
            listener.onTaskFailure(e1.getMessage());
        } catch (IOException e1) {
            listener.onTaskFailure(e1.getMessage());
        } catch (Exception e) {
            listener.onTaskFailure(e.getMessage());
        }
        return output;
    }

    @Override
    protected void onPostExecute(String output) {
        if (listener != null && output!=null) {
            listener.onTaskCompleted(output);
       }

    }
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

}