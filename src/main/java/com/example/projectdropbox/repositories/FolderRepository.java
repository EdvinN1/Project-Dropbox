package com.example.projectdropbox.repositories;

import com.example.projectdropbox.models.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Integer> {
    List<Folder> findByOwnerUsername(String username);
}
