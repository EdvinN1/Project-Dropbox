package com.example.projectdropbox.services;

import com.example.projectdropbox.Exceptions.FolderNotFoundException;
import com.example.projectdropbox.models.Folder;
import com.example.projectdropbox.models.User;
import com.example.projectdropbox.repositories.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderService {

    private final FolderRepository folderRepository;

    @Autowired
    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public void createFolder(String folderName, User owner) {
        // Skapa en ny mapp
        Folder folder = new Folder();
        folder.setFolderName(folderName);
        folder.setOwner(owner);

        // Spara mappen i databasen
        folderRepository.save(folder);
    }
    public Folder getFolderById(int folderId) {
        return folderRepository.findById(folderId)
                .orElseThrow(() -> new FolderNotFoundException("Folder not found with id: " + folderId));
    }
    public List<Folder> getFoldersByUserUsername(String username) {
        return folderRepository.findByOwnerUsername(username);
    }
}
