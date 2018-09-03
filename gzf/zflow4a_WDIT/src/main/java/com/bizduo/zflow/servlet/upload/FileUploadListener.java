/**
 * 本例程演示了通过Web上传文件过程中的进度显示。您可以对本例程进行任何修改和使用。
 * 如果需要转载本例程，请您注明作者。
 *
 * 作者： 刘作晨
 * EMail:liuzuochen@gmail.com
 */

package com.bizduo.zflow.servlet.upload;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.ProgressListener;

public class FileUploadListener implements ProgressListener{
	private HttpServletRequest request=null;

	public FileUploadListener(HttpServletRequest request){
		this.request=request;
	}

	/**
	 * 更新状态
	 */
	public void update(long pBytesRead, long pContentLength, int pItems){
		FileUploadStatus statusBean= BackGroundService.getStatusBean(request);
		statusBean.setUploadTotalSize(pContentLength);
		//读取完成
	    if (pContentLength == -1) {
	    	statusBean.setStatusCode("2");
	       statusBean.setStatus("完成对" + pItems +"个文件的读取:读取了 " + pBytesRead + " bytes.");
	       statusBean.setReadTotalSize(pBytesRead);
	       statusBean.setSuccessUploadFileCount(pItems);
	       statusBean.setProcessEndTime(System.currentTimeMillis());
	       statusBean.setProcessRunningTime(statusBean.getProcessEndTime());
	    //读取中
	    } else {
	       statusBean.setStatus("当前正在处理第" + pItems +"个文件:已经读取了 " + pBytesRead + " / " + pContentLength+ " bytes.");
	       statusBean.setReadTotalSize(pBytesRead);
	       statusBean.setCurrentUploadFileNum(pItems);
	       statusBean.setProcessRunningTime(System.currentTimeMillis());
	       if(pBytesRead == pContentLength) 
	    	   statusBean.setStatusCode("2");
	       else
	    	   statusBean.setStatusCode("1");
	    	   
	    }
            BackGroundService.saveStatusBean(request,statusBean);
	}
}
