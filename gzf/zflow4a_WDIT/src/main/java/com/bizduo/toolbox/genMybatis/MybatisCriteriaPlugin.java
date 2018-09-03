package com.bizduo.toolbox.genMybatis;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.internal.util.StringUtility;

public class MybatisCriteriaPlugin extends PluginAdapter{

	/** 数据库类型 */
	private String databaseType;
	
	private boolean commonFile=false;
	
	public boolean validate(List<String> warnings) {
		databaseType = context.getJdbcConnectionConfiguration().getDriverClass();
		
		if (stringHasValue(properties.getProperty("commonFile"))) { //$NON-NLS-1$
			commonFile = StringUtility.isTrue(properties.getProperty("commonFile"));
		} else {
			commonFile = false;
		}
		return true;
	}


	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		
		XmlElement Where_Clause=new XmlElement("sql");
		Where_Clause.addAttribute(new Attribute("id", "Example_Where_Clause"));	
		StringBuilder sb = new StringBuilder();
		XmlElement dynamicElement = new XmlElement("trim"); //$NON-NLS-1$
		dynamicElement.addAttribute(new Attribute("prefix", "where")); //$NON-NLS-1$ //$NON-NLS-2$
		dynamicElement.addAttribute(new Attribute("prefixOverrides", "and|or")); //$NON-NLS-1$ //$NON-NLS-2$
		Where_Clause.addElement(dynamicElement);

		for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
			XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
			String va = introspectedColumn.getJavaProperty("params.") + " != null";
			isNotNullElement.addAttribute(new Attribute("test", va)); //$NON-NLS-1$ //$NON-NLS-2$
			
