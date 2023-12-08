package com.example.projectdropbox.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileDTO {
    private int fileId;
    private String fileName;
    private Long fileSize;
    private String fileDownloadLink;
}

