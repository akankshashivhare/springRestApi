package com.ubs.creditrisk.zeus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ubs.creditrisk.zeus.entities.User;

public interface UserRepository extends JpaRepository<User,Long>{

}
