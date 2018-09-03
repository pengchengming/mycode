package com.bizduo.zflow.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bizduo.zflow.ZflowThread.ZflowThread;
import com.bizduo.zflow.service.bizType.IDataDictionaryService;
import com.bizduo.zflow.service.customform.IExcelListService;
import com.bizduo.zflow.service.customform.IExcelTableListService;
import com.bizduo.zflow.service.customform.IFormPropertyService;
import com.bizduo.zflow.service.customform.ISelectConditionsService;
import com.bizduo.zflow.service.customform.ISelectListService;
import com.bizduo.zflow.service.customform.ISelectTableListService;
import com.bizduo.zflow.service.formView.IFormViewPropertyService;
import com.bizduo.zflow.service.formView.IFormViewService;

/**
 * Servlet implementation class InitZflowServlet
 */
public class InitZflowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitZflowServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		WebApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		IFormViewService formViewService =(IFormViewService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, IFormViewService.class);
		IFormViewPropertyService formViewPropertyService =(IFormViewPropertyService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, IFormViewPropertyService.class);
		IFormPropertyService formPropertyService=(IFormPropertyService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, IFormPropertyService.class);
		ISelectTableListService selectTableListService=(ISelectTableListService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, ISelectTableListService.class);
		ISelectListService selectListService=(ISelectListService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, ISelectListService.class);
		ISelectConditionsService selectConditionsService=(ISelectConditionsService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, ISelectConditionsService.class);
		IDataDictionaryService dataDictionaryService=(IDataDictionaryService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, IDataDictionaryService.class);
		IExcelTableListService  excelTableListService=(IExcelTableListService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, IExcelTableListService.class);
	    IExcelListService  excelListService=(IExcelListService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, IExcelListService.class);
			   
		String initType= request.getParameter("initType");
		
		ZflowThread zflowThread = new ZflowThread();
		
		zflowThread.setFormViewService(formViewService);
		zflowThread.setFormViewPropertyService(formViewPropertyService);
		zflowThread.setFormPropertyService(formPropertyService);
		zflowThread.setSelectTableListService(selectTableListService);
		zflowThread.setSelectListService(selectListService);
		zflowThread.setSelectConditionsService(selectConditionsService);
		zflowThread.setDataDictionaryService(dataDictionaryService);
		zflowThread.setExcelTableListService(excelTableListService);
		zflowThread.setExcelListService(excelListService);
		
		zflowThread.setInitType(initType);
        Thread thread = new Thread(zflowThread);
        thread.start();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

 
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		WebApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		IFormViewService formViewService =(IFormViewService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, IFormViewService.class);
		IFormViewPropertyService formViewPropertyService =(IFormViewPropertyService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, IFormViewPropertyService.class);
		IFormPropertyService formPropertyService=(IFormPropertyService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, IFormPropertyService.class);
		ISelectTableListService selectTableListService=(ISelectTableListService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, ISelectTableListService.class);
		ISelectListService selectListService=(ISelectListService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, ISelectListService.class);
		ISelectConditionsService selectConditionsService=(ISelectConditionsService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, ISelectConditionsService.class);
		IExcelTableListService  excelTableListService=(IExcelTableListService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, IExcelTableListService.class);
    	IExcelListService  excelListService=(IExcelListService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, IExcelListService.class);
		
		ZflowThread zflowThread1 = new ZflowThread(); 
		zflowThread1.setFormViewService(formViewService);
		zflowThread1.setFormViewPropertyService(formViewPropertyService);
		zflowThread1.setFormPropertyService(formPropertyService);
		/*
		zflowThread1.setSelectTableListService(selectTableListService);
		zflowThread1.setSelectListService(selectListService);
		zflowThread1.setSelectConditionsService(selectConditionsService);
		*/

		zflowThread1.setInitType("formView"); 
		Thread thread = new Thread(zflowThread1);
        thread.start();
        
		ZflowThread zflowThread2 =new ZflowThread(); 
		/*zflowThread2.setFormViewService(formViewService);
		zflowThread2.setFormViewPropertyService(formViewPropertyService);*/
		zflowThread2.setFormPropertyService(formPropertyService);
		zflowThread2.setSelectTableListService(selectTableListService);
		zflowThread2.setSelectListService(selectListService);
		zflowThread2.setSelectConditionsService(selectConditionsService);
		zflowThread2.setInitType("selectTable"); 
        Thread thread2 = new Thread(zflowThread2);
        thread2.start();
        
        IDataDictionaryService dataDictionaryService=(IDataDictionaryService) BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, IDataDictionaryService.class);
        ZflowThread zflowThread3 =new ZflowThread();
        zflowThread3.setInitType("dictionary");
        zflowThread3.setDataDictionaryService(dataDictionaryService);
        Thread thread3 = new Thread(zflowThread3);
        thread3.start();
        
        ZflowThread zflowThread4 =new ZflowThread(); 
        zflowThread4.setExcelTableListService(excelTableListService);
        zflowThread4.setExcelListService(excelListService);
        zflowThread4.setInitType("excelTable"); 
        Thread thread4 = new Thread(zflowThread4);
        thread4.start();
        
        ZflowThread zflowThread5 =new ZflowThread(); 
        zflowThread5.setFormViewService(formViewService);
        zflowThread5.setInitType("packageItem"); 
        Thread thread5 = new Thread(zflowThread5);
        thread5.start();
	}

}
