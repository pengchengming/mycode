import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.bizduo.zflow.util.deco.DecoUtil;


public class TestMainMD5 {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException {
		 
		 // 分配给每个商家的唯一的appkey和appsecret
        
        final long timestamp = System.currentTimeMillis();
        final String sign = MD5(DecoUtil.appSecret + DecoUtil.appKey + timestamp);
        final String planId = "3FO4KHBFO4CN";
        final StringBuilder apiBuilder = new StringBuilder();
        // API地址，生产环境域名对应为www.kujiale.com
        apiBuilder.append(DecoUtil.apiurl+"/p/openapi/floorplan/")
        .append(planId)
        .append("/info")
        .append("?appkey=").append(DecoUtil.appKey)
        .append("&timestamp=").append(timestamp)
        .append("&sign=").append(sign);
        final HttpGet getRequest = new HttpGet(apiBuilder.toString());
        HttpEntity resultEntity = null;
        try {
            // 执行请求并获取返回值
            final HttpClient httpClient = new DefaultHttpClient();
            final HttpResponse response = httpClient.execute(getRequest);
            resultEntity = response.getEntity();
            final String result = EntityUtils.toString(resultEntity, "UTF-8");
            System.out.println(result);
        } catch (final ClientProtocolException e) {
            e.printStackTrace();
            return;
        } catch (final IOException e) {
            e.printStackTrace();
            return;
        } finally {
            getRequest.abort();
            try {
                EntityUtils.consume(resultEntity);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }


	}
	
	private static String MD5(final String input) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] digest = md.digest(input.getBytes());
        final String result = byte2hex(digest);
        return result;
    }
    /**
     * byte to string
     *
     * @param digest
     * @return
     */
    private static String byte2hex(final byte[] digest) {
        String des = "";
        String tmp = null;
        for (final byte element : digest) {
            tmp = (Integer.toHexString(element & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }


}
