package com.example.ad.controller;

import com.example.ad.model.User;
import com.example.ad.repository.UserRepository;
import com.example.ad.service.UserService;
import com.example.ad.security.JwtUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {


private static final Logger logger =
        LoggerFactory.getLogger(UserController.class);

@Autowired
private UserService userService;

@Autowired
private UserRepository userRepository;

@Autowired
private PasswordEncoder passwordEncoder;

@Autowired
private JwtUtil jwtUtil;

// START: signup
@PostMapping("/signup")
public User signup(
        @RequestParam("name") String name,
        @RequestParam("dob") String dob,
        @RequestParam("mobileNo") String mobileNo,
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        @RequestParam("confirmPassword") String confirmPassword,
        @RequestParam(value = "profilePhoto", required = false)
        MultipartFile profilePhoto
) throws Exception {

    logger.info("Signup request received for email: {}", email);

    if (!password.equals(confirmPassword)) {

        logger.warn(
                "Password and Confirm Password mismatch for email: {}",
                email);

        throw new Exception("Passwords do not match!");
    }

    User user = new User();
    user.setName(name);
    user.setDob(dob);
    user.setMobileNo(mobileNo);
    user.setEmail(email);

    // Encrypt password before saving
    user.setPassword(passwordEncoder.encode(password));

    if (profilePhoto != null && !profilePhoto.isEmpty()) {

        logger.info(
                "Profile photo received during signup for email: {}",
                email);

        user = userService.saveUser(user, profilePhoto);

    } else {

        logger.info(
                "No profile photo received during signup for email: {}",
                email);

        user = userService.saveUserWithoutPhoto(user);
    }

    logger.info(
            "User registered successfully with email: {}",
            email);

    return user;
}
// END: signup

// START: login
@PostMapping("/login")
public ResponseEntity<Map<String, String>> login(
        @RequestBody User user) {

    logger.info(
            "Login request received for email: {}",
            user.getEmail());

    Optional<User> foundUser =
            userService.findByEmail(user.getEmail());

    if (foundUser.isPresent()
            && passwordEncoder.matches(
                    user.getPassword(),
                    foundUser.get().getPassword())) {

        logger.info(
                "Login successful for email: {}",
                user.getEmail());

        String token =
                jwtUtil.generateToken(
                        foundUser.get().getEmail());

        Map<String, String> response =
                new HashMap<>();

        String photoUrl =
                "http://localhost:8080/uploads/"
                        + foundUser.get().getProfilePhoto();

        response.put("token", token);
        response.put(
                "id",
                String.valueOf(foundUser.get().getId()));
        response.put(
                "name",
                foundUser.get().getName());
        response.put(
                "email",
                foundUser.get().getEmail());
        response.put(
                "role",
                foundUser.get().getRole());
        response.put(
                "dob",
                foundUser.get().getDob());
        response.put(
                "mobile",
                foundUser.get().getMobileNo());
        response.put(
                "photo",
                photoUrl);

        return ResponseEntity.ok(response);

    } else {

        logger.warn(
                "Login failed due to invalid credentials for email: {}",
                user.getEmail());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        Map.of(
                                "message",
                                "Invalid credentials!"
                        )
                );
    }
}
// END: login

@GetMapping("/dashboard")
public ResponseEntity<String> dashboard() {

    logger.info("Dashboard API accessed successfully");

    return ResponseEntity.ok(
            "Dashboard accessed successfully with valid JWT Token");
}


}
