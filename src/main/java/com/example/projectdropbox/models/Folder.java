package com.example.projectdropbox.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "dropboxFolders")
@Getter
@Setter
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String folderName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "folder")
    private List<File> files = new ArrayList<>();


    // Lägg till eventuella ytterligare attribut som du behöver

    // Getter och setter
}
