package com.bizduo.zflow.util.ccm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Map;

public class CreateTamp {
	 /**
	  * 读取文件
	  * 
	  * @param file
	  * @return
	  * @throws Exception
	  */
	 public static String readFile(String typeCode,Map<String ,String> valueMap) throws Exception {
		 String filePath = CreateTamp.class.getResource("").toString();
		 if(typeCode.trim().equals("taLeader")){
			 filePath += "tamp/taLeaderFormTamp.html";
		 }else if(typeCode.trim().equals("ppGroup")){
			 filePath += "Tamp/ppGroupTamp.html";
		 }else if(typeCode.trim().equals("ppFeedback")){
			 filePath += "Tamp/ppFeedbackTamp.html";
		 } else if(typeCode.trim().equals("unPPGroup")){
			 filePath += "Tamp/unPPGroupTamp.html";
		 }
		 String path= filePath.substring(6);
		// File file=   new File(path); 
		  //BufferedReader br = new BufferedReader(new FileReader(file));
		  BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8")); 
		  
		  StringBuffer sbf = new StringBuffer("");
		  String line = null;
		  while ((line = br.readLine()) != null) {   
			 // line= new String( line.getBytes("gbk"),"utf-8");
			  //line= new String(line.getBytes("GBK"),"UTF-8");
			  sbf.append(line).append("\r\n");// 按行读取，追加换行\r\n
		  }
		  br.close();
		  String str= sbf.toString();
		  for (Map.Entry<String, String> entry : valueMap.entrySet()) {
			  str= str.replaceAll("\\$\\{"+entry.getKey()+"\\}",entry.getValue());
		   }
		  return  str;
	 }
	 
	 /**
	  * 写入文件
	  * 
	  * @param str
	  *            要保存的内容
	  * @param savePath
	  *            保存的文件路径
	  * @throws Exception
	  *             找不到路径
	  */
	 public static void writeFile( String typeCode,String savePath,String fileName,Map<String ,String> valueMap) throws Exception {
		
		  String str = null;
		  try {  
			   str =  readFile(typeCode,valueMap);
		  } catch (Exception e) {
			  e.printStackTrace();
			  System.out.println("读取出错");
		  }  
		  File newFile=new File(savePath);
		  if(!newFile.exists()) newFile.mkdirs(); 
		  File outfile    = new File(savePath+fileName);                 
		  //如果文件不存在，则创建一个新文件
		  if(!outfile.isFile()){
		      outfile.createNewFile();
		  } 
		  BufferedWriter bw = new BufferedWriter(new FileWriter(savePath+fileName));
		  bw.write(str);
		  bw.close();
	 }
	 
	 
}
