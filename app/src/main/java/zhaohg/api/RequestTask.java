package zhaohg.api;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import zhaohg.json.JsonValue;

public class RequestTask extends AsyncTask<Void, Integer, JsonValue> {

    private RequestParam param;
    private RequestPostEvent postEvent;

    public RequestTask(RequestParam param) {
        super();
        this.param = param;
    }

    public void setRequestStatusListener(RequestPostEvent event) {
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
        return new HttpPut(this.param.getEncodedUrl());
    }

    private HttpDelete getHttpDelete() {
        return new HttpDelete(this.param.getEncodedUrl());
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
        }
        return null;
    }

    @Override
    protected JsonValue doInBackground(Void... params) {
        JsonValue json = null;
        HttpRequestBase httpRequest = null;

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
        if (this.postEvent != null) {
            this.postEvent.onPostEvent(result);
        }
    }

}
