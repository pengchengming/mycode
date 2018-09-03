package com.bizduo.zflow.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {	
	/**
	 * post请求方式
	 * @param url
	 * @param paramMap
	 * @return
	 */
	public  static String postRequest(String url,Map<String,String> paramMap){
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(paramMap.size());
		if(paramMap!=null){
	        for(Map.Entry<String, String> entry : paramMap.entrySet()){
	            NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue()));  
	            valuePairs.add(nameValuePair);  
	        }
		}
      // 构造HTTP请求
    	final HttpPost postRequest = new HttpPost(url);
        HttpEntity resultEntity = null;
        try {
            final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs, "UTF-8");
            postRequest.setEntity(entity);
            // 执行请求并获取返回值
            final HttpClient httpClient = new DefaultHttpClient();
            final HttpResponse response = httpClient.execute(postRequest);
            resultEntity = response.getEntity();
            final String result = EntityUtils.toString(resultEntity, "UTF-8");
            System.out.println(result);
            return result;
        } catch (final ClientProtocolException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            postRequest.abort();
            try {
                EntityUtils.consume(resultEntity);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return "";
	}

	public static String httpGet(String url) {  
        try {  
            HttpGet httpGet = new HttpGet(url);  
            HttpClient client = new DefaultHttpClient();  
            HttpResponse resp = client.execute(httpGet);  
              
            HttpEntity entity = resp.getEntity();  
            String respContent = EntityUtils.toString(entity , "GBK").trim();  
            httpGet.abort();  
            client.getConnectionManager().shutdown();  
  
            return respContent;  
              
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
	
	public static String httpPost(String url, Map<String, String> params) {
	try {  
        HttpPost httpPost = new HttpPost(url);  
        HttpClient client = new DefaultHttpClient();  
        List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(params.size());  
        for(Map.Entry<String, String> entry : params.entrySet()){  
            NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue()));  
            valuePairs.add(nameValuePair);  
        }  
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(valuePairs, "GBK");  
        httpPost.setEntity(formEntity);  
        HttpResponse resp = client.execute(httpPost);  
          
        HttpEntity entity = resp.getEntity();  
        String respContent = EntityUtils.toString(entity , "GBK").trim();  
        httpPost.abort();  
        client.getConnectionManager().shutdown();  
  
        return respContent;  
          
    } catch (Exception e) {  
        e.printStackTrace();  
        return null;  
    }  
}  

}
