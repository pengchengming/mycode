package com.bizduo.zflow.util.deco;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.bizduo.zflow.domain.sys.User;

public class DecoUtil {
	public final static String appKey = "QysBKj9tlE";
	public final static String  appSecret = "EwkgulgNa3KigRmR74MITI82syGtCdrU";
	public final static String avatar = "http://qhyxpic.oss.kujiale.com/avatars/72.jpg";
	public final static String apiurl = "http://www.kujiale.com";
	
	 
	public  static String loginkujiale(User createUser,String url,String type,String  apputype, String designid,String planid){
        
          String userName =createUser.getUsername();
          String email =createUser.getEmail();
          String phone =createUser.getTel();
          String ssn = "";  //用户身份证号
          String address = "";
          String avatar = "http://qhyxpic.oss.kujiale.com/avatars/72.jpg";
        // 签名加密
        // 设置HTTP请求的参数，等同于以query string的方式附在URL后面
        final List<NameValuePair> params = new ArrayList<NameValuePair>(10);
        params.add(new BasicNameValuePair("appuname", userName));
        params.add(new BasicNameValuePair("appuemail", email));
        params.add(new BasicNameValuePair("appuphone", phone));
        params.add(new BasicNameValuePair("appussn", ssn));
        params.add(new BasicNameValuePair("appuaddr", address));
        params.add(new BasicNameValuePair("appuavatar", avatar));
        params.add(new BasicNameValuePair("apputype", apputype));
        if(type!=null){
        	if(type.equals("0")){//登录跳转设计
                params.add(new BasicNameValuePair("dest", "0"));	
            }else if(type.equals("1")){//修改方案
                params.add(new BasicNameValuePair("dest", "1")); 
                params.add(new BasicNameValuePair("designid",designid));
                
            }else if(type.equals("2")){//选择户型修改
                params.add(new BasicNameValuePair("dest", "2"));	
                params.add(new BasicNameValuePair("planid", planid)); 
            }	
        }
        
        return postRequestKujiale(createUser.getAppuid().toString(),url, params);
	} 
	
	public  static String postRequestKujiale(String appuid, String url,List<NameValuePair> params){  
        final long timestamp = System.currentTimeMillis();
        String md5Val=DecoUtil.appSecret + DecoUtil.appKey + appuid + timestamp;
        final String sign = MD5Util.string2MD5(md5Val); 
        
        params.add(new BasicNameValuePair("appkey", DecoUtil.appKey));
        params.add(new BasicNameValuePair("timestamp", String.valueOf(timestamp)));
        if(appuid!=null&&!appuid.equals("")) params.add(new BasicNameValuePair("appuid", appuid));
        params.add(new BasicNameValuePair("sign", sign));
 	   // API地址，生产环境域名对应为www.kujiale.com
     // final String api = "http://www.kujiale.com/p/openapi/login";
      // 构造HTTP请求
    	final HttpPost postRequest = new HttpPost(url);
        HttpEntity resultEntity = null;
        try {
            final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
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
	
	
	public static String  getRequestKujiale(String url) {
         
        final HttpGet getRequest = new HttpGet(url);
        HttpEntity resultEntity = null;
        try {
            // 执行请求并获取返回值
            final HttpClient httpClient = new DefaultHttpClient();
            final HttpResponse response = httpClient.execute(getRequest);
            resultEntity = response.getEntity();
            final String result = EntityUtils.toString(resultEntity, "UTF-8");
            System.out.println(result);
            return result;
        } catch (final ClientProtocolException e) {
            e.printStackTrace(); 
        } catch (final IOException e) {
            e.printStackTrace(); 
        } finally {
            getRequest.abort();
            try {
                EntityUtils.consume(resultEntity);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

	public static String  deleteRequestKujiale(String url) {
		 final HttpDelete deleteRequest = new HttpDelete(url);
	        HttpEntity resultEntity = null;
	        try {
	            // 执行请求并获取返回值
	            final HttpClient httpClient = new DefaultHttpClient();
	            final HttpResponse response = httpClient.execute(deleteRequest);
	            resultEntity = response.getEntity();
	            final String result = EntityUtils.toString(resultEntity, "UTF-8");
	            System.out.println(result);
	            return result;
	        } catch (final ClientProtocolException e) {
	            e.printStackTrace(); 
	        } catch (final IOException e) {
	            e.printStackTrace(); 
	        } finally {
	            deleteRequest.abort();
	            try {
	                EntityUtils.consume(resultEntity);
	            } catch (final IOException e) {
	                e.printStackTrace();
	            }
	        }
	       return "";
	}
}
