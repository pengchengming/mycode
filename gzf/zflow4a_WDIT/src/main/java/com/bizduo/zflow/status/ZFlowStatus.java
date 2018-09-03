package com.bizduo.zflow.status;

/**
 * 工作流的静态常量字段
 * @author lm
 */
public class ZFlowStatus {
	/**
	 * 性别：男
	 **/
	public static final int SEX_MALE = 1;
	/**
	 * 性别：女
	 **/
	public static final int SEX_FEMALE = 0;
	/**
	 * 是否删除：否
	 **/
	public static final int ISDELETE_NO = 0;
	/**
	 * 是否删除：是
	 **/
	public static final int ISDELETE_YES = 1;
	/**
	 * 设置商店LOGO的宽
	 */
	public static final Integer LOGOWIDTH = 167;
	/**
	 * 设置商店LOGO的高
	 */
	public static final Integer LOGOHEIGHT = 187;
	
	/**
	 * FormService中需要设置的几个常量值,用来判断前台传
	 * 过来的JSON数据中所对应的Form对象中的属性名
	 */
	public static final String ATTRDATA = "attrData";
	public static final String FORMHTML = "formHtml";
	public static final String PROPERTY = "Property";
	public static final String TABLELIST = "tableList";
	public static final String ISTEMPLATE = "isTemplate";
	public static final String TEMPLATE = "template";
	public static final String TABLEPROPERTYLIST = "tablePropertyList";

}

