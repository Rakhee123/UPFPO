package com.example.newupfpo1.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.newupfpo1.models.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	
	User findByUserName(String userName);

}
