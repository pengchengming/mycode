package zflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.bizduo.zflow.domain.sys.Action;
import com.bizduo.zflow.domain.sys.MenuItem;
import com.bizduo.zflow.domain.sys.Module;
import com.bizduo.zflow.domain.sys.Organization;
import com.bizduo.zflow.domain.sys.Permission;
import com.bizduo.zflow.domain.sys.Resource;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.sys.IActionService;
import com.bizduo.zflow.service.sys.IMenuItemService;
import com.bizduo.zflow.service.sys.IModuleService;
import com.bizduo.zflow.service.sys.IOrganizationService;
import com.bizduo.zflow.service.sys.IPermissionService;
import com.bizduo.zflow.service.sys.IResourceService;
import com.bizduo.zflow.service.sys.IRoleService;
import com.bizduo.zflow.service.sys.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false) 
public class TestData extends AbstractJUnit4SpringContextTests {
    
    @Autowired
    private IModuleService moduleService;
    @Test
    public void createModule() throws Exception{
    	Collection<Module> c = new ArrayList<Module>();
    	c.add(new Module(1L,"01","工作台",null));
    	c.add(new Module(2L,"02","客户管理",null));
    	c.add(new Module(3L,"03","销售管理",null));
    	c.add(new Module(4L,"04","销售机会",null));
    	c.add(new Module(5L,"05","合同管理",null));
    	c.add(new Module(6L,"06","系统管理",null));
//    	c.add(new Module(7L,"07","监管统计",null));
//    	c.add(new Module(8L,"08","系统管理",null));
//    	c.add(new Module(9L,"00","首页",null));
    	for(Module m : c)
    		moduleService.create(m);
    }
    
    @Autowired
    private IActionService actionService;
    @Test
    public void createAction() throws Exception{
    	Collection<Action> c = new ArrayList<Action>();
    	c.add(new Action(1L,"ADD","创建"));
    	c.add(new Action(2L,"UPDATE","更改"));
    	c.add(new Action(3L,"VIEW","查看"));
    	c.add(new Action(4L,"DELETE","删除"));
//    	c.add(new Action(5L,"IMPORT","导入"));
//    	c.add(new Action(6L,"EXPORT","导出"));
//    	c.add(new Action(7L,"AUDIT","审核"));
//    	c.add(new Action(8L,"CANCEL","取消"));
//    	c.add(new Action(9L,"CHECK","验收"));
//    	c.add(new Action(10L,"CONFIRM","确认"));
//    	c.add(new Action(11L,"VIEWH","查看（医院）"));
//    	c.add(new Action(12L,"VIEWV","查看（配送商）"));
//    	c.add(new Action(13L,"VIEWALL","查看（运营商）"));
//    	c.add(new Action(14L,"SIGN","签订"));
//    	c.add(new Action(15L,"DECLARE","申报"));
//    	c.add(new Action(16L,"VOTE","投票"));
    	for(Action a : c)
    		actionService.create(a);
    }
    
