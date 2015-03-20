package zhaohg.api;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RequestParam {

    public static final int METHOD_GET = 0;
    public static final int METHOD_POST = 1;
    public static final int METHOD_PUT = 2;
    public static final int METHOD_DELETE = 3;

    private Map<String, String> params;

    private String url = "";
    private int method = METHOD_GET;
    private int connectionTimeout = 10000;
    private int socketTimeout = 30000;

    private String token = "";

    public RequestParam() {
        this.params = new TreeMap<>();
    }

    public void setUrl(String value) {
        this.url = value;
    }

    public String getUrl() {
        return this.url;
    }

    public void setMethod(int value) {
        this.method = value;
    }

    public int getMethod() {
        return this.method;
    }

    public void setConnectionTimeout(int value) {
        this.connectionTimeout = value;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setSocketTimeout(int value) {
        this.socketTimeout = value;
    }

    public int getSocketTimeout() {
        return this.socketTimeout;
    }

    public void addParam(String key, String value) {
        this.params.put(key, value);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void initSecretToken() {
        if (this.token != "") {
            this.params.put("token", this.token);
            String text = "";
            boolean first = true;
            for (Map.Entry<String, String> entry : this.params.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    text += "&";
                }
                text += entry.getKey() + "=" + entry.getValue();
            }
            this.params.put("token", Encryption.md5(text));
        }
    }

    public String getEncodedUrl() {
        this.initSecretToken();
        String url = this.url;
        if (!url.endsWith("?")) {
            url += "?";
        }
        List<NameValuePair> values = this.getNameValuePairs();
        url += URLEncodedUtils.format(values, "utf-8");
        return url;
    }

    public List<NameValuePair> getNameValuePairs() {
        this.initSecretToken();
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return nameValuePairs;
    }

}
