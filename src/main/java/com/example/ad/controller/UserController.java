package com.example.ad.controller;

import com.example.ad.model.User;
import com.example.ad.repository.UserRepository;
import com.example.ad.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.ad.security.JwtUtil;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
//	START: signup
    @PostMapping("/signup")
    public User signup(
            @RequestParam("name") String name,
            @RequestParam("dob") String dob,
            @RequestParam("mobileNo") String mobileNo,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam(value = "profilePhoto", required = false) MultipartFile profilePhoto // Make it optional
    ) throws Exception {
        if (!password.equals(confirmPassword)) {
            throw new Exception("Passwords do not match!");
        }

        User user = new User();
        user.setName(name);
        user.setDob(dob);
        user.setMobileNo(mobileNo);
        user.setEmail(email);
        //user.setPassword(password);
        user.setPassword(passwordEncoder.encode(password));

        // If profile photo is present, save it
        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            user = userService.saveUser(user, profilePhoto);  // Save user along with photo
        } else {
            user = userService.saveUserWithoutPhoto(user);  // Save user without photo
        }

        return user;
    }
//	END: signup  
//	START: login
    //@PostMapping("/api/users/login")
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody User user) {
//        Optional<User> foundUser = userService.findByEmail(user.getEmail());
//
//        if (foundUser.isPresent() && foundUser.get().getPassword().equals(user.getPassword())) {
//            return ResponseEntity.ok("Login successful!");  // 200 OK
//        } else {
//            return ResponseEntity.status(401).body("Invalid credentials(SB)");  // 401 Unauthorized
//        }
//    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        Optional<User> foundUser = userService.findByEmail(user.getEmail());

        if (foundUser.isPresent() &&
        	    passwordEncoder.matches(
        	        user.getPassword(),
        	        foundUser.get().getPassword()
        	    )) {
        	String token = jwtUtil.generateToken(foundUser.get().getEmail());
            // Prepare the response data with name and email
            Map<String, String> response = new HashMap<>();
            String photoUrl = "http://localhost:8080/uploads/" + foundUser.get().getProfilePhoto();
//            To send data 
            response.put("token", token);
            response.put("name", foundUser.get().getName());  // Add user name
            response.put("email", foundUser.get().getEmail());  // Add user email
            response.put("dob", foundUser.get().getDob());
            response.put("mobile", foundUser.get().getMobileNo());
//            response.put("photo", foundUser.get().getProfilePhoto());
            response.put("photo", photoUrl);  // Include full photo URL
            
            
            return ResponseEntity.ok(response);  // Send name and email with 200 OK status
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials!"));  // 401 Unauthorized
        }
    }
//	END: login 
}



    

