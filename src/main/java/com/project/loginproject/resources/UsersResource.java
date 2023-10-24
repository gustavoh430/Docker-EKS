package com.project.loginproject.resources;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.project.loginproject.dto.AuthResponseDTO;
import com.project.loginproject.dto.LoginDto;
import com.project.loginproject.dto.SetAuthoritiesDTO;
import com.project.loginproject.entities.Users;
import com.project.loginproject.repositories.RolesRepository;
import com.project.loginproject.repositories.UsersRepository;
import com.project.loginproject.security.JWTGenerator;
import com.project.loginproject.services.UsersService;
import com.project.loginproject.services.exceptions.EmptyFieldsException;
import com.project.loginproject.services.exceptions.ExistingEmailException;


@RestController
@RequestMapping("/login/")
public class UsersResource {

	private JWTGenerator jwtGenerator;

	@Autowired
	private UsersService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	public UsersResource(AuthenticationManager authenticationManager, UsersRepository userRepository,
			RolesRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
		this.authenticationManager = authenticationManager;
		this.jwtGenerator = jwtGenerator;
	}


	@PostMapping("signup")
	public ResponseEntity<String> insert(@RequestBody Users obj) {

		if (userService.nonNullFields(obj.getEmail())) {

			throw new EmptyFieldsException("Email");
		}

		else if (userService.nonNullFields(obj.getName())) {

			throw new EmptyFieldsException("Name");

		}

		else if (userService.nonNullFields(obj.getAge())) {

			throw new EmptyFieldsException("Age");
		}

		else if (userService.nonNullFields(obj.getPassword())) {

			throw new EmptyFieldsException("Password");
		}

		if (userService.existsByEmail(obj.getEmail())) {
			throw new ExistingEmailException();
		}

		userService.save(obj);

		return ResponseEntity.ok().body("User registered successfully");

	}

	@PostMapping("signin")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDto users) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(users.getEmail(), users.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtGenerator.generateToken(authentication);

		return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);

	}

	@GetMapping("{email}")
	public ResponseEntity<Users> RequestUsers(@PathVariable String email) {

		Users obj = userService.findByEmail(email);
		return ResponseEntity.ok().body(obj);
	}

	@PostMapping("authorities")
	public ResponseEntity<String> setAuthorities(@RequestBody SetAuthoritiesDTO authorities) {

		userService.SetAuthoritiesService(authorities);

		return ResponseEntity.ok().body ("Authority has been changed successfully");

	}
}
