package com.bizduo.zflow.util.storeprocedure;
 
import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
 
	/**
	 * 所有测试的基类
	 *
	 *继承自AbstractTransactionalJUnit4SpringContextTests需要一个TransactionManager，需要在spring的配置文件中指定
	 *
	 * 默认所有的测试用例数据不会提交到数据库，但是可以使用@Rollback(false)控制，
	 * 但是它需要维护一个plm.test.xml的配置文件，用来提供TransactionManager
	 * 
	 * @author Administrator
	 *
	 */
	@ContextConfiguration(locations={"file:D:/A5/Developing/AVIDM_HOME/plm/pdm/springconfig/plm.test.xml"})
	public class GenericTestCase extends AbstractTransactionalJUnit4SpringContextTests{
		
		@BeforeClass
		public static void initTestCase(){
			System.setProperty("AVIDM_HOME", "D:\\A5\\Developing\\AVIDM_HOME");
			MockServletContext servletContext = new MockServletContext("D:/A5/Developing/plm/WebContent/");
			servletContext.setInitParameter(ContextLoader.CONTEXT_CLASS_PARAM, "com.bjsasc.platform.spring.PlatformWebContext");
			ContextLoaderListener listener = new ContextLoaderListener();
			listener.initWebApplicationContext(servletContext);//加载spring
			
//			Helper.getPersistService().find("from User");//加载hibernate
			
			initThreadLocalContext();
		}
		
		/**
		 * 这个方法是根据项目的特点，将当前登录用户设置为系统管理员
		 */
		private static void initThreadLocalContext(){
//			User admin = UserHelper.getService().getUserById("administrator");
//			PersonModel person = new PersonModel();
//			person.setUserIID(admin.getAaUserInnerId());
//			person.setUserName(admin.getName());
//			person.setDomainRef(admin.getDomainInfo().getDomain().getAaDomainRef());
//			person.setDomainName(admin.getDomainInfo().getDomainName());
//			person.setIp("127.0.0.1");
//			ThreadConfigBean.setPtPerson(person);
		}
		
		/**
		 * DataSource默认是Autowire By Type的，如果在spring的配置文件中配置了DataSource，也可以使用@Resource(name="beanid")来进行配置
		 * 由于本项目的数据源是另外配置的，没有使用spring管理，所以需要重写。
		 */
		@Override
		public void setDataSource(DataSource dataSource) {
//			DataSource adpDataSource = AdpDataSourceManager.getDataSource("AvidmSystemDB");
//			super.setDataSource(adpDataSource);
		}
	}

 
