package com.example.projectdropbox.controllers;

import com.example.projectdropbox.DTOs.FileDTO;
import com.example.projectdropbox.DTOs.FolderDTO;
import com.example.projectdropbox.models.File;
import com.example.projectdropbox.models.Folder;
import com.example.projectdropbox.models.User;
import com.example.projectdropbox.services.FileService;
import com.example.projectdropbox.services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.Base64;
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
    public ResponseEntity<FolderDTO> getFilesInFolder(@PathVariable int folderId, @AuthenticationPrincipal User user) {
        try {
            // Hämta mappen och kontrollera om användaren äger den
            Folder folder = folderService.getFolderById(folderId);
            if (!folder.getOwner().equals(user)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            // Hämta filinformationen i mappen som FileDTO med länkar för att visa innehåll
            List<FileDTO> fileDTOs = folder.getFiles().stream()
                    .map(file -> new FileDTO(file.getId(), file.getFileName(), file.getFileSize(), file.getCreatedDate(), getFileDownloadLink(file)))
                    .collect(Collectors.toList());

            // Skapa FolderDTO och returnera
            String folderName = folder.getFolderName() != null ? folder.getFolderName() : "";
            System.out.println("Folder Name: " + folderName);

            FolderDTO folderDTO = new FolderDTO(folder.getId(), folderName, fileDTOs);



            return ResponseEntity.ok(folderDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    private String getFileDownloadLink(File file) {
        // Generera en länk för att ladda ner filen
        return "/files/download/" + file.getId();
    }
    @GetMapping("/files")
    public ResponseEntity<List<FileDTO>> getAllFiles() {
        try {
            List<File> files = fileService.getAllFiles();
            List<FileDTO> fileDTOs = files.stream()
                    .map(file -> new FileDTO(file.getId(), file.getFileName(), file.getFileSize(), file.getCreatedDate(), getFileDownloadLink(file)))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(fileDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(new FileDTO(0, "Failed to retrieve files. " + e.getMessage(), null, null, null)));
        }
    }

    @DeleteMapping("/delete/{fileId}")
    @Transactional
    public ResponseEntity<String> deleteFile(
            @PathVariable int fileId,
            @AuthenticationPrincipal User user) {
        try {
            // Hitta filen baserat på fil-ID.
            File fileToDelete = fileService.getFileById(fileId);

            // Kontrollera om användaren äger filen.
            if (!fileToDelete.getFolder().getOwner().equals(user)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You don't have permission to delete this file.");
            }

            // Radera filen.
            fileService.deleteFile(fileToDelete);

            return ResponseEntity.ok("File deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete file. " + e.getMessage());
        }
    }
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable int fileId) {
        try {
            // Hämta filen från databasen baserat på fil-ID
            File file = fileService.getFileById(fileId);

            // Skapa en ByteArrayResource från filens innehåll
            ByteArrayResource resource = new ByteArrayResource(file.getContent());

            // Returnera en ResponseEntity med filen och HTTP-headers för nedladdning
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFileName())
                    .contentLength(file.getContent().length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Add more methods for file-related operations (download, delete, etc.)
}
