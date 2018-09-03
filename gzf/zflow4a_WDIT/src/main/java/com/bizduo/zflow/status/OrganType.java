package com.bizduo.zflow.status;

public enum OrganType {
	TYY("运营商", 1, true),	
	TLY("旅游局", 2, true),
	TJQ("景区", 3, false),
	TSJ("商家", 4, false),
	TGR("个人", 5, false);

	// 成员变量
    private String name;
    private int index;
    private boolean recommend;
    // 构造方法
    private OrganType(String name, int index, boolean recommend) {
        this.name = name;
        this.index = index;
        this.recommend = recommend;
    }
    // 普通方法
    public static String getName(int index) {
        for (OrganType o : OrganType.values()) {
            if (o.getIndex() == index) {
                return o.name;
            }
        }
        return null;
    }
    
    public static OrganType getSelf(int index){
    	for(OrganType o : OrganType.values()){
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
	public boolean isRecommend() {
		return recommend;
	}
	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}
}
