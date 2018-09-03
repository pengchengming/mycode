package com.bizduo.zflow.status;

public enum DeviceType {
	TERMINAL("终端机", 1),
	BLUETOOTH("蓝牙", 2);
	// 成员变量
    private String name;
    private int index;
    
    // 构造方法
    private DeviceType(String name, int index) {
        this.name = name;
        this.index = index;
    }
    // 普通方法
    public static String getName(int index) {
        for (DeviceType o : DeviceType.values()) {
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
