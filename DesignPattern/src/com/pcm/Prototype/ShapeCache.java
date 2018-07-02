/**
 * 
 */
package com.pcm.Prototype;

import java.util.Hashtable;

/**  
* @Package com.pcm.Prototype 
* @Title: ShapeCache.java   
* @Description: 创建一个类，从数据库获取实体类，并把它们存储在一个 Hashtable 中。  
* @author pcm  
* @date 2018年7月2日 上午10:29:21
* @version V1.0  
*/
public class ShapeCache {
	private static Hashtable<String, Shape> shapeMap = new Hashtable<String, Shape>();

	public static Shape getShape(String shapeId) throws CloneNotSupportedException {
		Shape cachedShape = shapeMap.get(shapeId);
		return (Shape) cachedShape.clone();
	}

	//对每种形状都运行数据库查询，并创建该形状
	//shapeMap.put(shapeKey,sahpe);
	//例如，我们要添加三种形状
	public static void loadCache(){
		Circle circle =new Circle();
		circle.setId("1");
		shapeMap.put(circle.getId(), circle);
		
		Square square =new Square();
		square.setId("2");
		shapeMap.put(square.getId(), square);
		
		Rectangle rectangle =new Rectangle();
		rectangle.setId("3");
		shapeMap.put(rectangle.getId(), rectangle);
	}
}
