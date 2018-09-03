package com.bizduo.zflow.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.bizduo.zflow.domain.sys.MenuItem;

public class Sorter implements Comparator<Object>{
 
	public int compare(Object arg0, Object arg1) {
		MenuItem menuItem1=(MenuItem)arg0;
		MenuItem menuItem2=(MenuItem)arg1; 
		  int flag=menuItem1.getIndexNum().compareTo(menuItem2.getIndexNum());
		  if(flag==0){
		   return menuItem1.getName().compareTo(menuItem2.getName());
		  }else{
		   return flag;
		  } 
	}
	
	//按key值进行排序 
    @SuppressWarnings({ "rawtypes" })
	public static Map sort(Map map,final String type) {
    	  /*	List list = new LinkedList(map.entrySet()); 
	        Collections.sort(list, new Comparator() { 
	            public int compare(Object o1, Object o2) { 
	                return ((Comparable) ((Map.Entry) (o1)).getValue()) 
	                        .compareTo(((Map.Entry) (o2)).getValue()); 
	            } 
	        }); 
	        Map result = new LinkedHashMap(); 
	        for (Iterator it = list.iterator(); it.hasNext();) { 
	            Map.Entry entry = (Map.Entry) it.next(); 
	            result.put(entry.getKey(), entry.getValue()); 
	        } 
	        return result;
	        */
	      Map<Object, Object> mapVK = new TreeMap<Object, Object>(
	            new Comparator<Object>() {
	                public int compare(Object v1, Object v2) { 
	                	if(type!=null&& type.equals("menu")){
	                		MenuItem menuItem1=(MenuItem) v1;
	                		Integer v1id= menuItem1.getIndexNum();
	                		MenuItem menuItem2=(MenuItem) v2;
	                		Integer v2id= menuItem2.getIndexNum(); 
	                		 if (v1id>v2id)  return 1 ;//Float.parseFloat(v1.toString())>Float.parseFloat(v2.toString()))return 1;
			                    else if(v1id==v2id) return 0;
			                    else return -1;
	                	}else if(type!=null&& type.equals("Long")){ 
		                    if(Float.parseFloat(v1.toString())>Float.parseFloat(v2.toString()))return 1;
		                    else if(v1==v2) return 0;
		                    else return -1;
	                	}
						return -1;
	                }
	            }
	        );
	        try {
	        	Set col = map.keySet();
		        Iterator iter = col.iterator();
		        while (iter.hasNext()) {
		        	if(type!=null&& type.equals("menu")){
		        		MenuItem key = (MenuItem) iter.next();
			        	Object value = (Object) map.get(key);
			            mapVK.put(key, value);
		        	}
		        	else if(type!=null&& type.equals("Long")){
		        		Integer key = (Integer) iter.next();
			        	Object value = (Object) map.get(key);
			            mapVK.put(key, value);	
		        	}else {
		        		Object key =   iter.next();
			        	Object value = (Object) map.get(key);
			            mapVK.put(key, value);	
		        	} 
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	        return mapVK;
	    }


}
