package com.bizduo.zflow.wrapper;

import java.io.Serializable;

import com.bizduo.zflow.domain.sys.Role;

public class RoleWrapper implements Serializable {
	private static final long serialVersionUID = -7751199594984970987L;
	private Role role;
	private boolean checked = false;
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
}
