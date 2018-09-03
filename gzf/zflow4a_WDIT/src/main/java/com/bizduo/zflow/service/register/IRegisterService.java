package com.bizduo.zflow.service.register;

import java.util.List;

import com.bizduo.zflow.domain.register.Register;
import com.bizduo.zflow.domain.tableData.TableData;
import com.bizduo.zflow.service.base.IBaseService;
public interface IRegisterService extends IBaseService<Object, Long>{

	/**
	 * 根据id获得register
	 * 
	 * @param code
	 * @return
	 * @author lm
	 */
	public Register getRegisterByUsername(String username);

	/**
	 * 根据id删除register
	 * 
	 * @param code
	 * @return 2012-4-11
	 * @author lm
	 */
	public void deleteRegisterByUsername(String username);
	
	/**
	 * 获得所有Register
	 * @return
	 * 2012-4-17
	 * @author lm
	 */
	public List<Register> getAllRegister();
	
	/**
	 * 根据formId获得tableDatalist
	 * 
	 * @param code
	 * @return
	 * @author lm
	 */
	public List<TableData> getTableDataListByFormId(Long registerId);

	/**
	 * 根据FormId查询Register
	 * @return
	 * 2012-5-11
	 * @author lm
	 */
	public List<Register> getRegisterByFormId(Long formId);

	
	
	/**
	 * 获取formObject所有Data
	 * @param fromObject
	 * 2012-5-15
	 * @author dzt
	 */
	public List<Object> getfromObjectAllData(Object fromObject);

}
