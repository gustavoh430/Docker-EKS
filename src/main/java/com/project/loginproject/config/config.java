package com.project.loginproject.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import com.project.loginproject.entities.Role;
import com.project.loginproject.repositories.RolesRepository;

@Configuration
public class config implements CommandLineRunner {

	@Autowired
	private RolesRepository rolesRepository;

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub

		
		  Set<Role> role = new HashSet<>(); 
		  Role role1 = new Role(1, "ADMIN"); Role
		  role2 = new Role(2, "USER");
		  
		  role.add(role1); 
		  role.add(role2); 
		  rolesRepository.save(role1);
		  rolesRepository.save(role2);
		  


	}
}
