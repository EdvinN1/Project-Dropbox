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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FolderService folderService;


    @PostMapping("/upload")
    @Transactional
    public ResponseEntity<String> uploadFile(
            @RequestParam int folderId,
            @RequestParam MultipartFile file,
            @AuthenticationPrincipal User user) {
        try {
            // Check if the user owns the folder
            Folder folder = folderService.getFolderById(folderId);
            if (!folder.getOwner().equals(user)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You don't have permission to upload files to this folder.");
            }

            // Extract file name and content from MultipartFile
            String fileName = file.getOriginalFilename();
            byte[] fileContent = file.getBytes();

            // Process file upload with both file name and content
            File newFile = fileService.uploadFile(folder, fileName, fileContent);

            return ResponseEntity.ok("File uploaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file. " + e.getMessage());
        }
    }

    @GetMapping("/folder/{folderId}")
    @Transactional
    public ResponseEntity<List<String>> getFilesInFolder(@PathVariable int folderId, @AuthenticationPrincipal User user) {
        try {
            // Hämta mappen och kontrollera om användaren äger den
            Folder folder = folderService.getFolderById(folderId);
            if (!folder.getOwner().equals(user)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Collections.singletonList("You don't have permission to access files in this folder."));
            }

            // Hämta filnamnen i mappen
            List<String> fileNames = folder.getFiles().stream()
                    .map(File::getFileName)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(fileNames);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList("Failed to retrieve files from the folder. " + e.getMessage()));
        }
    }
    @GetMapping("/files")
    public ResponseEntity<List<String>> getAllFiles() {
        try {
            List<File> files = fileService.getAllFiles();
            List<String> fileNames = files.stream()
                    .map(File::getFileName)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(fileNames);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList("Failed to retrieve files. " + e.getMessage()));
        }
    }

    // Add more methods for file-related operations (download, delete, etc.)
}
