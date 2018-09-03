package com.bizduo.zflow.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class Configure {
	private String skin;
	private String parts;
	
	protected static ResourceBundle Configure_Resource = ResourceBundle.getBundle("configure", Locale.getDefault());

	public Configure() {
		super();
		// TODO Auto-generated constructor stub
		skin =Configure_Resource.getString("skin").trim();
		parts =Configure_Resource.getString("parts").trim();
		
	}
	
    public static String getConfigure(String name){
    	return Configure_Resource.getString(name).trim();
    }
    
	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public String getParts() {
		return parts;
	}

	public void setParts(String parts) {
		this.parts = parts;
	} 


}
