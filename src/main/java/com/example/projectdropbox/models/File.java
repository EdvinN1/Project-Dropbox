package com.example.projectdropbox.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fileName;

    @Lob
    private byte[] content;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;
}
