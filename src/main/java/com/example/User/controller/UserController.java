package com.example.User.controller;

import com.example.User.UserException.UserNotFoundException;
import com.example.User.model.User;
import com.example.User.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api") // the methods will have "/api" prefixed in the URI.
@Slf4j
public class UserController {

    private static final String TODO_API_URL = "https://jsonplaceholder.typicode.com/todos/";

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    // Create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Creating a new user: " + user);
        User createdUser = userService.saveUser(user);
        log.info("User created: " + createdUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userService.getAllUsers();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("Fetching user with ID: " + id);
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            log.info("User found: " + user.get());
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            log.warn("User with ID " + id + " not found");
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
    }

    // Update user by ID
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        log.info("Updating user with ID: " + id);
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            user.setId(id);
            User updatedUser = userService.saveUser(user);
            log.info("User updated: " + updatedUser);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        log.warn("User with ID " + id + " not found for update");
        throw new UserNotFoundException("User with ID " + id + " not found for update");
    }

    // Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: " + id);
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            userService.deleteUser(id);
            log.info("User with ID " + id + " deleted");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // 204 No Content
        }
        log.warn("User with ID " + id + " not found for deletion");
        throw new UserNotFoundException("User with ID " + id + " not found for deletion");
    }

    // Endpoint to call external URI
    @GetMapping("/todo/{id}")
    public ResponseEntity<String> getTodoById(@PathVariable Long id) {
        log.info("Fetching TODO item with ID: " + id);
        String url = TODO_API_URL + id;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            log.info("Response from TODO API: " + response.getBody());
            return response;  // Return the external API response
        } catch (Exception e) {
            log.error("Error fetching TODO item", e);
            return new ResponseEntity<>("Error fetching TODO item", HttpStatus.INTERNAL_SERVER_ERROR);  // Handle external API errors
        }
    }
}
