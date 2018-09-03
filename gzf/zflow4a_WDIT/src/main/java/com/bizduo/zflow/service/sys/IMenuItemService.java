package com.bizduo.zflow.service.sys;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bizduo.zflow.domain.sys.MenuItem;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.base.IBaseService;
public interface IMenuItemService extends IBaseService<MenuItem, Integer> {
	
	public Collection<MenuItem> findMenuItemsByPage(int firstResult, int sizeNo);
	
	public Collection<MenuItem> findByUser(User user,boolean isRoleGroup);
	public Collection<MenuItem> findPhoneByUser(User user,boolean isRoleGroup) ;
	
	public List<MenuItem> menuTreeNode(Collection<? extends MenuItem> c) throws Exception;
	
	public Collection<MenuItem> findIsLeafNode();
	
	public Map<MenuItem, List<MenuItem>> getParentSubMenuItemMap(Collection<? extends MenuItem> c)throws Exception;
	public List<MenuItem> getMenuItemListByMap(Map<MenuItem,List<MenuItem>> subMenuItemListTwoMap);
}
