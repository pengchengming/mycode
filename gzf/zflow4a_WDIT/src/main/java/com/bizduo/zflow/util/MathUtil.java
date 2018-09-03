package com.bizduo.zflow.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.regex.Pattern;

public class MathUtil {
	// 默認除法運算精度
	private static final int DEF_DIV_SCALE = 10;

	// 這個類不能實例化
	private MathUtil() {
	}

	/**
	 * 提供精確的加法運算。
	 * 
	 * @param v1
	 *            被加數
	 * @param v2
	 *            加數
	 * @return 兩個參數的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精確的減法運算。
	 * 
	 * @param v1
	 *            被減數
	 * @param v2
	 *            減數
	 * @return 兩個參數的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精確的乘法運算。
	 * 
	 * @param v1
	 *            被乘數
	 * @param v2
	 *            乘數
	 * @return 兩個參數的積
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}
	/**
	 * 提供一定精度的乘法運算。
	 * 
	 * @param v1
	 *            被乘數
	 * @param v2
	 *            乘數
	 * @return 兩個參數的積
	 */
	public static double mul(double v1, double v2,int scale) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * 提供（相對）精確的除法運算，當發生除不盡的情況時，精確到 小數點以後10位元，以後的數字四捨五入。
	 * 
	 * @param v1
	 *            被除數
	 * @param v2
	 *            除數
	 * @return 兩個參數的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相對）精確的除法運算。當發生除不盡的情況時，由scale參數指 定精度，以後的數字四捨五入。
	 * 
	 * @param v1
	 *            被除數
	 * @param v2
	 *            除數
	 * @param scale
	 *            表示表示需要精確到小數點以後幾位。
	 * @return 兩個參數的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精確的小數位四捨五入處理。
	 * 
	 * @param v
	 *            需要四捨五入的數位
	 * @param scale
	 *            小數點後保留幾位
	 * @return 四捨五入後的結果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static float scientificNotationToFloat(String str){
		return 0f;
	}
	/* 
	  * 判断是否为整数  
	  * @param str 传入的字符串  
	  * @return 是整数返回true,否则返回false  
	*/  
	  public static boolean isInteger(String str) {    
	    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
	    return pattern.matcher(str).matches();    
	  } 
	  
	  /*  
	   * 判断是否为浮点数，包括double和float  
	   * @param str 传入的字符串  
	   * @return 是浮点数返回true,否则返回false  
	 */    
	   public static boolean isDouble(String str) {    
	     Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");    
	     return pattern.matcher(str).matches();    
	   }
	   
	    /**
	     * 转换为BigDecimal
	     *  
	     * @param o
	     * @return BigDecimal
	     * @author fantasy 
	     * @date 2013-8-27
	     */
	    public static BigDecimal toBig(Object o) {
	        if (o == null || o.toString().equals("") || o.toString().equals("NaN")) {
	            return new BigDecimal(0);
	        }
	        return new BigDecimal(o.toString());
	    }
	    
	    /**
	     * 计算百分比
	     *  
	     * @param divisor
	     * @param dividend
	     * @return String
	     * @author fantasy 
	     * @date 2013-8-27
	     */
	    public static String getPercent(Object divisor, Object dividend){
	        if(divisor == null || dividend == null){
	            return "";
	        }
	        NumberFormat percent = NumberFormat.getPercentInstance();   
	        //建立百分比格式化引用   
	        percent.setMaximumFractionDigits(2);
	        BigDecimal a = toBig(divisor);
	        BigDecimal b = toBig(dividend);
	        if(a.equals(toBig(0)) || b.equals(toBig(0)) || a.equals(toBig(0.0)) || b.equals(toBig(0.0))){
	       	 return "0.00%";
	        }
	        BigDecimal c = a.divide(b, 4, BigDecimal.ROUND_DOWN);
	        return percent.format(c);
	    }
	    
	    /**
	     * 计算比例
	     *  
	     * @param divisor
	     * @param dividend
	     * @return String
	     * @author fantasy 
	     * @date 2013-10-9
	     */
	    public static String divideNumber(Object divisor, Object dividend){
	    	if(divisor == null || dividend == null){
	            return "";
	        }
	    	 BigDecimal a = toBig(divisor);
	         BigDecimal b = toBig(dividend);
	         if(a.equals(toBig(0)) || b.equals(toBig(0))){
	        	 return "0";
	         }
	         BigDecimal c = a.divide(b, 2, BigDecimal.ROUND_DOWN);
	         return c.toString();
	    }
}
