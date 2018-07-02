/**
 * 
 */
package com.pcm.Prototype;

/**  
* @Package com.pcm。Prototype
* @Title: PrototypePatternDemo.java   
* @Description: PrototypePatternDemo 使用 ShapeCache 类来获取存储在 Hashtable 中的形状的克隆。  
* @author pcm  
* @date 2018年7月2日 上午10:54:37
* @version V1.0  
*/
public class PrototypePatternDemo {
	public static void main(String[] args) throws CloneNotSupportedException {
		ShapeCache.loadCache();
		Shape clonedShape = ShapeCache.getShape("1");
		System.out.println("Shape:" + clonedShape.getType());

		Shape clonedShape2 = ShapeCache.getShape("2");
		System.out.println("Shape:" + clonedShape2.getType());

		Shape clonedShape3 = ShapeCache.getShape("3");
		System.out.println("Shape:" + clonedShape3.getType());
	}
}
