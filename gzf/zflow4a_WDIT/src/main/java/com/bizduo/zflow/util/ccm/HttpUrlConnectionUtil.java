package com.bizduo.zflow.util.ccm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
 

public class HttpUrlConnectionUtil {
	public static String readContentFromPOST(String url,String params) throws IOException { 
        URL getUrl = new URL(url);
        // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
        // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) getUrl .openConnection();
        connection.setRequestMethod("POST");// 提交模式 
        connection.setDoOutput(true);// 是否输入参数
//        connection.setUseCaches(false);// Post 请求不能使用缓存              
//        connection.setInstanceFollowRedirects(true); //URLConnection.setInstanceFollowRedirects 是成员函数，仅作用于当前函数       
//        connection.setRequestProperty(" Content-Type ",    
//                " application/x-www-form-urlencoded ");  
        //StringBuffer params = new StringBuffer();
        // 表单参数与get形式一样
        //params.append("wen").append("=").append(wen).append("&").append("btnSearch").append("=").append(btnSearch);
        byte[] bypes = params.toString().getBytes("utf-8");
        connection.getOutputStream().write(bypes);// 输入参数
//        InputStream inStream=conn.getInputStream();
//        System.out.println(new String(StreamTool.readInputStream(inStream), "gbk")); 
        // 服务器
        connection.connect();
        // 取得输入流，并使用Reader读取
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String lines;
        String tempStr="";
        while ((lines = reader.readLine()) != null) {
        	tempStr += lines;
        }
        reader.close();
        // 断开连接
        connection.disconnect();
        System.out.println(tempStr);
        return tempStr;
    }
	
	 
}
