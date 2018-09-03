package com.bizduo.zflow.service.customform;

import java.util.List;

import com.bizduo.zflow.domain.form.FormProperty;
import com.bizduo.zflow.service.base.IBaseService;


public interface IFormPropertyService extends IBaseService<Object, Long>{
	//根据formId 查询出属性值
	public  List<FormProperty> getFormPropertyListByformId(Long fromId);

	public FormProperty getFormPropertyByZcolumnId(Long zcolumnId);
	
}
