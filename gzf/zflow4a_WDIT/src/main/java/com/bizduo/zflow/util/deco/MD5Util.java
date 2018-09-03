package com.bizduo.zflow.util.deco;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MD5Util {
	 
    /*** 
     * MD5加码 生成32位md5码 
     */  
	public static String string2MD5(String inStr){  
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");  
        }catch (Exception e){  
            System.out.println(e.toString());  
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = inStr.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
  
    }  
	
	
	 /** 
     * 加密解密算法 执行一次加密，两次解密 
     */   
    public static String convertMD5(String inStr){  
  
        char[] a = inStr.toCharArray();  
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ 't');  
        }  
        String s = new String(a);  
        return s;  
  
    }  
  
    // 测试主函数  
  /*  public static void main(String args[]) {  
        String s = new String("tangfuqiang");  
        System.out.println("原始：" + s);  
        System.out.println("MD5后：" + string2MD5(s));  
        System.out.println("加密的：" + convertMD5(s));  
        System.out.println("解密的：" + convertMD5(convertMD5(s)));  
  
    }*/  
    
    public static Map<String,String> getPropertiesValue(String fileNamePath, List<String> keyList)throws IOException {
    	Map<String,String>  valMap=new HashMap<String,String>();
        Properties props = new Properties();  
        InputStream in = null;  
        try {  
            in = new FileInputStream(fileNamePath);  
             props.load(in);  
             for (String key : keyList) {
            	 String value = props.getProperty(key);
            	 valMap.put(key, value);
			} 
            // 有乱码时要进行重新编码  
            // new String(props.getProperty("name").getBytes("ISO-8859-1"), "GBK");  
            return valMap;  
  
        } catch (IOException e) {  
            e.printStackTrace();  
            return null;  
  
        } finally {  
            if (null != in)  
                in.close();  
        }  
    }  
    
}
