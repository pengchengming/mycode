package com.bizduo.zflow.ZflowThread;

import com.bizduo.zflow.service.customform.IFormService;

public class FormThread implements Runnable {
	private String json;
	private IFormService formService;
	
	@Override
	public void run(){
			try {
				formService.saveFormDataJson(json);
			} catch (Exception e) {
				throw new RuntimeException();
			}
	} 
	public void setFormService(IFormService formService) {
		this.formService = formService;
	}
	public void setJson(String json) {
		this.json = json;
	}

}
