package com.example.projectdropbox.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class FileDTO {
    private int fileId;
    private String fileName;
    private Long fileSize; // Lägg till detta om du vill inkludera filstorlek
    private Date createdDate; // Lägg till detta om du vill inkludera skapad tidpunkt
    private String content; // Länk eller base64-kodning av filinnehållet
}

