package com.bizduo.zflow.service.customform;

import java.util.List;

import com.bizduo.zflow.domain.form.SelectList;
import com.bizduo.zflow.service.base.IBaseService;

public interface ISelectListService extends IBaseService<SelectList, Long>  {

	List<SelectList> findByTableId(Long id);

}
