package com.project.loginproject.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.loginproject.entities.Role;

public interface RolesRepository extends JpaRepository<Role, String> {

	Optional<Role> findByName(String name);

}
