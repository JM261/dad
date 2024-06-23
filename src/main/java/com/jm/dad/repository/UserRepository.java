package com.jm.dad.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jm.dad.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findById(long id);

	User findByUsername(String username);

}
