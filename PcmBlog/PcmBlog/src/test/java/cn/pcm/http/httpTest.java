package cn.pcm.http;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

public class httpTest {
	
	
	@Test
	public void httpClientTest() {
		String urlNameString = "http://www.baidu.com/s?ie=UTF-8&wd=aa";
        String result="";
          try {
                // 根据地址获取请求
                HttpGet request = new HttpGet(urlNameString);//这里发送get请求
                // 获取当前客户端对象
                HttpClient httpClient = new DefaultHttpClient();
                // 通过请求对象获取响应对象
                HttpResponse response = httpClient.execute(request);
                
                // 判断网络连接状态码是否正常(0--200都数正常)
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    result= EntityUtils.toString(response.getEntity(),"utf-8");
                } 
                System.out.println(result);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
	}
}