package com.project.loginproject.dto;

public class SetAuthoritiesDTO {
	
	String email;
	String role;
	
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public String toString() {
		return "SetAuthoritiesDTO [email=" + email + ", role=" + role + "]";
	}
	
	
	

}
