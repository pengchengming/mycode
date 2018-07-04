package com.pcm.chainofresponsibility;

/**  
* @Package com.pcm.chainofresponsibility 
* @Title: AbstractLogger.java   
* @Description: 创建抽象的记录器类  
* @author pcm  
* @date 2018年7月4日 下午3:48:38
* @version V1.0  
*/
public abstract class AbstractLogger {
	public static int INFO = 1;
	public static int DEBUG = 2;
	public static int ERROR = 3;
	protected int level;
	// 责任链中的下一个元素
	protected AbstractLogger nextLogger;

	public void setNextLogger(AbstractLogger nextLogger) {
		this.nextLogger = nextLogger;
	}

	public void logMessage(int level, String message) {
		if (this.level <= level) {
			write(message);
		}
		if (nextLogger != null) {
			nextLogger.logMessage(level, message);
		}
	}

	abstract protected void write(String message);
}
