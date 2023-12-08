package com.example.projectdropbox.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FolderDTO {
    private int folderId;
    private String folderName;
    private List<FileDTO> files;
}

