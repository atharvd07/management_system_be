package com.example.ad.service.impl;

import com.example.ad.model.User;
import com.example.ad.repository.UserRepository;
import com.example.ad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    // Define the directory where files will be uploaded
    private final String uploadDir = "uploads/";
//	START: signup
    @Override
    public User saveUser(User user, MultipartFile profilePhoto) throws IOException {
        // Get the absolute path of the uploads directory
        Path path = Paths.get(uploadDir).toAbsolutePath();
        File dir = path.toFile();

        // Ensure the directory exists, create it if not
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new IOException("Failed to create upload directory: " + uploadDir);
            }
        }

        // Check if the profile photo is not empty
        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            // Validate the file size (e.g., 5MB max)
            if (profilePhoto.getSize() > 5000000) {  // 5MB in bytes
                throw new IOException("File size is too large! Max size is 5MB.");
            }

            // Validate the file type (ensure it's an image)
            String contentType = profilePhoto.getContentType();
            if (!contentType.startsWith("image/")) {
                throw new IOException("Only image files are allowed.");
            }

            // Generate a unique file name using the current timestamp
            String fileName = System.currentTimeMillis() + "-" + profilePhoto.getOriginalFilename();
            Path filePath = path.resolve(fileName);

            // Save the file to the uploads directory
            try {
                profilePhoto.transferTo(filePath.toFile());  // Save the file
                user.setProfilePhoto(fileName);  // Save only the file name in the user entity
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("Could not save file: " + fileName, e);
            }
        }

        // Save the user data in the database
        return userRepository.save(user);
    }

    // New method to save a user without a profile photo
    public User saveUserWithoutPhoto(User user) {
        return userRepository.save(user);
    }
//	END: signup    
//	START: login
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);  // This will return Optional<User>
    }
//	END: login 
}
