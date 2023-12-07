package com.example.projectdropbox.services;

import com.example.projectdropbox.models.File;
import com.example.projectdropbox.models.Folder;
import com.example.projectdropbox.repositories.FileRepository;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Transactional
    public File uploadFile(Folder folder, String fileName, byte[] fileContent) throws FileUploadException {
        try {
            File newFile = new File();
            newFile.setFileName(fileName);
            newFile.setContent(fileContent);
            newFile.setFolder(folder);

            folder.getFiles().add(newFile);

            // Save file in the database
            fileRepository.save(newFile);

            return newFile;
        } catch (Exception e) {
            throw new FileUploadException("Failed to upload file.", e);
        }
    }
    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    // Add more methods for file-related operations (download, delete, etc.)
}
