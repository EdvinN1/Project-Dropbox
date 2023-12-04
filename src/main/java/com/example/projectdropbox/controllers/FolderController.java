package com.example.projectdropbox.controllers;

import com.example.projectdropbox.models.File;
import com.example.projectdropbox.models.Folder;
import com.example.projectdropbox.models.User;
import com.example.projectdropbox.services.FileService;
import com.example.projectdropbox.services.FolderService;
import com.example.projectdropbox.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @PostMapping("/create")
    public ResponseEntity<String> createFolder(@AuthenticationPrincipal User user, @RequestParam String folderName) {
        try {
            // Skapa en ny mapp med hjälp av FolderService
            folderService.createFolder(folderName, user);

            return ResponseEntity.ok("Folder created successfully!");
        } catch (Exception e) {
            // ... (hantera fel)
        }

        return null;
    }
}

