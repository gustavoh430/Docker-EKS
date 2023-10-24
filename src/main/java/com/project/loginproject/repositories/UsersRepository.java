package com.project.loginproject.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.project.loginproject.entities.Users;
public interface UsersRepository extends JpaRepository<Users, Long>{
	
	 Users findByEmail(String username);
	 Users findByPassword(String username);
	 Boolean existsByEmail(String username);
	 
	 
	


}
