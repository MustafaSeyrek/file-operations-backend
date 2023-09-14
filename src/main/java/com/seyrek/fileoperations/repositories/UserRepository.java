package com.seyrek.fileoperations.repositories;

import com.seyrek.fileoperations.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}

