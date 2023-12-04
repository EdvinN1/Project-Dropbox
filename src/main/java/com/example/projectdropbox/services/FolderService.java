package com.example.projectdropbox.services;

import com.example.projectdropbox.models.Folder;
import com.example.projectdropbox.models.User;
import com.example.projectdropbox.repositories.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        folder.setName(folderName);
        folder.setOwner(owner);

        // Spara mappen i databasen
        folderRepository.save(folder);
    }
}
