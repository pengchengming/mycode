package com.bizduo.zflow.service.register.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.register.Register;
import com.bizduo.zflow.domain.tableData.TableData;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.register.IRegisterService;
import com.bizduo.zflow.status.ZFlowStatus;
@Service
public class RegisterService extends BaseService<Object, Long> implements IRegisterService {

	/**
	 * 根据name获得register
	 * 
	 * @param register
	 * @return 2012-4-9
	 * @author lm
	 */
	@SuppressWarnings("unchecked")
	public Register getRegisterByUsername(String username) {
		PageDetachedCriteria detachedCriteria = PageDetachedCriteria.forClass(Register.class);
		detachedCriteria.add(Restrictions.eq("username", username));
		detachedCriteria.add(Restrictions.eq("isDelete",ZFlowStatus.ISDELETE_NO));
		List<Register> registerList = this.queryDao.getByDetachedCriteria(detachedCriteria);
		if (registerList != null && registerList.size() > 0) {
			return registerList.get(0);
		}
		else return new Register();
	}

	/**
	 * 根据name删除register
	 * 
	 * @return 2012-4-11
	 * @author lm
	 */
	public void deleteRegisterByUsername(String username) {
		if (username != null) {
			Register register = getRegisterByUsername(username);
			register.setIsDelete(ZFlowStatus.ISDELETE_YES);
			this.queryDao.save(register);
		}
	}
    
	/**
	 * 获得所有register
	 * @param register
	 * @return
	 * 2012-4-9
	 * @author lm
	 */
	@SuppressWarnings("unchecked")
	public List<Register> getAllRegister(){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Register.class);
		cri.add(Restrictions.eq("isDelete", ZFlowStatus.ISDELETE_NO));
		return this.queryDao.getByDetachedCriteria(cri);
	}
	
	 /**
	 * 根据formId获得tableDatalist
	 * 
	 * @param code
	 * @return
	 * @author lm
	 */
	@SuppressWarnings("unchecked")
	public List<TableData> getTableDataListByFormId(Long registerId){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(TableData.class);
		cri.createAlias("register", "r");
		cri.add(Restrictions.eq("r.id", registerId));
		List<TableData> tableDataList = this.queryDao.getByDetachedCriteria(cri);
		return tableDataList;
	}
	
	/**
	 * 根据FormId查询Register
	 * @return
	 * 2012-5-11
	 * @author lm
	 */
	@SuppressWarnings("unchecked")
	public List<Register> getRegisterByFormId(Long formId){
		PageDetachedCriteria cri=PageDetachedCriteria.forClass(Register.class);
		cri.add(Restrictions.eq("form.id", formId));
		return  (List<Register>)this.queryDao.getByDetachedCriteria(cri);  
	}
	
	/**
	 * 获取formObject所有Data
	 * @param fromObject
	 * 2012-5-15
	 * @author dzt
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getfromObjectAllData(Object fromObject){
		try {
			PageDetachedCriteria cri=PageDetachedCriteria.forClass(fromObject.getClass());
			return  (List<Object>)this.queryDao.getByDetachedCriteria(cri);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		  
	}

}
