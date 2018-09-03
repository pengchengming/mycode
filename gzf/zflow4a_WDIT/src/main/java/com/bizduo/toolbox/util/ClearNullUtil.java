package com.bizduo.toolbox.util;

import java.util.Iterator;
import java.util.Map;
/**
 * 清除 空值
 * @author Administrator
 *
 */
public class ClearNullUtil {

	/**
	 * 清除 map中的NUll和空
	 * @author Administrator
	 * @param mapParam
	 */
	public static void mapClear(Map<String,Object> mapParam){
		Iterator<String> ii= mapParam.keySet().iterator();
		while (ii.hasNext()) {
			String param=ii.next();
			Object obj=mapParam.get(param);
			if (obj==null) {
				ii.remove();
				continue;
			}
			
			if (obj.toString().equals("")){
				ii.remove();
				continue;
			}
			
		}
	}
}
