package com.bizduo.zflow.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
    /** 
     * 加载属性文件 
     * @param filePath 文件路径 
     * @return 
     */  
	public static Properties loadProps(String filePath){  
		   Properties dbProps = new Properties(); 
        try { 
            InputStream is =   new FileInputStream(filePath); 
            dbProps.load(is);    
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
        return dbProps;  
    }  
	/** 
     * 读取配置文件 
     * @param props 配置文件 
     * @param key  
     * @return 
     */  
    public static String getString(Properties properties,String key){  
        return properties.getProperty(key);  
    }  
    
	public static void saveFile(String path,String center){
//		FileToUploadRecord fileUploadRecord =new FileToUploadRecord();
		RandomAccessFile raf = null;
		Boolean isfoundRecord=false;
		try {
			File uploadRecord = new File(path); 
       	 	FileUtil.createFile(uploadRecord,path); 
       	 	/*System.out.println(uploadRecord.getParentFile());
			if(!uploadRecord.exists()){
				uploadRecord.createNewFile();
			}*/
			raf = new RandomAccessFile(uploadRecord,  "rw");
			if(!isfoundRecord){				
    			FileWriter fw = new FileWriter(uploadRecord);
    			fw.write(center);
    			fw.flush();  
    			fw.close();
			}
			
		} catch (Exception e) {
		   e.printStackTrace();
		} finally {
			   try {
				   if(raf!=null){
					   raf.close();
				   }
			   } catch (IOException e) {
				   e.printStackTrace();
			   }
			}
	
	}
	
	public static void saveFile(String path, MultipartFile file, String filename){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		String realPath = path + File.separator + "upload" + File.separator + sdf.format(new Date());
        File dir = new File(realPath);  
        if(dir.exists()) {  
            dir.mkdirs();  
        }  
        //这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的  
        try {
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, filename + ".xlsx"));
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	 /** 
     * @param args 
     */  
    @SuppressWarnings("unused")
	public static String getMD5(String path) {
        StringBuilder sb = new StringBuilder();  
        byte[] size = null;  
        StringBuilder noAlgorithm=new StringBuilder("无法使用MD5算法，这可能是你的JAVA虚拟机版本太低");  
        StringBuilder fileNotFound=new StringBuilder("未能找到文件，请重新定位文件路径");  
        StringBuilder IOerror=new StringBuilder("文件输入流错误");  
        try {  
            MessageDigest md5=MessageDigest.getInstance("MD5");//生成MD5类的实例  
            File file = new File(path); //创建文件实例，设置路径为方法参数  
            FileInputStream fs = new FileInputStream(file);   
            BufferedInputStream bi = new BufferedInputStream(fs);   
            ByteArrayOutputStream bo = new ByteArrayOutputStream();   
            byte[] b = new byte[bi.available()]; //定义字节数组b大小为文件的不受阻塞的可访问字节数  
            int i;   
            //将文件以字节方式读到数组b中  
            while ((i = bi.read(b, 0, b.length)) != -1)   
            {   
            }   
            md5.update(b);//执行MD5算法  
            for (byte by : md5.digest())   
            {  
            sb.append(String.format("%02X", by));//将生成的字节MD５值转换成字符串  
            }  
            bo.close();  
            bi.close();  
        } catch (NoSuchAlgorithmException e) {  
            // TODO Auto-generated catch block  
            return noAlgorithm.toString();  
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            return fileNotFound.toString();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            return IOerror.toString();  
        }  
        return sb.toString();//返回MD５值  
    }  	
    /***
     * 读取文件夹下所有文件
     * @param path
     * @param importDateLong
     * @return
     */
    public static List<String> traverseFolder(String path,Long importDateLong,List<String> filePathNames) {
    	boolean isAll=true;		
		if(importDateLong!=null)
			isAll=false;
		
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("文件夹是空的!");
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        traverseFolder(file2.getAbsolutePath(),importDateLong,filePathNames);
                    } else {
                    	boolean isdownload=false;
                    	if(isAll)
                    		isdownload=true;
                    	else if(file2.lastModified() > importDateLong){
                    		isdownload=true;
                    	}
                    	if(isdownload){
                        	String filePath= file2.getAbsolutePath();
                        	String filePathName=  filePath.substring(filePath.lastIndexOf("upload")-1);
                        	filePathNames.add(filePathName);
                            System.out.println("文件:" + file2.getAbsolutePath());
                    	}
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
        return filePathNames;
    }
    
    /**  
     * 下载远程文件并保存到本地  
     * @param remoteFilePath 远程文件路径   
     * @param localFilePath 本地文件路径  
     */
    public static boolean downloadFile(String remoteFilePath, String localFilePath)
    {
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        boolean  issuccess=true;
        try
        {
            urlfile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection)urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            //如果链接成功成功 创建本地目录
            File f = new File(localFilePath);
            boolean isExist= createFile(f,localFilePath);
            if(!isExist){
            	issuccess=false;
            }
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1)
            {
                bos.write(b, 0, len);
            }
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        }
        catch (Exception e)
        {
        	issuccess=false;
            e.printStackTrace();
        }
        finally
        {
            try
            {
            	if(bis!=null)
            		bis.close();
            	if(bos!=null)
            		bos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return issuccess;
    }
    
    
    public static boolean createFile(File file,String destFileName) {  
       // File file = new File(destFileName);  
        if(file.exists()) {  
            System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");  
            return false;  
        }  
        if (destFileName.endsWith(File.separator)) {  
            System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");  
            return false;  
        }  
        //判断目标文件所在的目录是否存在  
        if(!file.getParentFile().exists()) {  
            //如果目标文件所在的目录不存在，则创建父目录  
            System.out.println("目标文件所在目录不存在，准备创建它！");  
            if(!file.getParentFile().mkdirs()) {  
                System.out.println("创建目标文件所在目录失败！");  
                return false;  
            }  
        }  
        //创建目标文件  
        try {  
            if (file.createNewFile()) {  
                System.out.println("创建单个文件" + destFileName + "成功！");  
                return true;  
            } else {  
                System.out.println("创建单个文件" + destFileName + "失败！");  
                return false;  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
            System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage());  
            return false;  
        }  
    }  
    
    
 
}
