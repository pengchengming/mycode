package com.bizduo.zflow.status;

public enum CRUD {
	CREATE("新增", 1),
	READ("读取", 2),
	UPDATE("修改", 3),
	DELETE("删除", 4),
	
	SHOW("展示", 5),
	VIEW("显示", 6),
	MODIFY("修改", 7),
	ADD("增加", 8),
	LIST("列表", 9),
	MOVE("移动", 10),
	REMOVE("移除", 11);
	
	// 成员变量
    private String name;
    private int index;
    
    // 构造方法
    private CRUD(String name, int index) {
        this.name = name;
        this.index = index;
    }
    // 普通方法
    public static String getName(int index) {
        for(CRUD o : CRUD.values()){
            if(o.getIndex() == index){
                return o.name;
            }
        }
        return null;
    }
    
    public static CRUD getSelf(int index){
    	for(CRUD o : CRUD.values()){
            if(o.getIndex() == index){
                return o;
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
