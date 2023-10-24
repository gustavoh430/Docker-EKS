package com.project.loginproject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private ImpUserDetailsService userDetailsService;

	private JwtAuthEntryPoint authEntryPoint;


	public WebSecurityConfig(ImpUserDetailsService userDetailsService, JwtAuthEntryPoint authEntryPoint) {
		this.userDetailsService = userDetailsService;
		this.authEntryPoint = authEntryPoint;

	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.exceptionHandling()
		.authenticationEntryPoint(authEntryPoint)
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeHttpRequests()
		.requestMatchers("/login/signup").permitAll()
		.requestMatchers("/login/signin").permitAll()
		.requestMatchers("/login/authorities").permitAll()
		.requestMatchers("/login/{email}").authenticated()
		.requestMatchers("/login/{email}").hasAuthority("ADMIN")
		.and()
		.httpBasic();

		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).build();
	}

	@Bean
	public JWTAuthenticationFilter jwtAuthenticationFilter() {
		return new JWTAuthenticationFilter();
	}
}
