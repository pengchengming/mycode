package zflow;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.aliyun.openservices.HttpMethod;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectListing;

public class PanMainTest {

        static String accessKeyId = "GpJqxQDmKAa7pouT";
        static String accessKeySecret = "8ccWp2nkm5qW8m15d7pIPMI23JLsRS";
        static String key ="logo.jpg";//request.getParameter("key");
        static String bucketName = "jahwa";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			testPUTMethod2();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		try {
//			testPUTMethod1();
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	void testGeneralMethod(){
		//保存当前文件 
		com.aliyun.openservices.oss.OSSClient ossClient;
		com.aliyun.openservices.oss.model.PutObjectResult  result;
		
        String accessid = "GpJqxQDmKAa7pouT";          // AccessID
        String accesskey = "8ccWp2nkm5qW8m15d7pIPMI23JLsRS";     // AccessKey
        String bucketName ="jahwa";		
        ossClient = new com.aliyun.openservices.oss.OSSClient(accessid, accesskey); //当然这里可以封装下
		
        ObjectListing objects  = ossClient.listObjects(bucketName);
        do   
        {  
            for (OSSObjectSummary  objectSummary : objects .getObjectSummaries())   
            {  
                System.out.println("Object: " + objectSummary.getKey());                  
            }                                 
//            objects = ossClient.listObjects(objects);  
        } while (objects.isTruncated());          
        
        System.out.println(""+objects .getObjectSummaries().size());        
        String objectkey1 ="test/Data.xlsx";
     // Generate a presigned URL
        Date expires = new Date (new Date().getTime() + 100000 * 60); // 1 minute to expire
        GeneratePresignedUrlRequest generatePresignedUrlRequest1 =
        new GeneratePresignedUrlRequest(bucketName, objectkey1);
        generatePresignedUrlRequest1.setExpiration(expires);
        URL url1 = ossClient.generatePresignedUrl(generatePresignedUrlRequest1);
        System.out.println(url1.toString());

        String objectkey2 ="test/test1.jpg";
        GeneratePresignedUrlRequest generatePresignedUrlRequest2 =
                new GeneratePresignedUrlRequest(bucketName, objectkey2);
        generatePresignedUrlRequest2.setMethod(HttpMethod.PUT);  	
        generatePresignedUrlRequest2.setExpiration(expires);  
//        generatePresignedUrlRequest2.addUserMetadata("usermeta", "uservalue"); // If you need to set user metadata  
        URL url2 = ossClient.generatePresignedUrl(generatePresignedUrlRequest2);   
        System.out.println(url2.toString());
        
	}
	
	
	
	static void testPUTMethod1() throws ClientProtocolException, IOException{
		
        OSSClient client = new OSSClient(accessKeyId, accessKeySecret);
        Date expiration = new Date(new Date().getTime() + 1000*60);
        URL url = client.generatePresignedUrl(bucketName, key, expiration, HttpMethod.PUT);
        System.out.println(url.toString());
        
        //利用httpclient的httpput，通过刚才生成的签名url向oss上传指定的文件！
        HttpClient httpclient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(url.toString());
            
        FileBody bin = new FileBody(new File("D:\\temp\\" + key));
        MultipartEntity reqEntity = new MultipartEntity();  
        reqEntity.addPart(key, bin);
        httpPut.setEntity(reqEntity);  
        httpPut.addHeader("Content-Type", "");    
        HttpResponse rsps = httpclient.execute(httpPut);  
        int statusCode = rsps.getStatusLine().getStatusCode();  
        if (statusCode == HttpStatus.SC_OK) {  
            HttpEntity resEntity = rsps.getEntity();
            System.out.println(EntityUtils.toString(resEntity));
            EntityUtils.consume(resEntity);
        }else{
            HttpEntity resEntity = rsps.getEntity();
            System.out.println(EntityUtils.toString(resEntity));
            EntityUtils.consume(resEntity);
        }
	
	}
	
	static void testPUTMethod2() throws Exception{

        OSSClient client = new OSSClient(accessKeyId, accessKeySecret);	
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key, HttpMethod.PUT);
		 request.setExpiration(new Date(new Date().getTime() + 1000*60));
		//request.setContentType("text/plain; charset=ISO-8859-1");
		URL url =client.generatePresignedUrl(request);
		System.out.println(url.toString());
		HttpClient httpclient = new DefaultHttpClient();
		HttpPut httpPut = new HttpPut(url.toString());
		HttpEntity entity = new FileEntity(new File("D:\\temp\\" + key), "application/java-achive"); 
//		new StringEntity("sssssss");
		httpPut.setEntity(entity);
//		有个更简单的办法不用修改源代码，在setEntity和execute之间，进行httpPut.addHeader("Content-Type", "");就可以有效避免execute的时候强行加进Content-Type的Header 
		httpPut.addHeader("Content-Type", "");
		System.out.println("Request headers count:" + httpPut.getAllHeaders().length);
		HttpResponse rsps = httpclient.execute(httpPut);
		System.out.println("Response status code:" + rsps.getStatusLine().getStatusCode());
		System.out.println("Response headers count:" + rsps.getAllHeaders().length);
		InputStream in = rsps.getEntity().getContent();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = in.read(buffer)) != -1) {
		outStream.write(buffer, 0, len);
		}
		outStream.flush();
		System.out.println(outStream.toString("utf-8"));
	}

}
