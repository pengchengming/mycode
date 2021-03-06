package com.pcm.Interpreter;

/**  
* @Package com.pcm.Interpreter 
* @Title: OrExpression.java   
* @Description: 创建实现了上述接口的实体类OrExpression。  
* @author pcm  
* @date 2018年7月9日 下午8:08:55
* @version V1.0  
*/
public class OrExpression implements Expression {
	private Expression expr1 = null;
	private Expression expr2 = null;

	public OrExpression(Expression expr1, Expression expr2) {
		this.expr1 = expr1;
		this.expr2 = expr2;
	}

	@Override
	public boolean interpret(String context) {
		return expr1.interpret(context) || expr2.interpret(context);
	}

}
