package com.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.modal.User;

public interface UserRepository extends JpaRepository<User,Integer> {
	public User findByUsername(String username);
}
