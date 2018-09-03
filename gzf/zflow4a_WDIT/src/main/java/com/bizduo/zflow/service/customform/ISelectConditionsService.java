package com.bizduo.zflow.service.customform;

import java.util.List;

import com.bizduo.zflow.domain.form.SelectConditions;
import com.bizduo.zflow.service.base.IBaseService;

public interface ISelectConditionsService extends IBaseService<SelectConditions, Long>  {

	List<SelectConditions> findByTableId(Long id);

}
