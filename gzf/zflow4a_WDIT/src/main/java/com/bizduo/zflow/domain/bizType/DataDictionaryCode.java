package com.bizduo.zflow.domain.bizType;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**代码**/
@Entity
@Table(name="sys_DataDictionary_Code")
public class DataDictionaryCode  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6065416988284632796L;
	private Long id; 
	private String code  ;//代码; 
	private  String display;//  描述; 
	private DataDictionaryType dataDictionaryType; //类别 
	private String dataDictionaryTypeCode;

	private String type ;//(单选多选); 
	private Boolean  isupdate ;//是否允许维护
	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="dm_DataDictionary_Code")    //对应生成策略名称  
    @TableGenerator(name="dm_DataDictionary_Code", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="dm_DataDictionary_Code",            //主键的值  
                    valueColumnName="next_val", //生成的值  列名  
                    table="id_table",            //生成的表名  
                    initialValue=1000,//   [主键初识值，默认为0。]  
                    //catalog schema   指定表所在的目录名或是数据库名。  
                    allocationSize=1)  //主键每次增加的大小,默认为50  
    
	@Column
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Column
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dataDictionaryType_id")
	public DataDictionaryType getDataDictionaryType() {
		return dataDictionaryType;
	}
	public void setDataDictionaryType(DataDictionaryType dataDictionaryType) {
		this.dataDictionaryType = dataDictionaryType;
	}
	@Column(name="code_type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column
	public Boolean getIsupdate() {
		return isupdate;
	}
	public void setIsupdate(Boolean isupdate) {
		this.isupdate = isupdate;
	}
	 
	public String getDataDictionaryTypeCode() {
		return dataDictionaryTypeCode;
	}
	public void setDataDictionaryTypeCode(String dataDictionaryTypeCode) {
		this.dataDictionaryTypeCode = dataDictionaryTypeCode;
	}
}
