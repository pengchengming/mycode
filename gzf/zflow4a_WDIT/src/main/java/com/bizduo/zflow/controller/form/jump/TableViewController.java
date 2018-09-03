package com.bizduo.zflow.controller.form.jump;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizduo.zflow.domain.form.Form;
import com.bizduo.zflow.domain.table.ConditionSelect;
import com.bizduo.zflow.json.ConditionselectJson;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.table.IConditionSelectService;

@Controller
@RequestMapping("/tableView")
public class TableViewController {
	@Autowired
	private IFormService formService;
	@Autowired
	private IConditionSelectService conditionSelectService;
	
	/**
	 * 显示查询
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/tableViewSelect.do")
	@ResponseBody
	public List<ConditionselectJson> showConditionSelect(
			@RequestParam(value = "formCode", required = true) String formCode
			) throws Exception{
		Form form= (Form)this.formService.getFormByCode(formCode);
		List<ConditionselectJson>  conditionselectJsonList=new ArrayList<ConditionselectJson>();
		if(form!=null){
			List<ConditionSelect> conditionSelectFieldList= this.conditionSelectService.getConditionSelectByZtableId(form.getZtable().getId());
			for(ConditionSelect conditionSelect: conditionSelectFieldList){
				ConditionselectJson conditionselectJson=new ConditionselectJson();
				conditionselectJson.setId(conditionSelect.getId());
				conditionselectJson.setName(conditionSelect.getName());
				conditionselectJson.setDescription(conditionSelect.getDescription());
				if(conditionSelect.getCreateDate()!=null){
					 SimpleDateFormat dataFm=new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
					 String data=dataFm.format(conditionSelect.getCreateDate());
					 conditionselectJson.setCreateDate(data);
				}
				conditionselectJsonList.add(conditionselectJson);
			}
		}
		return conditionselectJsonList;
	}
}