    @Autowired
    private IResourceService resourceService;
    @Test
    public void creataResource() throws Exception{
    	Collection<Resource> c = new ArrayList<Resource>();
//    	c.add(new Resource(1L,"DOSAGEFORM","剂型","","功能点", moduleService.findObjByKey(Module.class, 1L)));
//    	c.add(new Resource(2L,"PUDI","铺底单","","单据", moduleService.findObjByKey(Module.class, 3L)));
//    	c.add(new Resource(3L,"POTR","传统补货","","", moduleService.findObjByKey(Module.class, 3L)));
//    	c.add(new Resource(4L,"POVMI","VMI补货","","", moduleService.findObjByKey(Module.class, 3L)));
//    	c.add(new Resource(5L,"DO","配送单","","单据", moduleService.findObjByKey(Module.class, 3L)));
//    	c.add(new Resource(6L,"RO","退货单","","单据", moduleService.findObjByKey(Module.class, 3L)));
//    	c.add(new Resource(7L,"INVOICE","发票","","单据", moduleService.findObjByKey(Module.class, 4L)));
//    	c.add(new Resource(8L,"SETTLEMENT","结算单","","单据", moduleService.findObjByKey(Module.class, 4L)));
//    	c.add(new Resource(9L,"PAYMENT","请付单","","单据", moduleService.findObjByKey(Module.class, 4L)));
//    	c.add(new Resource(10L,"COST","付款","","", moduleService.findObjByKey(Module.class, 4L)));
//    	c.add(new Resource(11L,"HOMEPAGE","首页","","页面", moduleService.findObjByKey(Module.class, 9L)));
//    	c.add(new Resource(12L,"PROJECT","遴选项目","","", moduleService.findObjByKey(Module.class, 2L)));
//    	c.add(new Resource(13L,"BIDDING","标的","","", moduleService.findObjByKey(Module.class, 2L)));
//    	c.add(new Resource(14L,"DFMERG","剂型合并","","", moduleService.findObjByKey(Module.class, 2L)));
//    	c.add(new Resource(15L,"BIDANNOUNCE","遴选公告","","", moduleService.findObjByKey(Module.class, 2L)));
//    	c.add(new Resource(16L,"BIDDOC","遴选文件","","", moduleService.findObjByKey(Module.class, 2L)));
//    	c.add(new Resource(17L,"BASEDATA","其他基础数据","","A", moduleService.findObjByKey(Module.class, 1L)));
//    	c.add(new Resource(18L,"PERMISSIONDEFINE","权限定义","","功能点", moduleService.findObjByKey(Module.class, 8L)));
//    	c.add(new Resource(19L,"USER","用户信息","","", moduleService.findObjByKey(Module.class, 8L)));
//    	c.add(new Resource(20L,"ROLE","角色信息","","", moduleService.findObjByKey(Module.class, 8L)));
//    	c.add(new Resource(21L,"HOSPITAL","医疗机构","","", moduleService.findObjByKey(Module.class, 1L)));
//    	c.add(new Resource(22L,"VENDOR","配送企业","","", moduleService.findObjByKey(Module.class, 1L)));
//    	c.add(new Resource(23L,"PRODUCER","生产企业","","", moduleService.findObjByKey(Module.class, 1L)));
//    	c.add(new Resource(24L,"REGULATION","监管机构","","", moduleService.findObjByKey(Module.class, 1L)));
//    	c.add(new Resource(25L,"DRUG","药品","","", moduleService.findObjByKey(Module.class, 1L)));
//    	c.add(new Resource(26L,"PRODUCT","产品","","", moduleService.findObjByKey(Module.class, 1L)));
//    	c.add(new Resource(27L,"RGNDRUGCATALOG","成交候选目录","","功能点", moduleService.findObjByKey(Module.class, 2L)));
//    	c.add(new Resource(28L,"POSTATIC","补货统计","","", moduleService.findObjByKey(Module.class, 7L)));
//    	c.add(new Resource(29L,"DRUGTRACE","药品追溯","","", moduleService.findObjByKey(Module.class, 7L)));
//    	c.add(new Resource(30L,"DRUGUSEEXCEPTION","用药异常","","", moduleService.findObjByKey(Module.class, 7L)));
//    	c.add(new Resource(31L,"MENUDEFINE","菜单定义","","", moduleService.findObjByKey(Module.class, 8L)));
//    	c.add(new Resource(32L,"MODULEDEFINE","模块定义","","", moduleService.findObjByKey(Module.class, 8L)));
//    	c.add(new Resource(33L,"RESOURCEDEFINE","资源定义","","功能点", moduleService.findObjByKey(Module.class, 8L)));
//    	c.add(new Resource(34L,"STORAGE","库存","","", moduleService.findObjByKey(Module.class, 4L)));
//    	c.add(new Resource(35L,"INOUTBOUND","出入库明细","","", moduleService.findObjByKey(Module.class, 4L)));
//    	c.add(new Resource(36L,"DATAMAPPING","数据对照","","功能点", moduleService.findObjByKey(Module.class, 1L)));
//    	c.add(new Resource(37L,"CONTRACT","合同","","", moduleService.findObjByKey(Module.class, 3L)));
//    	c.add(new Resource(38L,"INVENTORYCHECK","盘点任务","","", moduleService.findObjByKey(Module.class, 4L)));
//    	c.add(new Resource(39L,"PRODUCTEXPIRYDATE","效期","","统计指标", moduleService.findObjByKey(Module.class, 7L)));
//    	c.add(new Resource(40L,"REGION","地区代码","","", moduleService.findObjByKey(Module.class, 1L)));
//    	c.add(new Resource(41L,"DRUGTYPE","药品类型","","数据", moduleService.findObjByKey(Module.class, 1L)));
//    	c.add(new Resource(42L,"COMPANYTYPE","企业类型","","", moduleService.findObjByKey(Module.class, 1L)));
//    	c.add(new Resource(43L,"DISCOUNTBILL","分摊账单","","", moduleService.findObjByKey(Module.class, 6L)));
//    	c.add(new Resource(44L,"SERVICEBILL","服务费账单","","", moduleService.findObjByKey(Module.class, 6L)));
    	
//    	c.add(new Resource(1L, "WORKBENCH", "工作台","", "", moduleService.findObjByKey(Module.class, 1L)));
//    	
//    	c.add(new Resource(2L, "CLIENTENTRY", "客户列表", "", "", moduleService.findObjByKey(Module.class, 2L)));
//    	c.add(new Resource(3L, "LINKMEN", "联系人", "", "", moduleService.findObjByKey(Module.class, 2L)));
//    	
//		c.add(new Resource(4L, "SCHEDULEDESKTOP", "日程桌面", "", "", moduleService.findObjByKey(Module.class, 3L)));
//		c.add(new Resource(5L, "REPORTFORMS", "日报/周报/月报", "", "", moduleService.findObjByKey(Module.class, 3L)));
//		c.add(new Resource(6L, "TASKMANAGERMENT", "任务管理", "", "", moduleService.findObjByKey(Module.class, 3L)));
//		c.add(new Resource(7L, "EXPENSESCLAIMED ", "费用报销", "", "", moduleService.findObjByKey(Module.class, 3L)));
//		c.add(new Resource(8L, "QUOTATION", "报价单", "", "", moduleService.findObjByKey(Module.class, 3L)));
//		c.add(new Resource(9L, "EVECTIONRECORD", "出差记录", "", "", moduleService.findObjByKey(Module.class, 3L)));
//		c.add(new Resource(10L, "ITEMSBORROWED", "物品外借", "", "", moduleService.findObjByKey(Module.class, 3L)));
//		c.add(new Resource(11L, "MATERIALREQUISITION", "物料申请", "", "", moduleService.findObjByKey(Module.class, 3L)));
//	
//		c.add(new Resource(12L, "SALESCHANCECHILD", "销售机会", "", "", moduleService.findObjByKey(Module.class, 4L)));
//		c.add(new Resource(13L, "STATISTICANALYSIS", "统计分析", "", "", moduleService.findObjByKey(Module.class, 4L)));
//    		
//    	c.add(new Resource(14L, "CONTRACTMANAGEMENT", "合同管理", "", "", moduleService.findObjByKey(Module.class, 5L)));
//    	
//    	c.add(new Resource(15L, "SYSTEMMANAGEMENT", "系统管理", "", "", moduleService.findObjByKey(Module.class, 6L)));

    	for(Resource r : c)
    		resourceService.create(r);
    }
    
