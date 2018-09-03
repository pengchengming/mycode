package com.bizduo.zflow.service.sys.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.sys.Action;
import com.bizduo.zflow.domain.sys.MenuItem;
import com.bizduo.zflow.domain.sys.Module;
import com.bizduo.zflow.domain.sys.Permission;
import com.bizduo.zflow.domain.sys.Resource;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.sys.IMenuItemService;
import com.bizduo.zflow.service.sys.IPermissionService;
import com.bizduo.zflow.service.sys.IResourceService;
import com.bizduo.zflow.service.sys.IRoleService;
import com.bizduo.zflow.service.sys.IUserService;
import com.bizduo.zflow.wrapper.PermissionWrapper;
@Service
public class PermissionService extends BaseService<Permission, Integer> implements IPermissionService {

	@Autowired
	private IUserService userService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IMenuItemService menuItemService;
	@Autowired
	private IResourceService resourceService;
	/**
	 * 更新权限的设置
	 * @author Administrator
	 * @param codes 权限编码(资源编码+"_"+操作编码)
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public Collection<Permission> findByCode(String code) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Permission.class);
		cri.add(Restrictions.like("code", code, MatchMode.ANYWHERE));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Permission> updatePermissionSetting(String[] codes) {
		
		if(null == codes || 0 == codes.length) return null;
		List<Permission> perList = super.queryDao.getAll(Permission.class);
		List<Resource> resList = super.queryDao.getAll(Resource.class);
		List<Action> actList = super.queryDao.getAll(Action.class);
		
		//1.这里先修正权限的code ,以资源+操作的code为准.
		//  主要是防止资源或者操作的code被修改了造成的不一致问题
		for(Permission p : perList){
			Resource r = p.getResource();
			if(null == r){
				try {
					resourceService.delete(Permission.class, p.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				Action a = p.getAction();
				String ra = r.getCode() + "_" + a.getCode();
				if(!(p.getCode().equals(ra))){
					p.setCode(ra);
					super.queryDao.save(p);
				}
			}
		}
		
		//2.先删除被取消的权限,先在perList中判断,如果p.getCode在codes中不存在则表示该权限被取消了
		for(Permission p : perList){
			boolean toDel = true;
			for(String s : codes){
				if(s.equals(p.getCode())){
					toDel = false;
					break;
				}
			}
			if(toDel) 
				super.queryDao.remove(Permission.class, p.getId());
		}
		
		//3.保存被选择的权限,循环传过来的codes,如果code不在perList的P中存在就表示是新增的
		//  页面传过来的codes保存的对象都保存到list后返回(好像暂时没有用到perList,action中没有接收返回值)
		for(String code : codes){
			boolean toAdd = true;
			for(Permission p : perList){
				if(p.getCode().equals(code)){
					toAdd = false;
					break;
				}
			}
			if(toAdd){
				String[] c = code.split("_");//拆分为资源和操作的code
				Permission newp = new Permission();
				newp.setCode(code);
				for(Resource r : resList){
					if(r.getCode().equals(c[0])){
						newp.setResource(r);//将resource保存到P中
						break;
					}
				}
				for(Action a : actList){
					if(a.getCode().equals(c[1])){
						newp.setAction(a);//将action保存到P中
						break;
					}
				}
				newp = (Permission) super.queryDao.save(newp);//保存
				perList.add(newp);//将保存后返回的对象放到perList中
			}
		}
		return perList;
	}
	/**
	 * 权限的笛卡尔乘积,就是所有的组合(资源  X 操作)
	 * @author Administrator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PermissionWrapper> findPermissionDescart() {
		//1.取出资源,操作,现有权限
		//2.只有资源操作构造权限,如果已经存在的就标记为checked
		List<Resource> resList = resourceService.findByGroupFromModule(); //按模块分组查询出所有的资源
		List<Action> actList = super.queryDao.getAll(Action.class);
		List<Permission> perList = this.findByGroupFromResourceAndModule(null); //按资源和模块分组查询所有权限P
		List<Permission> totalPerList = new ArrayList<Permission>();
		if(0 == resList.size() || 0 == actList.size()){
			return null;
		}
	
		//3.构造出所有的Permission,已经构造的标记checked = true
		for(Resource r : resList){
			for(Action a : actList){
				Permission newp = new Permission();
				newp.setResource(r);
				newp.setAction(a);
				newp.setName(a.getName());
				newp.setCode(r.getCode() + "_" + a.getCode());
				for(Permission p : perList){
					if(r.getId().equals(p.getResource().getId()) && a.getId().equals(p.getAction().getId())){
						newp.setChecked(true);
						break;
					}
				}
				totalPerList.add(newp);
			}
		}
			
		//4.够造PermissionWrapper  将同一资源下所有权限通过PermissionWrapper封装起来,保存到pwList
		List<PermissionWrapper> pwList = new ArrayList<PermissionWrapper>();
		for(Resource r : resList){ 
			Resource newr=new Resource(r.getId(),r.getCode(),r.getName(),r.getScope(),r.getType(),r.getModule()); 
			
			List<Permission> per = new ArrayList<Permission>();
			for(Permission p : totalPerList){
				if(p.getResource().equals(r)){
					Permission newp=new Permission();
					newp.setId(p.getId());
					newp.setCode(p.getCode());
					newp.setName(p.getName());
					newp.setChecked(p.getChecked());
					newp.setAction(p.getAction());
					
					if(p.getResource()!=null){
						Resource resource=new Resource();
						resource.setId(p.getResource().getId());
						resource.setCode(p.getResource().getCode());
						resource.setName(p.getResource().getName());
						resource.setScope(p.getResource().getScope());
						newp.setResource(resource);
					}
					per.add(newp);
				}
			}
			PermissionWrapper pw = new PermissionWrapper(newr, per);
			pwList.add(pw);
		}
		
		//5.倒序计算PermissionWrapper模块名和行高(资源数量),放在模块的第一个资源行
		int resourceNum = 1;
		int lastPwIndex = pwList.size();
		Module lastModule = pwList.get((pwList.size()-1)).getResource().getModule(); //取出最后一个资源所在的模块
		
		for(int i = pwList.size() -1 ; i > -1; i--){
			Module module = pwList.get(i).getResource().getModule(); //取出每个资源所在的模块
			if(lastModule.equals(module)){ //相同模块
				lastPwIndex = i;
				resourceNum++;
			}else{  //循环到新模块,设置模块名和行高
				pwList.get(lastPwIndex).setModuleName(lastModule.getName());
				pwList.get(lastPwIndex).setRowspan(resourceNum);
				resourceNum = 1;
				lastPwIndex = i;
				lastModule = module;
			}
		}
		//处理最后一个
		pwList.get(0).setModuleName(lastModule.getName());
		pwList.get(0).setRowspan(resourceNum);
		return pwList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Permission> findByGroupFromResourceAndModule(Integer resourceId){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Permission.class);
		cri.createAlias("resource", "r");
		cri.createAlias("r.module", "m");
		if(null != resourceId && 0L != resourceId)
			cri.add(Restrictions.eq("resource.id", resourceId));
		cri.addOrder(Order.asc("m.code"));
		cri.addOrder(Order.asc("resource.id"));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	
	/**
	 * 设置菜单的权限
	 * @throws Exception 
	 */
	public void updateMenuPermission(Integer menuItemId, Integer[] ids) throws Exception {
		MenuItem mi = this.menuItemService.findObjByKey(MenuItem.class, menuItemId);

		List<Permission> perList = new ArrayList<Permission>();
		
		if(null == ids || 0 == ids.length){
			mi.getPermissions().clear();
		}else{
			for(int i = 0; i < ids.length; i++){
				Permission p = (Permission)super.findObjByKey(Permission.class, ids[i]);
				perList.add(p);
			}
			mi.getPermissions().clear();
			mi.getPermissions().addAll(perList);
//			for(Permission pe : perList){
//				mi.getPermissions().add(pe);
//			}
		}
		this.menuItemService.update(mi);
	}
	
