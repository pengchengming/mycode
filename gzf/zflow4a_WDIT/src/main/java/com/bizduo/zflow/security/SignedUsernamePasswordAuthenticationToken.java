package com.bizduo.zflow.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class SignedUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private String requestSignature;  
    private static final long serialVersionUID = 3145548673810647886L;  

	/**
	 * Construct a new token instance with the given principal, credentials, and
	 * signature.
	 * 
	 * @param principal the principal to use
	 * @param credentials the credentials to use
	 * @param signature the signature to use
	 */
  public SignedUsernamePasswordAuthenticationToken(String principal,  
    String credentials, String signature) {  
    super(principal, credentials);  
    this.requestSignature = signature;  
   }  
  public void setRequestSignature(String requestSignature) {  
    this.requestSignature = requestSignature;  
  }  
  public String getRequestSignature() {  
    return requestSignature;  
  } 
}
