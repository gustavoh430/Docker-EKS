package com.project.loginproject.security;

import com.project.loginproject.entities.Role;
import com.project.loginproject.entities.Users;
import com.project.loginproject.repositories.UsersRepository;

import jakarta.transaction.*;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class ImpUserDetailsService implements UserDetailsService {

	@Autowired
	private UsersRepository ur;
	
	

	@SuppressWarnings("unused")
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Users usuario = ur.findByEmail(login);

		
		 Set <GrantedAuthority> grantedAuthorities = new HashSet <> ();
	        for (Role role: usuario.getRoles()) {
	            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
	        }
		
		
		
		
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuario não encontrado!");
		}
		return new User(usuario.getEmail(), usuario.getPassword(), true, true, true, true, grantedAuthorities);
	}

}
