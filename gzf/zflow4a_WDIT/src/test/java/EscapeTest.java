import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 

import com.bizduo.zflow.util.FileUtil;


public class EscapeTest {

	public static void main(String[] args) {
		try {
			String path1="D:\\develop\\zflowWorkspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\zflow2a_WDIT\\WEB-INF\\views\\wdit";
			Long nowTime=new Date().getTime();			
			traverseFolderAll(path1,"D:\\jsp\\escapeJsp1\\"+nowTime+"\\");
			
			String path2="D:\\develop\\zflowWorkspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\zflow2a_WDIT\\wdit\\wdit\\request";
			traverseFolderAll(path2,"D:\\jsp\\escapeJsp1\\"+nowTime+"\\js\\");
			

			String path3="D:\\develop\\projects\\zflow\\zflow2\\eclipse\\zflow4a_WDIT\\zflow4a_WDIT\\src\\main\\webapp\\wdit\\createUI";
			traverseFolderAll(path3,"D:\\jsp\\escapeJsp1\\"+nowTime+"\\js\\wditcreateUI\\");
			String path4="D:\\develop\\projects\\zflow\\zflow2\\eclipse\\zflow4a_WDIT\\zflow4a_WDIT\\src\\main\\webapp\\script\\createUI";
			traverseFolderAll(path4,"D:\\jsp\\escapeJsp1\\"+nowTime+"\\js\\scriptcreateUI\\");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//traverseFolderAll("D:\\jsp\\escapeJsp\\test", filePathNames);
	} 

    public static List<String> traverseFolderAll(String path, String savePath) { 
		List<String> filePathNames=new ArrayList<String>();
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                //System.out.println("文件夹是空的!");
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                       // System.out.println("文件夹:" + file2.getAbsolutePath());
                        traverseFolderAll(file2.getAbsolutePath(),savePath);
                    } else {
                    	String filePath= file2.getAbsolutePath();
                    	if(filePath.lastIndexOf("wdit")>0){
                    		String filePathName=  filePath.substring(filePath.lastIndexOf("wdit")-1);
                        	System.out.println("文件:" + filePathName);
                        	readFileByChars(file2,savePath+filePathName);	
                    	}else if(filePath.lastIndexOf("createUI")>0){
                    		String filePathName=  filePath.substring(filePath.lastIndexOf("createUI")-1);
                        	System.out.println("文件:" + filePathName);
                        	readFileByChars(file2,savePath+filePathName);	
                    	}
                    	                         
                    	filePathNames.add(filePath);
                       // System.out.println("文件:" + file2.getAbsolutePath());
                    }
                }
            }
        } else {
            //System.out.println("文件不存在!");
        }
        return filePathNames;
    }
    

    public static void readFileByChars(File file,String savePath) {
         BufferedReader reader = null;
         try {
             System.out.println("以行为单位读取文件内容，一次读一整行：");
             reader = new BufferedReader(new FileReader(file));
             String tempString = null;
             int line = 1;
             // 一次读入一行，直到读入null为文件结束
             String html="";
             while ((tempString = reader.readLine()) != null) {
                 // 显示行号
                 System.out.println("line " + line + ": " + tempString);                 
                 line++;
                 html+=tempString+"\n";
             }
             html=html.replace(" where ", "_701^");
             html=html.replace(" WHERE ", "_701^");
             html=html.replace(" select ", "_702^");
             html=html.replace(" SELECT ", "_702^");
             html=html.replace(" and ", "_703^");
             html=html.replace(" AND ", "_703^");
             html=html.replace(" count", "_704^");
             html=html.replace(" COUNT", "_704^");
             html=html.replace(" or ", "_705^");
             html=html.replace(" OR ", "_705^");
             html=html.replace(" EXISTS", "_706^").replace(" exists ", "_706^");
             //|exec|insert||delete|update|
             //"'|and|count|*|%|chr|mid|master|truncate|char|declare|; |or|-|+|,";
             FileUtil.saveFile(savePath, html);
             reader.close();
         } catch (IOException e) {
             e.printStackTrace();
         } finally {
             if (reader != null) {
                 try {
                     reader.close();
                 } catch (IOException e1) {
                 }
             }
         }
    }
    
  
}
