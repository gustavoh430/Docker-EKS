package com.project.loginproject.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.loginproject.dto.SetAuthoritiesDTO;
import com.project.loginproject.entities.Role;
import com.project.loginproject.entities.Users;
import com.project.loginproject.repositories.RolesRepository;
import com.project.loginproject.repositories.UsersRepository;
import com.project.loginproject.services.exceptions.ResourceNotFoundException;

@Service
public class UsersService {

	@Autowired
	UsersRepository userRepository;
	@Autowired
	RolesRepository rolesRepository;

	public Users findByEmail(String email) {
		Optional<Users> obj = Optional.of(userRepository.findByEmail(email));
		return obj.orElseThrow(() -> new ResourceNotFoundException(email));
	}

	public Users findByPassword(String password) {
		Optional<Users> obj = Optional.of(userRepository.findByPassword(password));
		return obj.orElseThrow(() -> new ResourceNotFoundException(password));
	}

	public boolean nonNullFields(String users) {
		Boolean obj = users.isBlank();
		return obj;

	}

	public Boolean existsByEmail(String username) {
		return userRepository.existsByEmail(username);

	}

	public Users save(Users user) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Role roles = rolesRepository.findByName("USER").get();
		user.setRoles(Collections.singletonList(roles));
		return userRepository.save(user);

	}

	public void SetAuthoritiesService(SetAuthoritiesDTO authorities) {

		List<Role> listRoles = new ArrayList<>();

		try {
			Role roles = rolesRepository.findByName(authorities.getRole()).get();
			Users user = Optional.of(userRepository.findByEmail(authorities.getEmail())).get();

			listRoles.add(roles);
			user.setRoles(listRoles);
			userRepository.save(user);

		} catch (Exception e) {

			throw new ResourceNotFoundException("Email or Role does not exist");

		}

	}

	public Role saveRole(Role roles) {
		return rolesRepository.save(roles);

	}

}