	/**
	 * 设置角色的权限
	 * @throws Exception
	 */
	public void updateRolePermission(Integer roleId, Integer[] ids) throws Exception {
		Role r = this.roleService.findObjByKey(Role.class, roleId);
		List<Permission> perList = new ArrayList<Permission>();
		if(null == ids || 0 == ids.length){
			r.getPermissions().clear();
		}else{
			for(int i = 0; i < ids.length; i++){
				Permission p = (Permission)super.findObjByKey(Permission.class, ids[i]);
				perList.add(p);
			}
			r.getPermissions().clear();
			r.getPermissions().addAll(perList);
		}
		this.roleService.update(r);
	}
	
	/**
	 * 设置用户的权限
	 * @throws Exception
	 */
	public void updateUserPermission(Integer userId, Integer[] ids) throws Exception {
		User u = this.userService.findObjByKey(User.class, userId);
		List<Permission> perList = new ArrayList<Permission>();

		if(null == ids || 0 == ids.length){
			u.getPermissions().clear();
		}else{
			for(int i = 0; i < ids.length; i++){
				Permission p = (Permission)super.findObjByKey(Permission.class, ids[i]);
				perList.add(p);
			}
			u.getPermissions().clear();
//			u.getPermissions().addAll(perList);
		}
		this.userService.update(u);
	}
	/**
	 * 设置角色权限需要的数据(return 设置时页面展示所用的数据)
	 */
//	public List<PermissionWrapper> findByRole(Role role) {
//		Collection<Permission> list = role.getPermissions();
//		
//		return findPermissionByT(list);
//	}
	/**
	 * 设置用户权限需要的数据(return 设置时页面展示所用的数据)
	 */
//	public List<PermissionWrapper> findByUser(User user) {
//		Collection<Permission> list = user.getPermissions();
//		
//		return findPermissionByT(list);
//	}
//	
	@SuppressWarnings("unused")
	public List<PermissionWrapper> findByC(Collection<? extends Permission> c) {
		// 3.取到所有的资源,该资源按照模块分类(group by),按照模块的CODE ASC排序 (order by)
		List<Resource> resList = resourceService.findByGroupFromModule();
		
		List<PermissionWrapper> pwList = new ArrayList<PermissionWrapper>();
		// 4.循环资源
		for(int i = 0; i < resList.size(); i++){
			Resource r = resList.get(i);
			Resource newr=new Resource(r.getId(),r.getCode(),r.getName(),r.getScope(),r.getType(),r.getModule()); 
			// 5.取出资源对应的所有权限   (按资源和模块分组,按模块的CODE和资源的ID ASC排序)
			List<Permission> pList = this.findByGroupFromResourceAndModule(r.getId());
			// 6. pList会放到permissionWrapper的构造函数中来构造对象
			//	     这里主要是检查 mi 所对应权限是否存在于pList中,如果存在就设置该权限的checked=true;

			List<Permission> per = new ArrayList<Permission>();
			for(Permission p : pList){
				for(Permission pe : c){
					if(p.getId().equals(pe.getId())) p.setChecked(true);
				}
				p.setName(p.getAction().getName()); 
//				p.setCode(p.getResource().getCode() + "_" + p.getAction().getCode()); 
				
				Permission newp=new Permission();
				newp.setId(p.getId());
				newp.setCode(p.getCode());
				newp.setName(p.getName());
				newp.setChecked(p.getChecked());
				Action action=new Action(p.getAction().getId(),p.getAction().getCode(),p.getAction().getName());				
				newp.setAction(action);
				
				if(p.getResource()!=null){
					Resource resource=new Resource();
					resource.setId(p.getResource().getId());
					resource.setCode(p.getResource().getCode());
					resource.setName(p.getResource().getName());
					resource.setScope(p.getResource().getScope());
					newp.setResource(resource);
				}
				per.add(newp);				
			}
			// 7.构造PermissionWrapper对象
			PermissionWrapper pw = new PermissionWrapper(r, per);
			pwList.add(pw);
		}
		 
		
		// 8.倒序计算PermissionWrapper  模块名和行高(某个模块下对应的资源数)
		int resourceNum = 1;
		int lastPwIndex = pwList.size();
		Module lastModule = pwList.get(pwList.size()-1).getResource().getModule();
		
		for(int i = (pwList.size() - 1); i > -1; i--){
			Module m = pwList.get(i).getResource().getModule();
			// 9.相同模块则进入,这里计算相同模块下资源的总数
			if(lastModule.equals(m)){
				lastPwIndex = i;
				resourceNum++;
			}else{
				// 10.循环到新模块则设置当前模块名和行高(就是当前模块下的资源总数)
				pwList.get(lastPwIndex).setModuleName(lastModule.getName());
				pwList.get(lastPwIndex).setRowspan(resourceNum);
				
				resourceNum = 1;
				lastPwIndex = i;
				lastModule = m;
			}
		}
		// 11.处理最后一个
		pwList.get(0).setModuleName(lastModule.getName());
		pwList.get(0).setRowspan(resourceNum);
		return pwList;
	}
	
