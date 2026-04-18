package com.example.ad.service;

import com.example.ad.model.User;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface UserService {
//	START: signup
    User saveUser(User user, MultipartFile profilePhoto) throws Exception;
    User saveUserWithoutPhoto(User user) throws Exception;
//	END: signup    
//	START: login
    Optional<User> findByEmail(String email);
    //	END: login 
}
