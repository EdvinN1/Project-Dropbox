package com.example.projectdropbox.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    // Lägg till denna metod
    public Long getFileSize() {
        return (long) this.content.length; // Detta är bara en exempelmetod. Du kan anpassa den efter dina behov.
    }
}

