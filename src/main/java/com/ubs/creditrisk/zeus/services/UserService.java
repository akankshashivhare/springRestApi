package com.ubs.creditrisk.zeus.services;

import java.util.List;
import java.util.Optional;

import com.ubs.creditrisk.zeus.entities.User;


public interface UserService {
	
	List<User> findAll();
	
	Optional<User> findById(long id);

}
