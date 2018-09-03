package com.bizduo.zflow.service.sys.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.sys.MenuItem;
import com.bizduo.zflow.domain.sys.Permission;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.sys.IMenuItemService;
import com.bizduo.zflow.util.Sorter;
@Service
public class MenuItemService extends BaseService<MenuItem, Integer> implements IMenuItemService {
	@SuppressWarnings("unchecked")
	public Collection<MenuItem> findMenuItemsByPage(int firstResult, int sizeNo) {
		 PageTrace pageTrace = new PageTrace(firstResult);
			pageTrace.setPageIndex(sizeNo);
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(MenuItem.class);
		return super.queryDao.getByDetachedCriteria(cri, pageTrace, false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MenuItem> findByParam(Map<String, Object> paramMap){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(MenuItem.class);
		Integer id = (Integer)paramMap.get("id");
		Integer level = (Integer)paramMap.get("level");
		if(null != level && 0 != level)
			cri.add(Restrictions.eq("level", level));
		if(null != id && 0L != id)
			cri.add(Restrictions.eq("parentMenuItem.id", id));
		cri.addOrder(Order.asc("indexNum"));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	
	@Override
	public MenuItem create(MenuItem obj) throws Exception{
		MenuItem pmi = obj.getParentMenuItem();
		obj.setIsLeafNode(false);
		if(null != pmi && null != pmi.getId() && 0L != pmi.getId()){
			pmi = super.findObjByKey(MenuItem.class, pmi.getId());
			if(null != pmi.getIsLeafNode() && pmi.getIsLeafNode())
				pmi.setIsLeafNode(false);
			Integer level = pmi.getLevel();
			obj.setLevel(level.intValue() + 1);
			obj.setParentMenuItem(pmi);
		}else{
			obj.setLevel(1);
			obj.setParentMenuItem(null);
		}
		return super.create(obj);
	}

	@SuppressWarnings("unchecked")
	public Collection<MenuItem> findIsLeafNode() {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(MenuItem.class);
		cri.add(Restrictions.eq("isLeafNode", true));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<MenuItem> getByLevel(Integer level,Integer isPhone){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(MenuItem.class);
		cri.add(Restrictions.eq("level", level));
		if(isPhone.intValue()==1)
			cri.add(Restrictions.eq("isPhone", 1)); 
		if(isPhone.intValue()==0)
			cri.add(Restrictions.eq("isPc", 1));
		cri.addOrder(Order.asc("indexNum"));
		return super.queryDao.getByDetachedCriteria(cri);
	}
	
	public Collection<MenuItem> findByUser(User user,boolean isRoleGroup) {
		Collection<MenuItem> menuitems = new HashSet<MenuItem>();
		Collection<GrantedAuthority> ups =new HashSet<GrantedAuthority>();
		if(isRoleGroup){
			Map<String, Collection<GrantedAuthority>> permissionMap=user.getPermissionMap();
			if(permissionMap!=null){
				ups = permissionMap.get(user.getRoleName());	
			}
		}else {
			ups = user.getPermissions();	
		}
		List<MenuItem> menuItemList =getmenuItemList(ups,0);
		menuitems.addAll(menuItemList);
		return menuitems;
	}
	public Collection<MenuItem> findPhoneByUser(User user,boolean isRoleGroup) {
		Collection<MenuItem> menuitems = new HashSet<MenuItem>();
		Collection<GrantedAuthority> ups =new HashSet<GrantedAuthority>();
		if(isRoleGroup){
			Map<String, Collection<GrantedAuthority>> phonePermissionMap=user.getPhonePermissionMap();
			if(phonePermissionMap!=null){
				ups = phonePermissionMap.get(user.getRoleName());	
			}
		}else {
			ups = user.getPhonePermissions(); 
		}
		List<MenuItem> menuItemList =getmenuItemList(ups,1);
		menuitems.addAll(menuItemList);
		return menuitems;
	}
	
	private List<MenuItem> getmenuItemList(Collection<GrantedAuthority> ups,Integer isPhone){
		List<MenuItem> menuItemList = new ArrayList<MenuItem>();
		//Collection<MenuItem> mis = this.getByLevel(3);
		Collection<MenuItem> mis = this.getByLevel(2,isPhone);
		
		if(null != ups && 0 < ups.size()){
			for(MenuItem mi : mis){
				if(null != mi.getPermissions() && 0 < mi.getPermissions().size()){
					Collection<Permission> mips = mi.getPermissions();
					if(null != mips && 0 < mips.size()){
						for(Permission p : mips){
							boolean exists = false;
							for(GrantedAuthority ga : ups){
								if(ga.getAuthority().equals(p.getCode())){
									if(isListContains(menuItemList, mi))
										menuItemList.add(mi);
									exists = true;
									break;
								}
							}
							if(exists) break;
						}
					}
				}
			}
		}
		return menuItemList;
	}
	
	public boolean isListContains(List<MenuItem> menuItemList, MenuItem mi){
		if(0 < menuItemList.size())
			for (MenuItem menuItem : menuItemList)
				if(menuItem.getId().intValue() == mi.getId().intValue()) 
					return false;
		return true; 
	}
	
	@SuppressWarnings("unchecked")
	public List<MenuItem> menuTreeNode(Collection<? extends MenuItem> c) throws Exception{
		//列出所有二级菜单 和三级集合
   	    Map<MenuItem,List<MenuItem>> subMenuItemListTwoMap = getParentSubMenuItemMap(c);   
   	   //循环Map    获取 二级集合
   	    subMenuItemListTwoMap= Sorter.sort(subMenuItemListTwoMap,"menu");
   	    List<MenuItem> menuItemTwoList = getMenuItemListByMap(subMenuItemListTwoMap);
   	    return menuItemTwoList;
   	    /*//列出所有一级菜单 和二级集合
   	    Map<MenuItem,List<MenuItem>> subMenuItemListThreeMap = getParentSubMenuItemMap(menuItemTwoList);
        //一级菜单和二级集合
        subMenuItemListThreeMap= Sorter.sort(subMenuItemListThreeMap,"menu");
        List<MenuItem> menuItemOneList = getMenuItemListByMap(subMenuItemListThreeMap);
        return menuItemOneList;*/
	}
	//获取二级集合
	public List<MenuItem> getMenuItemListByMap(Map<MenuItem,List<MenuItem>> subMenuItemListTwoMap){
		List<MenuItem> menuItemTwoList = new ArrayList<MenuItem>();
		Set<MenuItem> key = subMenuItemListTwoMap.keySet();
        for (Iterator<MenuItem> it = key.iterator(); it.hasNext();) {
        	MenuItem menuItem = it.next();
        	List<MenuItem> subMenuItemList= subMenuItemListTwoMap.get(menuItem);
        	
        	Sorter sorter=new Sorter();
        	Collections.sort(subMenuItemList, sorter);
        	   
        	  
        	menuItem.setSubMenuItemList(subMenuItemListTwoMap.get(menuItem));
        	menuItemTwoList.add(menuItem);
        }
        return menuItemTwoList;
	}
	//获取出入集合的    父级对象和子集集合
	public Map<MenuItem, List<MenuItem>> getParentSubMenuItemMap(Collection<? extends MenuItem> c) throws Exception {
		Map<MenuItem,List<MenuItem>> subMenuItemListMap = new LinkedHashMap<MenuItem, List<MenuItem>>();
		for(MenuItem menuItem :c){
			//判断二级的菜单
			MenuItem parent = super.findObjByKey(MenuItem.class, menuItem.getParentMenuItem().getId());
			if(subMenuItemListMap.get(parent) == null){
				//所有三级的菜单   //实例化三级集合
				List<MenuItem> subMenuItemList = new ArrayList<MenuItem>();
				subMenuItemList.add(menuItem);
				subMenuItemListMap.put(parent, subMenuItemList);
			}else{
				subMenuItemListMap.get(parent).add(menuItem);
			}
		}
		return subMenuItemListMap;
	}

}