	/**
	 * 设置菜单权限需要的数据(return 设置时页面展示所用的数据)
	 */
//	public List<PermissionWrapper> findByMenuItem(MenuItem mi) {
		// 1.取到该菜单项的父级菜单的ID
//		Long parentId = mi.getParentMenuItem() == null ? 0L : mi.getParentMenuItem().getId();
		// 2.取到该菜单项的所有权限
//		Collection<Permission> list = mi.getPermissions();
//		return findPermissionByT(list);
//	}
	
//	@SuppressWarnings("unused")
//	private List<PermissionWrapper> findPermissionByT(Collection<Permission> list) {
//		// 3.取到所有的资源,该资源按照模块分类(group by),按照模块的CODE ASC排序 (order by)
//		List<Resource> resList = resourceService.findByGroupFromModule();
//		
//		List<PermissionWrapper> pwList = new ArrayList<PermissionWrapper>();
//		// 4.循环资源
//		for(int i = 0; i < resList.size(); i++){
//			Resource r = resList.get(i);
//			Long resId = r.getId();
//			// 5.取出资源对应的所有权限   (按资源和模块分组,按模块的CODE和资源的ID ASC排序)
//			List<Permission> pList = this.findByGroupFromResourceAndModule(resId);
//			// 6. pList会放到permissionWrapper的构造函数中来构造对象
//			//	     这里主要是检查 mi 所对应权限是否存在于pList中,如果存在就设置该权限的checked=true;
//			for(Permission p : pList){
//				for(Permission pe : list){
//					if(p.getId().equals(pe.getId())) p.setChecked(true);
//				}
//				p.setName(p.getAction().getName());
//				p.setCode(p.getResource().getCode() + "_" + p.getAction().getCode());
//			}
//			// 7.构造PermissionWrapper对象
//			PermissionWrapper pw = new PermissionWrapper(r, pList);
//			pwList.add(pw);
//		}
//		
//		// 8.倒序计算PermissionWrapper  模块名和行高(某个模块下对应的资源数)
//		int resourceNum = 1;
//		int lastPwIndex = pwList.size();
//		Module lastModule = pwList.get(pwList.size()-1).getResource().getModule();
//		
//		for(int i = (pwList.size() - 1); i > -1; i--){
//			Module m = pwList.get(i).getResource().getModule();
//			// 9.相同模块则进入,这里计算相同模块下资源的总数
//			if(lastModule.equals(m)){
//				lastPwIndex = i;
//				resourceNum++;
//			}else{
//				// 10.循环到新模块则设置当前模块名和行高(就是当前模块下的资源总数)
//				pwList.get(lastPwIndex).setModuleName(lastModule.getName());
//				pwList.get(lastPwIndex).setRowspan(resourceNum);
//				
//				resourceNum = 1;
//				lastPwIndex = i;
//				lastModule = m;
//			}
//		}
//		// 11.处理最后一个
//		pwList.get(0).setModuleName(lastModule.getName());
//		pwList.get(0).setRowspan(resourceNum);
//		return pwList;
//	}

}
