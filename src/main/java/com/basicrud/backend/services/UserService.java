package com.basicrud.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.basicrud.backend.domain.User;
import com.basicrud.backend.dto.UserCreateRequest;
import com.basicrud.backend.exceptions.InvalidRequestDataException;
import com.basicrud.backend.repositories.UserRepository;
import com.basicrud.backend.utils.NicknameValidator;
import com.basicrud.backend.utils.PasswordValidator;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private PasswordValidator passwordValidator;
    private NicknameValidator nicknameValidator;

    @Autowired
    public UserService(
        UserRepository userRepository, 
        PasswordEncoder passwordEncoder,
        PasswordValidator passwordValidator,
        NicknameValidator nicknameValidator
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.passwordValidator = passwordValidator;
        this.nicknameValidator = nicknameValidator;
    }

    public User createUser(UserCreateRequest request) {
        boolean isValidNickname = nicknameValidator.isValid(request.nickname());
        if (!isValidNickname) {
            throw new InvalidRequestDataException("Invalid nickname format");
        }

        boolean existingByEmail = userRepository.existsByEmail(request.email());
        if (existingByEmail) {
            throw new InvalidRequestDataException("Email already in use");
        }

        boolean isValidPassword = passwordValidator.isValid(request.password());
        if (!isValidPassword) {
            throw new InvalidRequestDataException(
                "Password must be 8-1024 characters long and include at least one uppercase letter, " +
                "one lowercase letter, one digit, and one special character (!@#$%^&*()-+)"
            );
        }

        User newUser = new User(
            request.nickname(),
            request.email(),
            ""
        );

        setUserPassword(newUser, request.password());
        userRepository.save(newUser);

        return newUser;
    }

    public void deleteUser(Long id) {
        // Logic to delete a user
        userRepository.deleteById(id);
    }

    public Optional<User> getUserById(Long id) {
        // Logic to retrieve a user by ID
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        // Logic to retrieve a user by email
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByNickname(String nickname) {
        // Logic to retrieve a user by nickname
        return userRepository.findByNickname(nickname);
    }

    public boolean userExistsByEmail(String email) {
        // Logic to check if a user exists by email
        return userRepository.existsByEmail(email);
    }

    public boolean userExistsByNickname(String nickname) {
        // Logic to check if a user exists by nickname
        return userRepository.existsByNickname(nickname);
    }

    public boolean userExistsById(Long id) {
        // Logic to check if a user exists by ID
        return userRepository.existsById(id);
    }

    public void setUserPassword(User user, String rawPassword) {
        String hashedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(hashedPassword);
    }

    public boolean checkUserPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
