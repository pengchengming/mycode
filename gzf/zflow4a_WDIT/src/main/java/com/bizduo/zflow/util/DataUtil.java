package com.bizduo.zflow.util;

import java.util.List;

public class DataUtil {

	
	/**
	 * 取double数组中的最大值.
	 * @param list
	 * @return
	 * @author pescado
	 * 2011-11-10 上午10:16:12
	 */
	public static Double getMaxValue(List<Double> list){
		double max=0;
		for(int i = 0 ; i<list.size() ; i++){
		    if(list.get(i)>max){
		    	max = list.get(i); 
		    }
		}
		return max;
	}
	/**
	 * 取double数组中的最小值.
	 * @param list
	 * @return
	 * @author pescado
	 * 2011-11-10 上午10:16:45
	 */
	public static Double getMinValue(List<Double> list){
		double min=0;
		if(list!=null&&list.size()>0){
			min = list.get(0);
			for(int i = 0 ; i<list.size() ; i++){
				if(list.get(i)<min){
					min = list.get(i); 
				}
			}
		}
		return min;
	}
	/**
	 * 根据double数据取合适的上限值.
	 * @param list 未排序的数字列表
	 * @return 上下限值，两个元素的数组
	 */
	public static Double[] getSuitableBound(List<Double> list){
		double min=list.get(0);
		double max=list.get(0);
		
		for(int i = 1 ; i<list.size() ; i++){
		    if(list.get(i)>max){
		    	max = list.get(i); 
		    }
		    if(list.get(i)<min){
		    	min = list.get(i); 
		    }
		}
		return new Double[]{min,max};
	}
	
	/**
	 * 根据天数差获取每个小时的间隔.
	 * @param dayInterval 天数差
	 * @return
	 * @author pescado
	 * 2011-11-15 下午05:59:31
	 */
	public static int getHourInterval(long dayInterval){
		int hour = 0;
		if(dayInterval==1){
			hour = 1;
		}else if(dayInterval==2){
			hour = 2;
		}else if(dayInterval>=3&&dayInterval<=6){
			hour = 6;
		}else if(dayInterval>6){
			int i = 1;
		    while(6*i<dayInterval){
		       hour = 12*i;
		       i++;
		    }
		}
		return hour;
	}
	
}
