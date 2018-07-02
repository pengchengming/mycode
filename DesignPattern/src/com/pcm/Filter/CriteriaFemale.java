/**
 * 
 */
package com.pcm.Filter;

import java.util.ArrayList;
import java.util.List;

/**  
* @Package com.pcm.Filter 
* @Title: CriteriaMale.java   
* @Description: 创建实现了Criteria接口的实体类  
* @author pcm  
* @date 2018年7月2日 下午2:56:41
* @version V1.0  
*/
public class CriteriaFemale implements Criteria {

	@Override
	public List<Person> meetCriteria(List<Person> persons) {
		List<Person> femalePersons = new ArrayList<Person>();
		for (Person person : persons) {
			if (person.getGender().equalsIgnoreCase("FEMALE")) {
				femalePersons.add(person);
			}
		}
		return femalePersons;
	}

}
