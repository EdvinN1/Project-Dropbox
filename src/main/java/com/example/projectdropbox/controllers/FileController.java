package com.example.projectdropbox.controllers;

import com.example.projectdropbox.models.File;
import com.example.projectdropbox.models.Folder;
import com.example.projectdropbox.models.User;
import com.example.projectdropbox.services.FileService;
import com.example.projectdropbox.services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FolderService folderService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam int folderId,
            @RequestParam MultipartFile file,
            @AuthenticationPrincipal User user) {
        try {
            // Check if the user owns the folder
            Folder folder = folderService.getFolderById(folderId);
            if (!folder.getOwner().equals(user)) {
                System.out.println(folder.getOwner());
                System.out.println(user);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You don't have permission to upload files to this folder.");

            }

            // Process file upload
            File newFile = fileService.uploadFile(folder, file);

            return ResponseEntity.ok("File uploaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file. " + e.getMessage());
        }
    }

    // Add more methods for file-related operations (download, delete, etc.)
}
