package cn.pcm.utils;

import java.util.HashMap;

import org.json.JSONObject;

import com.baidu.aip.face.AipFace;

public class FaceUtil {

	// 设置APPID/AK/SK
	public static final String APP_ID = "9557647";
	public static final String API_KEY = "lYSr1CGRM64lqEbpuY9heff7";
	public static final String SECRET_KEY = "W4gQdoGGhh6o8bHH2SOUT0MlLEvECxv3";

	public static String face(byte[] file ) {
		// 自定义参数定义
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("max_face_num", "1");
		//options.put("face_fields", "expression");//笑
//		options.put("face_fields", "beauty");//美丑
//		options.put("face_fields", "gender");//性别
//		options.put("face_fields", "glasses");//眼镜
//		options.put("face_fields", "race");//人种
//		options.put("face_fields", "age");//年龄
//		options.put("face_fields", "faceshape");//脸型 square，triangle，oval椭圆脸，heart心形脸，round
		options.put("face_fields", "expression,beauty,gender,glasses,race,age,faceshape");//脸型
		
		
		
		// 初始化一个FaceClient
		AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
		
		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);
		
		// 调用API
//		String image = "C:\\Users\\pcm\\Pictures\\LifeFrame\\11.jpg";
//		return client.detect(image, options);
		//System.out.println(res.toString(2));
		
		    JSONObject response = client.detect(file, options);
			return response.toString();
	}
}