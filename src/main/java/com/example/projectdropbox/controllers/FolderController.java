package com.example.projectdropbox.controllers;

import com.example.projectdropbox.models.Folder;
import com.example.projectdropbox.models.User;
import com.example.projectdropbox.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/folders")
public class FolderController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createFolder(@AuthenticationPrincipal User user, @RequestParam String folderName) {
            Folder folder = new Folder();
            folder.setName(folderName);
            folder.setOwner(user);

            user.getFolders().add(folder);
            userService.saveUser(user);

            return ResponseEntity.ok("Folder created successfully!");
        }
}

