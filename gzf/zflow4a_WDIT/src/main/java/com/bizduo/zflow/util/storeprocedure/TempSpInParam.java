package com.bizduo.zflow.util.storeprocedure;

public class TempSpInParam {  
    private int a;  
    private int b;  
  
    public TempSpInParam(int a, int b) {  
        super();  
        this.a = a;  
        this.b = b;  
    }  
  
    @Override  
    public String toString() {  
        return "TempSpIn [a=" + a + ", b=" + b + "]";  
    }  
  
    public int getA() {  
        return a;  
    }  
  
    public void setA(int a) {  
        this.a = a;  
    }  
  
    public int getB() {  
        return b;  
    }  
  
    public void setB(int b) {  
        this.b = b;  
    }  
  
}  