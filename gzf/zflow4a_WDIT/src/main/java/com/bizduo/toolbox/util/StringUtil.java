package com.bizduo.toolbox.util;
/**
 * 
 * @author Administrator
 *
 */
public class StringUtil {

	/**
	 * 已分隔符来输出传入的字符串
	 * @author Administrator
	 * @param split
	 * @param obj
	 * @return
	 */
	public static String toStringSpilt(String split,String ... obj){
		if (obj==null) return "";
		
		StringBuffer sbBuffer=new StringBuffer();
		for (String s:obj){
			if (s!=null && !s.equals("")){
				if (sbBuffer.length()==0)
					sbBuffer.append(s);
				else 
					sbBuffer.append(split).append(s);
			}
		}
		
		return sbBuffer.toString();
	}
	
	
	/**
	 * 按照长度获取字符串，如果超出截取最大长度，后面加...
	 * @author Administrator
	 * @param str
	 * @param len
	 * @return
	 */
	public static String maxString(String str,Integer len){
		if (str==null) return str;
		if (str.length()<=len) return str;
		return str.substring(0, len)+"...";
	}

	/**
	 * 判断传入参数,如果是Null或者空值，返回false，不为空返回true
	 * @author Administrator
	 * @param checkAll   True:所有的为空才返回,False:只要有一个为空返回
	 * @param strings
	 * @return
	 */
	public static Boolean checkNull(Boolean checkAll,Object ...objects ){
		Boolean ret=true;
		if (objects==null) return false;
		for (Object s : objects) {
			if (s==null || s.toString().trim().equals("")){
				if (!checkAll) 
					return false;
				else {
					ret= false;
				}
			}
		}
		return ret;
	}
	
	
	
	/**
	 * 返回等长字符，如果前缀+字符串>长度，返回字符串
	 * @author Administrator
	 * @param prefix 前缀
	 * @param len
	 * @param str
	 * @return
	 */
	public static String getMaxLength(String prefix,int len,String str){
		if (!checkNull(false,str) || str.length()>=len ) return str;
		
		if (prefix.length()+str.length()>len) return str;
		
		StringBuilder sb=new StringBuilder();
		sb.append(prefix);
		for (int i = 0; i < len-prefix.length()- str.length(); i++) {
			sb.append("0");
		}
		sb.append(str);
		
		return sb.toString();
	}

	
	/**
	 * 返回最长字符串，前方补足，如果传入值的长度大于，将切割字符串
	 * @author Administrator
	 * @param prefix
	 * @param len
	 * @param str
	 * @return
	 */
	public static String fixLength(String prefix,int len,String str){
		if (str==null){
			str="";
		}
		
		if (str.length()>=len){
			return str.substring(str.length()-len,str.length());
		}
		
		StringBuilder sb=new StringBuilder();
		sb.append(prefix);
		for (int i = 0; i < len-prefix.length()- str.length(); i++) {
			sb.append(prefix);
		}
		sb.append(str);
		
		return sb.toString();
	}
}
