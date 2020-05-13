package com.ubs.creditrisk.zeus.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ubs.creditrisk.zeus.entities.User;
import com.ubs.creditrisk.zeus.exceptions.UserNotFoundException;
import com.ubs.creditrisk.zeus.model.ApiSuccess;
import com.ubs.creditrisk.zeus.services.UserService;

@RestController
@RequestMapping("/user")
public class UserContoller {
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public List<User>findAll(){
		
		return userService.findAll();
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable("id")long id){
		Optional<User>isUser=userService.findById(id);
		if(!isUser.isPresent()) {
			throw new UserNotFoundException("User is not found by given id "+id);
		}
		ApiSuccess<User> apiSuccess=new ApiSuccess<>();
		apiSuccess.setStatus(HttpStatus.OK);
		apiSuccess.setMessage("User details found");
		apiSuccess.setData(isUser.get());
		return new ResponseEntity<>(apiSuccess, new HttpHeaders(), apiSuccess.getStatus());
	}

}
