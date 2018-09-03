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
                // ���ݵ�ַ��ȡ����
                HttpGet request = new HttpGet(urlNameString);//���﷢��get����
                // ��ȡ��ǰ�ͻ��˶���
                HttpClient httpClient = new DefaultHttpClient();
                // ͨ����������ȡ��Ӧ����
                HttpResponse response = httpClient.execute(request);
                
                // �ж���������״̬���Ƿ�����(0--200��������)
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