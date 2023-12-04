package com.example.projectdropbox.repositories;

import com.example.projectdropbox.models.File;
import com.example.projectdropbox.models.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Integer> {
}
