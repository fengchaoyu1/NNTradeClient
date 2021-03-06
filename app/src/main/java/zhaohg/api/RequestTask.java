package zhaohg.api;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import zhaohg.json.JsonValue;

public class RequestTask extends AsyncTask<Void, Integer, JsonValue> {

    private RequestParam param;
    private PostEvent postEvent;

    public void setRequestParam(RequestParam param) {
        this.param = param;
    }

    public void setRequestPostEvent(PostEvent event) {
        this.postEvent = event;
    }

    private HttpGet getHttpGet() {
        return new HttpGet(this.param.getEncodedUrl());
    }

    private HttpPost getHttpPost() {
        HttpPost httpPost = new HttpPost(this.param.getUrl());
        List<NameValuePair> values = this.param.getNameValuePairs();
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(values));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return httpPost;
    }

    private HttpPut getHttpPut() {
        HttpPut httpPut = new HttpPut(this.param.getUrl());
        try {
            httpPut.setEntity(new StringEntity(this.param.getEncodedAst()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httpPut;
    }

    private HttpDeleteWithBody getHttpDelete() {
        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(this.param.getUrl());
        try {
            httpDelete.setEntity(new StringEntity(this.param.getEncodedAst()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httpDelete;
    }

    private HttpPost getHttpFile() {
        HttpPost httpPost = new HttpPost(this.param.getUrl());
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        List<NameValuePair> pairs = this.param.getNameValuePairs();
        for (NameValuePair pair : pairs) {
            builder.addTextBody(pair.getName(), pair.getValue());
        }
        Map<String, File> files = this.param.getFiles();
        for (Map.Entry<String, File> entry : files.entrySet()) {
            builder.addBinaryBody(entry.getKey(), entry.getValue());
        }
        httpPost.setEntity(builder.build());
        return httpPost;
    }

    private HttpRequestBase getHttpRequest() {
        switch (this.param.getMethod()) {
            case RequestParam.METHOD_GET:
                return this.getHttpGet();
            case RequestParam.METHOD_POST:
                return this.getHttpPost();
            case RequestParam.METHOD_PUT:
                return this.getHttpPut();
            case RequestParam.METHOD_DELETE:
                return this.getHttpDelete();
            case RequestParam.METHOD_FILE:
                return this.getHttpFile();
        }
        return null;
    }

    @Override
    protected JsonValue doInBackground(Void... params) {
        JsonValue json = null;
        HttpRequestBase httpRequest = this.getHttpRequest();
        if (httpRequest != null) {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, this.param.getConnectionTimeout());
            HttpConnectionParams.setSoTimeout(httpParams, this.param.getSocketTimeout());
            HttpClient httpClient = new DefaultHttpClient(httpParams);
            try {
                HttpResponse httpResponse = httpClient.execute(httpRequest);
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    json = new JsonValue();
                    if (!json.tryParse(EntityUtils.toString(httpEntity))) {
                        json = null;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpClient.getConnectionManager().shutdown();
        }
        return json;
    }

    protected void onPostExecute(JsonValue result) {
        if (this.postEvent != null && !this.isCancelled()) {
            this.postEvent.onPostEvent(result);
        }
    }

}
