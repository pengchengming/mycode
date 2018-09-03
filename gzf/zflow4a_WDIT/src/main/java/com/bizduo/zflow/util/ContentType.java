package com.bizduo.zflow.util;

public enum ContentType {
	  LYXW("旅游新闻", 1),	
	  JQXW("景区新闻", 2),
	  GLXL("攻略线路", 3),
	  YYDY("语音导游", 4),
	  SWQJ("三围全景", 5),
	  YHQA("周边优惠券", 6),
	  JCHD("精彩活动", 7),
	  HDDR("活动达人", 8);
	 // 成员变量
    private String name;
    private int index;
    
    // 构造方法
    private ContentType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (ContentType o : ContentType.values()) {
            if (o.getIndex() == index) {
                return o.name;
            }
        }
        return null;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}
