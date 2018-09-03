package com.bizduo.zflow.domain.table;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**数据库信息**/
@Entity
@Table(name="zsql_dataBase")
public class ZDataBase implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5566383272864312273L;
	private Long id;
	/**字符集*/
	private String characterSet;
	/**数据库*/
	private String database;
	
	@Id //对应数据库中的主键  
	@GeneratedValue(strategy=GenerationType.TABLE,//指定主键生成策略  
		    generator="zsql_dataBase")    //对应生成策略名称  
	@TableGenerator(name="zsql_dataBase", //生成策略名称  
		    pkColumnName="sequence_name",     //主键的列名  
		    pkColumnValue="zsql_dataBase",            //主键的值  
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
	@Column(name="sql_characterSet")
	public String getCharacterSet() {
		return characterSet;
	}
	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}
	@Column(name="sql_database")
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	
	
}
