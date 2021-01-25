package com.dev.miceoservices.controller;

import com.dev.miceoservices.model.Contact;
import com.dev.miceoservices.model.User;
import com.dev.miceoservices.repo.ContactRepo;
import com.dev.miceoservices.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class SerachController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ContactRepo contactRepo;
    @GetMapping("/serach/{query}")
    public ResponseEntity<?> seachUser(@PathVariable String query ,Principal principal){
        User user = userRepo.findByEmail(principal.getName());
        List<Contact> contacts = contactRepo.findByNameContainingAndUser(query, user);
        return ResponseEntity.ok(contacts);
    }
}
