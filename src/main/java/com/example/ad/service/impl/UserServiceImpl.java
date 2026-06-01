package com.example.ad.service.impl;

import com.example.ad.model.User;
import com.example.ad.repository.UserRepository;
import com.example.ad.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    // Define the directory where files will be uploaded
    private final String uploadDir = "uploads/";

    // START: signup
    @Override
    public User saveUser(User user, MultipartFile profilePhoto) throws IOException {

        logger.info("User registration started for email: {}", user.getEmail());

        // Get the absolute path of the uploads directory
        Path path = Paths.get(uploadDir).toAbsolutePath();
        File dir = path.toFile();

        // Ensure the directory exists, create it if not
        if (!dir.exists()) {

            logger.info("Upload directory does not exist. Creating directory: {}", path);

            boolean created = dir.mkdirs();

            if (!created) {

                logger.error("Failed to create upload directory: {}", uploadDir);

                throw new IOException(
                        "Failed to create upload directory: " + uploadDir);
            }

            logger.info("Upload directory created successfully");
        }

        // Check if the profile photo is not empty
        if (profilePhoto != null && !profilePhoto.isEmpty()) {

            logger.info("Profile photo received: {}",
                    profilePhoto.getOriginalFilename());

            // Validate the file size (5MB max)
            if (profilePhoto.getSize() > 5000000) {

                logger.warn(
                        "File size exceeded limit for user: {}, Size: {} bytes",
                        user.getEmail(),
                        profilePhoto.getSize());

                throw new IOException(
                        "File size is too large! Max size is 5MB.");
            }

            // Validate the file type
            String contentType = profilePhoto.getContentType();

            if (contentType == null ||
                    !contentType.startsWith("image/")) {

                logger.warn(
                        "Invalid file type uploaded by user: {}, Content Type: {}",
                        user.getEmail(),
                        contentType);

                throw new IOException(
                        "Only image files are allowed.");
            }

            // Generate unique filename
            String fileName =
                    System.currentTimeMillis() + "-"
                            + profilePhoto.getOriginalFilename();

            Path filePath = path.resolve(fileName);

            try {

                logger.info("Uploading file: {}", fileName);

                profilePhoto.transferTo(filePath.toFile());

                user.setProfilePhoto(fileName);

                logger.info(
                        "Profile photo uploaded successfully: {}",
                        fileName);

            } catch (IOException e) {

                logger.error(
                        "Failed to upload file: {}",
                        fileName,
                        e);

                throw new IOException(
                        "Could not save file: " + fileName,
                        e);
            }
        } else {

            logger.info(
                    "No profile photo uploaded for email: {}",
                    user.getEmail());
        }

        try {

            logger.info(
                    "Saving user details into database for email: {}",
                    user.getEmail());
            if (user.getRole() == null) {
                user.setRole("USER");
            }
            User savedUser = userRepository.save(user);

            logger.info(
                    "User registered successfully. User ID: {}, Email: {}",
                    savedUser.getId(),
                    savedUser.getEmail());

            return savedUser;

        } catch (Exception e) {

            logger.error(
                    "Database error while saving user with email: {}",
                    user.getEmail(),
                    e);

            throw e;
        }
    }

    // Save user without photo
    @Override
    public User saveUserWithoutPhoto(User user) {

        logger.info(
                "Saving user without profile photo. Email: {}",
                user.getEmail());

        try {
        	if(user.getRole() == null) {
        	    user.setRole("USER");
        	}

            User savedUser = userRepository.save(user);

            logger.info(
                    "User saved successfully. User ID: {}, Email: {}",
                    savedUser.getId(),
                    savedUser.getEmail());

            return savedUser;

        } catch (Exception e) {

            logger.error(
                    "Error while saving user without photo. Email: {}",
                    user.getEmail(),
                    e);

            throw e;
        }
    }

    // START: login
    @Override
    public Optional<User> findByEmail(String email) {

        logger.info("Login attempt received for email: {}", email);

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {

            logger.info("User found for email: {}", email);

        } else {

            logger.warn("User not found for email: {}", email);
        }

        return user;
    }
    // END: login
}