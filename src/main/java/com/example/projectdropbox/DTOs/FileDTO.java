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
    private Long fileSize;
    private Date createdDate;
    private String fileDownloadLink;
}

