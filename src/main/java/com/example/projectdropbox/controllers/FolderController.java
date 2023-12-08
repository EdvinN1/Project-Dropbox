package com.example.projectdropbox.controllers;

import com.example.projectdropbox.DTOs.FolderDTO;
import com.example.projectdropbox.models.Folder;
import com.example.projectdropbox.models.User;
import com.example.projectdropbox.services.FolderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    @GetMapping("/username")
    @Transactional
    public ResponseEntity<List<FolderDTO>> getUserFolders(@AuthenticationPrincipal User user) {
        try {
            // Hämta mappar baserat på användarnamnet
            List<Folder> folders = folderService.getFoldersByUserUsername(user.getUsername());

            // Konvertera mapparna till DTOs för att skicka till klienten
            List<FolderDTO> folderDTOs = folders.stream()
                    .map(folder -> new FolderDTO(folder.getId(), folder.getFolderName(), null))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(folderDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

