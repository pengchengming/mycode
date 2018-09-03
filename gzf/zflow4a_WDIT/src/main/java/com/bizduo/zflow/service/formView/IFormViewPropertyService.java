package com.bizduo.zflow.service.formView;

import java.util.List;

import com.bizduo.zflow.domain.form.FormViewProperty;
import com.bizduo.zflow.service.base.IBaseService;

public interface IFormViewPropertyService extends IBaseService<FormViewProperty, Long>  {

	List<FormViewProperty> getFormViewPropertyByCode(String code);

}
