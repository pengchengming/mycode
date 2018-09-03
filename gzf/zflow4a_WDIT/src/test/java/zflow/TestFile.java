package zflow;

import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import org.json.JSONObject;

import com.bizduo.zflow.service.wdit.RequestService;
import com.bizduo.zflow.util.FileUtil;

public class TestFile {

	public static void main(String[] args) {
		int  maxi=9;
		int mini=1;
		for(int i=mini;i<=maxi;i++){
			
			for(int j=mini;j<=i;j++){
				System.out.print(j+"*"+i+"="+i*j+"  ");
			}
			System.out.println(" ");
		}
		
		
		
	}
	
	public  void a(){

		 ResourceBundle WB = ResourceBundle.getBundle("synchronizeConfig", Locale.getDefault());
		 String synImagePath= WB.getString("syn.synImagePath");
		 String synWebPath= WB.getString("syn.synWebPath");
		 
		 RequestService requestService=new RequestService();
		 JSONObject pathNames= requestService.getImageRequest(synImagePath, null);
		   
	
		if(pathNames!=null&&pathNames.length()>0){
			Iterator<String> sIterator = pathNames.keys();  
			while(sIterator.hasNext()){  
			    // 获得key  
			    String key = sIterator.next();  
			    // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可  
			    String value = pathNames.getString(key);  
			    value=value.replaceAll("\\\\", "/");
			    System.out.println("key: "+key+",value"+value);
			    FileUtil.downloadFile( synWebPath+value, "d:/synUpload"+value);	
			}  

		}
	 
		
		
	
	}

}
