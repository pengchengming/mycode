package com.bizduo.zflow.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class CustomAuthenticationToken extends
		UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 5414106440823275021L;

	public CustomAuthenticationToken(String principal, String credentials,
			Integer questionId, String answer) {
		super(principal, credentials);
		this.answer = answer;
		this.questionId = questionId;
	}

	private String answer;
	private Integer questionId;

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

}
