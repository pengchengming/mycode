package com.bizduo.zflow.service.formView;

import java.util.List;

import com.bizduo.zflow.domain.form.ViewPageProperty;
import com.bizduo.zflow.service.base.IBaseService;

public interface IViewPagePropertyService extends IBaseService<ViewPageProperty, Long>  {

	List<ViewPageProperty> getViewPagePropertyByCode(String code);

}
