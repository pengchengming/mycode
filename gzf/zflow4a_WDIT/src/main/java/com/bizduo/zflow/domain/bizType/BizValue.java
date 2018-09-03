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
@Entity
@Table(name="dm_biz_value")
public  class   BizValue  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2687234339357222318L;
	private Long id;
	private BizType bizType;//类别
	private String displayValue;//值
	private String remark;//备注
	private boolean disable =false;//禁用标识
	@Id //对应数据库中的主键  
    @GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
                    generator="dm_biz_value")    //对应生成策略名称  
    @TableGenerator(name="dm_biz_value", //生成策略名称  
                    pkColumnName="sequence_name",     //主键的列名  
                    pkColumnValue="dm_biz_value",            //主键的值  
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "biz_type_id")
	public BizType getBizType() {
		return bizType;
	}
	public void setBizType(BizType bizType) {
		this.bizType = bizType;
	}
	@Column(name="display_value")
	public String getDisplayValue() {
		return displayValue;
	}
	@Column
	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
	@Column
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
}