    @Autowired
    private IMenuItemService menuItemService;
    @Test
    public void createMenuItem() throws Exception{
    	List<MenuItem> c = new ArrayList<MenuItem>();
    //	id, clsName, description, Int indexNum, B isLeafNode, Int level, name, pageDisplayName, projectCode, shortCutLink, url, MenuItem parentMenuItem
//    	c.add(new MenuItem(1L, "0", "菜单描述", 1, false, 1, "home", "首页", "home_code", "shortCutLink", "index.jsp", null));
    	
//    	c.add(new MenuItem(1L, "1", "工作台", 1, false, 1, "workbench", "工作台", "WORKBENCH", "sc", "workbench.jsp", null));
//    	c.add(new MenuItem(2L, "2", "客户管理", 2, false, 1, "clientManagement", "客户管理", "CLIENTMANAGEMENT", "sc", "client.jsp", null));
//    		c.add(new MenuItem(3L, "21", "客户列表", 1, true, 2, "clientEntry", "客户列表", "CLIENTENTRY", "sc", "list.jsp", new MenuItem(2L, 1)));
//    		c.add(new MenuItem(4L, "22", "联系人", 2, true, 2, "linkmen", "联系人", "LINKMEN", "sc", "linkmen.jsp", new MenuItem(2L, 1)));
//    	c.add(new MenuItem(5L, "3", "销售管理", 3, false, 1, "salesManagement", "销售管理", "SALESMANAGEMENT", "sc", "sales.jsp", null));
//    		c.add(new MenuItem(6L, "31", "日程桌面", 1, true, 2, "scheduleDesktop", "日程桌面", "SCHEDULEDESKTOP", "sc", "schedule.jsp", new MenuItem(5L, 1)));
//    		c.add(new MenuItem(7L, "32", "日报/周报/月报", 2, true, 2, "reportForms", "日报/周报/月报", "REPORTFORMS", "sc", "reportforms.jsp", new MenuItem(5L, 1)));
//    		c.add(new MenuItem(8L, "33", "任务管理", 3, true, 2, "taskManagerment", "任务管理", "TASKMANAGERMENT", "sc", "task.jsp", new MenuItem(5L, 1)));
//    		c.add(new MenuItem(9L, "34", "费用报销", 4, true, 2, "expensesClaimed ", "费用报销", "EXPENSESCLAIMED", "sc", "expensesClaimed.jsp", new MenuItem(5L, 1)));
//    		c.add(new MenuItem(10L, "35", "报价单", 5, true, 2, "quotation", "报价单", "QUOTATION", "sc", "quotation.jsp", new MenuItem(5L, 1)));
//    		c.add(new MenuItem(11L, "36", "出差记录", 6, true, 2, "evectionRecord", "出差记录", "EVECTIONRECORD", "sc", "evectionRecord.jsp", new MenuItem(5L, 1)));
//    		c.add(new MenuItem(12L, "37", "物品外借", 7, true, 2, "itemsBorrowed", "物品外借", "ITEMSBORROWED", "sc", "itemsBorrowed.jsp", new MenuItem(5L, 1)));
//    		c.add(new MenuItem(13L, "38", "物料申请", 8, true, 2, "materialRequisition", "物料申请", "MATERIALREQUISITION", "sc", "materialRequisition.jsp", new MenuItem(5L, 1)));
//    	c.add(new MenuItem(14L, "4", "销售机会", 4, false, 1, "salesChance", "销售机会", "SALESCHANCE", "sc", "salesChance.jsp", null));
//    		c.add(new MenuItem(15L, "41", "销售机会", 1, true, 1, "salesChanceChild", "销售机会", "SALESCHANCECHILD", "sc", "salesChance.jsp", new MenuItem(14L, 1)));
//    		c.add(new MenuItem(16L, "42", "统计分析", 2, true, 1, "statisticAnalysis", "统计分析", "STATISTICANALYSIS", "sc", "statisticAnalysis.jsp", new MenuItem(14L, 1)));
//    	c.add(new MenuItem(17L, "5", "合同管理", 5, false, 1, "contractManagement", "合同管理", "CONTRACTMANAGEMENT", "sc", "contract.jsp", null));
//    		c.add(new MenuItem(18L, "51", "合同管理", 1, true, 2, "contractManagement", "合同管理", "CONTRACTMANAGEMENT", "sc", "contract.jsp", new MenuItem(17L, 1)));
//    	c.add(new MenuItem(19L, "6", "系统管理", 6, false, 1, "systemManagement", "系统管理", "SYSTEMMANAGEMENT", "sc", "system.jsp", null));
//    		c.add(new MenuItem(20L, "61", "系统管理", 1, true, 2, "systemManagement", "系统管理", "SYSTEMMANAGEMENT", "sc", "system.jsp", new MenuItem(19L, 1)));
//    	c.add(new MenuItem(1001L,"0","dec1",1,false,1,"首页","首页","","","/homepage.htm",null));
//    	
//		c.add(new MenuItem(1002L,"1","bbb1",2,false,1,"数据管理","数据管理","","","",null));
//			c.add(new MenuItem(1038L,"","",10,false,2,"基础数据","基础数据","","","",new MenuItem(2L, 1)));
//			c.add(new MenuItem(1039L,"","",30,false,2,"机构","机构","","","",new MenuItem(2L, 1)));
//		
//		
//		c.add(new MenuItem(1003L,"3","d2",4,false,1,"合同管理","合同管理","","","",null));
//			c.add(new MenuItem(1073L,"","",1,false,2,"合同管理","合同管理","","","",new MenuItem(5L, 1)));
//				c.add(new MenuItem(1082L,"","",7,true,3,"合同查询","合同查询","","","/contract/contractQueryJsp.htm",new MenuItem(6L, 2)));
//				c.add(new MenuItem(1084L,"","",1,true,3,"合同模板","合同模板","","","/contract/jsp_410.htm",new MenuItem(6L, 2)));
//				c.add(new MenuItem(1085L,"","",2,true,3,"合同起草","合同起草","","","/contract/jsp_420.htm",new MenuItem(6L, 2)));
//				c.add(new MenuItem(1086L,"","",3,true,3,"合同审核及签订","合同审核及签订","","","/contract/contractSignJsp.htm",new MenuItem(6L, 2)));
//			c.add(new MenuItem(1088L,"","",10,true,3,"合同履约进度","合同履约进度","","","",new MenuItem(5L, 1)));
//		
//		c.add(new MenuItem(1004L,"4","a",5,false,1,"库存管理","库存管理","","","",null));
//			c.add(new MenuItem(1010L,"","",1,false,2,"库存管理","库存管理","","","",new MenuItem(12L, 1)));
//				c.add(new MenuItem(1058L,"VMIREF","",10,true,3,"补货计划生成","补货计划生成","","","/order/jsp_650.htm",new MenuItem(13L, 2)));
//				c.add(new MenuItem(1060L,"VMIREF","",20,true,3,"补货计划审批","补货计划审批","","","/order/jsp_651.htm",new MenuItem(13L, 2)));
//				c.add(new MenuItem(1061L,"VMIREF","",5,true,3,"补货模板维护","补货模板维护","","","/order/jsp_653.htm",new MenuItem(13L, 2)));
//			c.add(new MenuItem(1011L,"","医疗机构",2,false,2,"铺底管理","铺底管理","","","",new MenuItem(12L, 1)));
//				c.add(new MenuItem(1149L,"","医院",20,true,3,"铺底单确认","铺底单确认","","","/order/jsp_610_2.htm",new MenuItem(17L, 2)));
//		
//		c.add(new MenuItem(1005L,"8","",10,false,1,"系统管理","系统管理","","","",null));
//			c.add(new MenuItem(1112L,"","",null,false,2,"系统管理","系统管理","","","",new MenuItem(19L, 1)));
//			c.add(new MenuItem(1111L,"","",null,false,2,"系统初始化","系统初始化","","","",new MenuItem(19L, 1)));
			
		for(int i = 0; i < c.size(); i++)
			menuItemService.create(c.get(i));
			
    }
    
