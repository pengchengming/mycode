package com.bizduo.zflow.domain.base;

public class PageNewTrace {
 private  int pageIndex; //当前页数
 private  int lcsCount;  //每页条数
 private  int cordCnt;  //一共多少条
 private  String pagem; //页数展示
 private  int pageCnt;  //页数
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getLcsCount() {
		return lcsCount;
	}
	public void setLcsCount(int lcsCount) {
		this.lcsCount = lcsCount;
	}
	public int getCordCnt() {
		return cordCnt;
	}
	public void setCordCnt(int cordCnt) {
		this.cordCnt = cordCnt;
	}
	public String getPagem() {
		return pagem;
	}
	public void setPagem(String pagem) {
		this.pagem = pagem;
	}
	public int getPageCnt() {
		return pageCnt;
	}
	public void setPageCnt(int pageCnt) {
		this.pageCnt = pageCnt;
	} 
 
}
