package zhaohg.test;

import android.test.InstrumentationTestCase;

import zhaohg.api.RequestParam;

public class TestRequestParam extends InstrumentationTestCase {

    @Override
    protected void setUp() {
        try {
            super.setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testUrlEmpty() {
        String url = "http://www.zhaohg.com/";
        RequestParam param = new RequestParam();
        param.setUrl(url);
        assertEquals(url, param.getUrl());
        assertEquals(url + "?", param.getEncodedUrl());
    }

    public void testUrlParams() {
        RequestParam param = new RequestParam();
        param.setUrl("http://www.zhaohg.com/");
        param.addParam("key1", "value1");
        param.addParam("key2", "value2");
        assertEquals("http://www.zhaohg.com/?key1=value1&key2=value2", param.getEncodedUrl());
    }

    public void testTimeoutValue() {
        RequestParam param = new RequestParam();
        param.setConnectionTimeout(100);
        param.setSocketTimeout(200);
        assertEquals(100, param.getConnectionTimeout());
        assertEquals(200, param.getSocketTimeout());
    }

    public void testMethodValue() {
        RequestParam param = new RequestParam();
        param.setMethod(RequestParam.METHOD_GET);
        assertEquals(RequestParam.METHOD_GET, param.getMethod());
        param.setMethod(RequestParam.METHOD_POST);
        assertEquals(RequestParam.METHOD_POST, param.getMethod());
        param.setMethod(RequestParam.METHOD_PUT);
        assertEquals(RequestParam.METHOD_PUT, param.getMethod());
        param.setMethod(RequestParam.METHOD_DELETE);
        assertEquals(RequestParam.METHOD_DELETE, param.getMethod());
    }

    public void testToken() {
        RequestParam param = new RequestParam();
        param.setToken("16c2bda807b7b152658458a5e3420181");
        assertEquals("16c2bda807b7b152658458a5e3420181", param.getToken());
        param.addParam("username", "login_name");
        param.getEncodedUrl();
        assertEquals("?token=c43fd9239c368e4cbd042019bab7b163&username=login_name", param.getEncodedUrl());
        param.setToken("3c6a77829e158694471b71c2f018a7a2");
        param.addParam("username", "login_name");
        param.getEncodedUrl();
        assertEquals("?token=0577eb2ff2b9dc2dda6661fc107de6fe&username=login_name", param.getEncodedUrl());
    }

}