    @Autowired
    private IRoleService roleService;
    @Test
    public void creataRole() throws Exception{
    	Collection<Role> c = new ArrayList<Role>();
//    	c.add(new Role(1L,"系统管理员","ROLE_ADMIN"));
//    	c.add(new Role(1001L,"配送企业","ROLE_VENDOR"));
//    	c.add(new Role(1003L,"医院采购","ROLE_HOS_PUR"));
//    	c.add(new Role(1004L,"库管","ROLE_HOS_WH_MGR"));
//    	c.add(new Role(1005L,"运营商","ROLE_OPERATOR"));
//    	c.add(new Role(1006L,"监管机构","ROLE_GOV"));
//    	c.add(new Role(2L,"有登陆系统权限的角色","ROLE_LOGIN"));
//    	c.add(new Role(1008L,"运营商数据维护","ROLE_OP_DATA_MAINTAIN"));
//    	c.add(new Role(3L,"基础数据查看角色","ROLE_OP_DATA_VIEW"));
    	
    	for(Role r : c)
    		roleService.create(r);
    }
    
    @Autowired
    private IOrganizationService organizationService;
    @Test
    public void createOrganization() throws Exception{
    	List<Organization> c = new ArrayList<Organization>();
//    	c.add(new Organization(1L, "商度集团", "商度集团", "商度集团(中国)股份有限公司", 1, null));
//	    	c.add(new Organization(2L, "上海分公司", "上海分公司", "商度集团(中国)股份有限公司上海分公司", 1, new Organization(1L, 1)));
//	    		c.add(new Organization(3L, "上海分公司研发部", "上海分公司研发部", "商度集团(中国)股份有限公司上海分公司研发部", 1, new Organization(2L, 2)));
//	    		c.add(new Organization(4L, "上海分公司商务部", "上海分公司商务部", "商度集团(中国)股份有限公司上海分公司商务部", 2, new Organization(2L, 2)));
//	    		c.add(new Organization(5L, "上海分公司财务部", "上海分公司财务部", "商度集团(中国)股份有限公司上海分公司财务部", 3, new Organization(2L, 2)));
//	    	c.add(new Organization(6L, "北京分公司", "北京分公司", "商度集团(中国)股份有限公司北京分公司", 2, new Organization(1L, 1)));
//	    		c.add(new Organization(7L, "北京分公司销售部", "北京分公司销售部", "商度集团(中国)股份有限公司北京分公司销售部", 1, new Organization(6L, 2)));
//	    		c.add(new Organization(8L, "北京分公司商务部", "北京分公司商务部", "商度集团(中国)股份有限公司北京分公司商务部", 2, new Organization(6L, 2)));
//	    		c.add(new Organization(9L, "北京分公司研发部", "北京分公司研发部", "商度集团(中国)股份有限公司北京分公司研发部", 3, new Organization(6L, 2)));
//    	
    	for(int i = 0; i < c.size(); i++)
    		organizationService.create(c.get(i));
    }
    
    
    @Autowired
    private IUserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    public void createUser() throws Exception{
    	Collection<Role> roles = new HashSet<Role>();
//    	roles.add(roleService.findObjByKey(Role.class, 1L));
//    	roles.add(roleService.findObjByKey(Role.class, 2L));
//    	roles.add(roleService.findObjByKey(Role.class, 3L));
    	
    	Collection<User> c = new ArrayList<User>();
    	User user = new User();
    	user.setUsername("fay");
    	user.setPassword(passwordEncoder.encode("111111"));
    	user.setEnabled(true);
    	user.setIsAdmin(true);
    	c.add(user);
//    	c.add(new User(1000L, "leslie", "leslie", "cheung", "leslie", organizationService.findObjByKey(Organization.class, 3L), roles));
    	
    	
    	for(User u : c)
    		userService.create(u);
    }
    
    @Autowired
    private IPermissionService permissionService;
    @Test
    public void createPermission() throws Exception{
    	Collection<Action> actions = actionService.findAll(Action.class);
    	Collection<Resource> resources = resourceService.findAll(Resource.class);
    	Collection<Permission> c = new ArrayList<Permission>();
    	for(Resource r : resources){
    		for(Action a : actions){
    			Permission p = new Permission(a, r);
    			c.add(p);
    		}
    	}
    	for(Permission p : c)
    		permissionService.create(p);
    }
    
    @Test
    public void createMenuItemPermission() throws Exception{
    	Collection<MenuItem> menuitems = menuItemService.findIsLeafNode();
    	for(MenuItem mi : menuitems){
    		mi.setPermissions(permissionService.findByCode(mi.getProjectCode()));
    		menuItemService.update(mi);
    	}
    	
    }
}
