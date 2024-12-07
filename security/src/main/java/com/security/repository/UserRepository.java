package com.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.modal.User;

public interface UserRepository extends JpaRepository<User,Integer>{
	 User findByUsername(String username);
}
