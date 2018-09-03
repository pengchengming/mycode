package zflow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bizduo.zflow.util.ccm.CreateTamp;


public class CreateTampMain { 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String,String> valueMap=new HashMap<String, String>();
		valueMap.put("id", "10001");
		try {
			 SimpleDateFormat dataFm=new SimpleDateFormat("yyyy-MM-dd");
			 String dataStr=dataFm.format(new Date());
			CreateTamp.writeFile("taleader", "d:/ccm/"+dataStr+"/","taleader.html", valueMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