			dynamicElement.addElement(isNotNullElement);
			sb.setLength(0);
			sb.append(" and "); //$NON-NLS-1$
			sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
			sb.append(" = "); //$NON-NLS-1$
			
			
			sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "params."));
			
			isNotNullElement.addElement(new TextElement(sb.toString()));
		}
		
		
		document.getRootElement().getElements().add(2, Where_Clause);
		
		
		//创建查询条件
		document.getRootElement().getElements().add(3, createSelectByParams(introspectedTable));
		document.getRootElement().getElements().add(4, createSelectCountByParams(introspectedTable));
		document.getRootElement().getElements().add(5, createSelectListByParams(introspectedTable));
		
		return true;
	}

	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		if (databaseType.contains("oracle")) {
			XmlElement oracleHeadIncludeElement = new XmlElement("include");
			oracleHeadIncludeElement.addAttribute(new Attribute("refid", "common.Pagination_Head"));
			// 在第一个地方增加
			element.addElement(0, oracleHeadIncludeElement);

			
			//增加排序
			XmlElement orderIncludeElement = new XmlElement("if");
			orderIncludeElement.addAttribute(new Attribute("test", "orderParam != null"));
			orderIncludeElement.addElement(new TextElement("order by ${orderParam}"));
			element.addElement(element.getElements().size(), orderIncludeElement);
			
			
			XmlElement oracleFootIncludeElement = new XmlElement("include");
			oracleFootIncludeElement.addAttribute(new Attribute("refid", "common.Pagination_Foot"));
			// 在最后增加
			element.addElement(element.getElements().size(), oracleFootIncludeElement);
		} else if (databaseType.contains("mysql")) {
			XmlElement mysqlLimitIncludeElement = new XmlElement("include");
			mysqlLimitIncludeElement.addAttribute(new Attribute("refid", "common.Mysql_Pagination_Limit"));
			// 在最后增加
			element.addElement(element.getElements().size(), mysqlLimitIncludeElement);
		}
		return true;
	}

	@Override
	public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		if (databaseType.contains("oracle")) {
			XmlElement oracleHeadIncludeElement = new XmlElement("include");
			oracleHeadIncludeElement.addAttribute(new Attribute("refid", "common.Pagination_Head"));
			// 在第一个地方增加
			element.addElement(0, oracleHeadIncludeElement);

			
			//增加排序
			XmlElement orderIncludeElement = new XmlElement("if");
			orderIncludeElement.addAttribute(new Attribute("test", "orderParam != null"));
			orderIncludeElement.addElement(new TextElement("order by ${orderParam}"));
			element.addElement(element.getElements().size(), orderIncludeElement);
			
			XmlElement oracleFootIncludeElement = new XmlElement("include");
			oracleFootIncludeElement.addAttribute(new Attribute("refid", "common.Pagination_Foot"));
			// 在最后增加
			element.addElement(element.getElements().size(), oracleFootIncludeElement);
		} else if (databaseType.contains("mysql")) {
			XmlElement mysqlLimitIncludeElement = new XmlElement("include");
			mysqlLimitIncludeElement.addAttribute(new Attribute("refid", "common.Mysql_Pagination_Limit"));
			// 在最后增加
			element.addElement(element.getElements().size(), mysqlLimitIncludeElement);
		}
		return true;
	}	
	
	
	/**
	 * 创建 selectByParams 查询条件语句 
	 * @author Administrator
	 * @param introspectedTable
	 * @return
	 */
	private XmlElement createSelectByParams(IntrospectedTable introspectedTable) {
		String tableName= introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();
	
		StringBuffer sb=new StringBuffer();
		
		XmlElement selectCriteria=new XmlElement("select");
		selectCriteria.addAttribute(new Attribute("id", "selectByParams"));	
		selectCriteria.addAttribute(new Attribute("resultMap", "BaseResultMap"));	
		
		selectCriteria.addElement(new TextElement("select"));
		
		XmlElement baseColumnIncludeElement = new XmlElement("include");
		baseColumnIncludeElement.addAttribute(new Attribute("refid", "Base_Column_List"));
		selectCriteria.addElement(baseColumnIncludeElement);
		
		sb.append("from ").append(tableName);
		selectCriteria.addElement(new TextElement(sb.toString()));
		
		XmlElement oracleWhereIncludeElement = new XmlElement("include");
		oracleWhereIncludeElement.addAttribute(new Attribute("refid", "Example_Where_Clause"));
		selectCriteria.addElement(oracleWhereIncludeElement);

		
		return selectCriteria;
	}
	
	
	
	/**
	 * 创建 selectCountByParams 条件查询语句
	 * @author Administrator
	 * @param introspectedTable
	 * @return
	 */
	private XmlElement createSelectCountByParams(IntrospectedTable introspectedTable) {
		String tableName= introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();
	
		StringBuffer sb=new StringBuffer();
		
		XmlElement selectCriteria=new XmlElement("select");
		selectCriteria.addAttribute(new Attribute("id", "selectCountByParams"));	
		selectCriteria.addAttribute(new Attribute("resultType", "int"));	
		
		sb.append("select").append(" count(*)").append(" from ").append(tableName);
		selectCriteria.addElement(new TextElement(sb.toString()));
		
		XmlElement oracleWhereIncludeElement = new XmlElement("include");
		oracleWhereIncludeElement.addAttribute(new Attribute("refid", "Example_Where_Clause"));
		selectCriteria.addElement(oracleWhereIncludeElement);
		
		return selectCriteria;
	}
	
	
	
	/**
	 * 创建 selectListByParams 条件查询语句
	 * @author Administrator
	 * @param introspectedTable
	 * @return
	 */
	private XmlElement createSelectListByParams(IntrospectedTable introspectedTable) {
		String tableName= introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();
	
		StringBuffer sb=new StringBuffer();
		
		XmlElement selectCriteria=new XmlElement("select");
		selectCriteria.addAttribute(new Attribute("id", "selectListByParams"));	
		selectCriteria.addAttribute(new Attribute("resultType", "list"));
		selectCriteria.addAttribute(new Attribute("resultMap", "BaseResultMap"));	
		
		if (databaseType.contains("oracle")) {
			XmlElement oracleHeadIncludeElement = new XmlElement("include");
			oracleHeadIncludeElement.addAttribute(new Attribute("refid", "common.Pagination_Head"));
			selectCriteria.addElement(oracleHeadIncludeElement);
			
			selectCriteria.addElement(new TextElement("select"));
			XmlElement baseColumnIncludeElement = new XmlElement("include");
			baseColumnIncludeElement.addAttribute(new Attribute("refid", "Base_Column_List"));
			selectCriteria.addElement(baseColumnIncludeElement);
			
			sb.append("from ").append(tableName);
			selectCriteria.addElement(new TextElement(sb.toString()));
			
			XmlElement oracleWhereIncludeElement = new XmlElement("include");
			oracleWhereIncludeElement.addAttribute(new Attribute("refid", "Example_Where_Clause"));
			selectCriteria.addElement(oracleWhereIncludeElement);
	
			
			//增加排序
			XmlElement orderIncludeElement = new XmlElement("if");
			orderIncludeElement.addAttribute(new Attribute("test", "orderParam != null"));
			orderIncludeElement.addElement(new TextElement("order by ${orderParam}"));
			selectCriteria.addElement(orderIncludeElement);
			
			
			XmlElement oracleFootIncludeElement = new XmlElement("include");
			oracleFootIncludeElement.addAttribute(new Attribute("refid", "common.Pagination_Foot"));
			selectCriteria.addElement(oracleFootIncludeElement);
		}
		else if (databaseType.contains("mysql")) {
			XmlElement mysqlLimitIncludeElement = new XmlElement("include");
			mysqlLimitIncludeElement.addAttribute(new Attribute("refid", "common.Mysql_Pagination_Limit"));
			selectCriteria.addElement(mysqlLimitIncludeElement);
		}
		return selectCriteria;
	}
	
	
	/**
	 * 增加CommonMapper.xml文件
	 */
	@Override
	public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
		if (!commonFile){
			Document document = new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID, XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
			XmlElement answer = new XmlElement("mapper"); //$NON-NLS-1$
			document.setRootElement(answer);
			answer.addAttribute(new Attribute("namespace", //$NON-NLS-1$
					"common"));
	
			if (databaseType.contains("oracle")) {
				answer.addElement(getOracleHead());
				answer.addElement(getOracleFoot());
			} else if (databaseType.contains("mysql")) {
				answer.addElement(getMysqlLimit());
			}
	
			GeneratedXmlFile gxf = new GeneratedXmlFile(document, properties.getProperty("fileName", "CommonMapper.xml"), //$NON-NLS-1$ //$NON-NLS-2$
					context.getSqlMapGeneratorConfiguration().getTargetPackage(), //$NON-NLS-1$
					context.getSqlMapGeneratorConfiguration().getTargetProject(), //$NON-NLS-1$
					false);
	
			List<GeneratedXmlFile> files = new ArrayList<GeneratedXmlFile>(1);
			files.add(gxf);
			commonFile=true;
			return files;
		}
		else{
			return null;
		}
	}

	private XmlElement getOracleHead() {
		XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$

		answer.addAttribute(new Attribute("id", "Pagination_Head")); //$NON-NLS-1$

		XmlElement outerisNotEmptyElement = new XmlElement("if");
		outerisNotEmptyElement.addAttribute(new Attribute("test", "startIndex != null and endIndex != null"));
		outerisNotEmptyElement.addElement(new TextElement("<![CDATA[ select * from ( select row_.*, rownum rn from ( ]]>"));
		answer.addElement(outerisNotEmptyElement);
		return answer;
	}

	private XmlElement getOracleFoot() {
		XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$

		answer.addAttribute(new Attribute("id", "Pagination_Foot")); //$NON-NLS-1$

		XmlElement outerisNotEmptyElement = new XmlElement("if");
		outerisNotEmptyElement.addAttribute(new Attribute("test", "startIndex != null and endIndex != null"));
		outerisNotEmptyElement.addElement(new TextElement(
				"<![CDATA[ ) row_ where rownum <= #{endIndex} ) where rn > #{startIndex} ]]>"));
		answer.addElement(outerisNotEmptyElement);
		return answer;
	}

	private XmlElement getMysqlLimit() {
		XmlElement answer = new XmlElement("sql"); //$NON-NLS-1$

		answer.addAttribute(new Attribute("id", "Mysql_Pagination_Limit")); //$NON-NLS-1$

		XmlElement outerisNotEmptyElement = new XmlElement("if");
		outerisNotEmptyElement.addAttribute(new Attribute("test", "mysqlOffset != null and mysqlLength != null"));
		outerisNotEmptyElement.addElement(new TextElement("<![CDATA[ limit #{mysqlOffset} , #{mysqlLength} ]]>"));
		answer.addElement(outerisNotEmptyElement);
		return answer;
	}
}
