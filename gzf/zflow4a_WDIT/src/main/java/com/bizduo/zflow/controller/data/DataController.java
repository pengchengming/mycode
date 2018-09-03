package com.bizduo.zflow.controller.data;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bizduo.zflow.service.data.IDataService;
import com.bizduo.zflow.util.FileUtil;
import com.bizduo.zflow.util.TimeUitl;
 


@Controller
@RequestMapping(value = "/data")
public class DataController {
	@Autowired
	private IDataService dataService;
	
	@RequestMapping(value="/service")
    public ModelAndView getService(@RequestParam(value = "p", required = false) String p, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
        List<Map<String, Object>> resultset = dataService.callShellProcedure(p);
        mav.addObject("resultset", resultset); 
        return mav;
    }
	
	@RequestMapping(value="/procedure")
	@ResponseBody
	public List<Map<String, Object>> procedure(@RequestParam(value = "p", required = true) String p, HttpServletRequest request, HttpServletResponse response){
		if(p.startsWith("U2014002|")||p.startsWith("U2014004|")){
			String nowdate="";
			try {
				  nowdate= TimeUitl.getTimeLongTime(new Date().getTime(),"yyyy-MM-dd HH:mm:ss");
			} catch (ParseException e) { 
				e.printStackTrace();
			}
			p+="|"+nowdate;
		}
        return dataService.callShellProcedure(p); 
	}
	
	public final static String uploadDataBase="/usr/local/sites/javasites/vsites/wdit/upload/databaseWdit";
	public final static String dataBaseConf=" mysqldump -h localhost -u root -p Sh201101 --set-charset=UTF8 zflow_gzf";
	@RequestMapping(value="/databaseWdit")
	@ResponseBody
	public  String  databaseWdit(){ 

    	JSONObject json=new JSONObject();
        try {
            if (exportDatabaseTool(  uploadDataBase, String.valueOf(new Date().getTime())+".sql" )) {  
                System.out.println("数据库成功备份！！！");  
                json.put("code", 1);
            } else {  
                System.out.println("数据库备份失败！！！");
                json.put("code", 0);
            }  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
            json.put("code", 0);
        }  
        return json.toString();
    }
    /** 
     * Java代码实现MySQL数据库导出 
     *  
     * @author GaoHuanjie 
     * @param hostIP MySQL数据库所在服务器地址IP 
     * @param userName 进入数据库所需要的用户名 
     * @param password 进入数据库所需要的密码 
     * @param savePath 数据库导出文件保存路径 
     * @param fileName 数据库导出文件文件名 
     * @param databaseName 要导出的数据库名 
     * @return 返回true表示导出成功，否则返回false。 
     */  
	public static boolean exportDatabaseTool(  String savePath, String fileName ) throws InterruptedException {  
        File saveFile = new File(savePath);  
        if (!saveFile.exists()) {// 如果目录不存在  
            saveFile.mkdirs();// 创建文件夹  
        }  
        if(!savePath.endsWith(File.separator)){  
            savePath = savePath + File.separator;  
        }  
          
        PrintWriter printWriter = null;  
        BufferedReader bufferedReader = null;  
        try {  
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(savePath + fileName), "utf8"));  
            Process process = Runtime.getRuntime().exec(dataBaseConf);  
//            String   command =  "mysqldump -h " + hostIP + " -u " + userName + " -p" + password + " --opt "+databaseName+" > "+savePath+fileName; //test Here is the table name with a password so that no  -p You can add the root   -p  Password
//            Process process =Runtime.getRuntime().exec("cmd /c "+command);
            
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");  
            bufferedReader = new BufferedReader(inputStreamReader);  
            String line;  
            while((line = bufferedReader.readLine())!= null){  
                printWriter.println(line);  
            }  
            printWriter.flush();  
            if(process.waitFor() == 0){//0 表示线程正常终止。  
                return true;  
            }  
        }catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (bufferedReader != null) {  
                    bufferedReader.close();  
                }  
                if (printWriter != null) {  
                    printWriter.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return false;  
    }  
	@RequestMapping(value="/getDatabaseWdit")
	@ResponseBody 
	public String   getDatabaseWdit(){  
    	JSONObject filePathNames=new JSONObject();
        try {
    		List<String> pathNams=new  ArrayList<String>();
    		FileUtil.traverseFolder(uploadDataBase,null,pathNams); 
    		for (int i = 0; i < pathNams.size(); i++) {
    			filePathNames.put(Integer.toString(i), pathNams.get(i));
    		}
        } catch (Exception e) {  
            e.printStackTrace();  
        }
		return filePathNames.toString();
    }
      
}
