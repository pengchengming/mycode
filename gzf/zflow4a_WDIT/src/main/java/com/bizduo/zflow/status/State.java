package com.bizduo.zflow.status;

public enum State {
	
	INITIAL("初始", 2), //2：未推荐；
	ON("开启", 1), //1：已推荐； 
	OFF("关闭", 3); //3：关闭;
	
	// 成员变量
    private String name;
    private int index;
    
    // 构造方法
    private State(String name, int index) {
        this.name = name;
        this.index = index;
    }
    // 普通方法
    public static String getName(int index) {
        for (State o : State.values()) {
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
