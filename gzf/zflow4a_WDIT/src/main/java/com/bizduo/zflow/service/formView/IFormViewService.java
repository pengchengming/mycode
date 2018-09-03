package com.bizduo.zflow.service.formView;

import java.util.List;

import com.bizduo.zflow.domain.form.FormView;
import com.bizduo.zflow.service.base.IBaseService;

public interface IFormViewService extends IBaseService<FormView, Long>  {

	FormView findFormViewByCode(String code);
	//查询全部
	List<FormView> findFormViewByAll();
 

}
