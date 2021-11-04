package com.cjack.meetingroomadmin.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by hello on 18/07/17.
 */
public class HttpClientUtil {

    public static String doPost( String url, String json)throws Exception{
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try{
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(url);
            //设置参数
            StringEntity s = new StringEntity(json,"utf-8");
            s.setContentEncoding("utf-8");
            s.setContentType("application/json");
            httpPost.setEntity(s);
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null && response.getStatusLine().getStatusCode()==200){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity, "utf-8");
                }
            }
        }catch(Exception e){
            throw new Exception( e);
        }
        return result;
    }

    public static String httpGet( String uri)throws Exception{
        String result= "";
        HttpGet httpRequst = new HttpGet(uri);
        try {
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequst);//其中HttpGet是HttpUriRequst的子类
            if(httpResponse.getStatusLine().getStatusCode() == 200)
            {
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);//取出应答字符串
                // 一般来说都要删除多余的字符
                result.replaceAll("\r", "");//去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格
            } else {
                httpRequst.abort();
            }
        } catch (ClientProtocolException e) {
            throw new Exception( e);
        } catch (IOException e) {
            throw new Exception( e);
        }
        return result;
    }

}
