package com.bizduo.zflow.util.storeprocedure;

import java.util.ArrayList;
import java.util.List;

public class TempSpResult {  
    private int returnVal;  
    private int outVal;  
    private List<Integer> list = new ArrayList<Integer>();  
  
    @Override  
    public String toString() {  
        return "SpObj [returnVal=" + returnVal + ", outVal=" + outVal  
                + ", list=" + list + "]";  
    }  
  
    public int getReturnVal() {  
        return returnVal;  
    }  
  
    public void setReturnVal(int returnVal) {  
        this.returnVal = returnVal;  
    }  
  
    public int getOutVal() {  
        return outVal;  
    }  
  
    public void setOutVal(int outVal) {  
        this.outVal = outVal;  
    }  
  
    public List<Integer> getList() {  
        return list;  
    }  
  
    public void setList(List<Integer> list) {  
        this.list = list;  
    }  
  
}  