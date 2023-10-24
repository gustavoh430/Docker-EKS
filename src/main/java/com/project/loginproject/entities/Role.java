package com.project.loginproject.entities;

import java.util.ArrayList;
import java.util.List;


import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;


@Entity
@Table(name = "tb_role")
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String name;

	
	@JsonIgnore
	@ManyToMany(mappedBy = "roles")
	private List<Users> users = new ArrayList<Users>();

	public Role(int id, String comum) {
		this.id = id;
		this.name = comum;
	}

	public Role() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Users> getUsers() {
		return users;
	}

	public void setUsers(List<Users> users) {
		this.users = users;
	}

	@Override
	public String getAuthority() {
		return this.name;
	}

}
