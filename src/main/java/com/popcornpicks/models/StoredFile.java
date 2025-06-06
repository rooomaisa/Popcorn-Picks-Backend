package com.popcornpicks.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stored_files")
public class StoredFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;
    private Long size;
    private String fileDownloadUri;

    public StoredFile() {}

    public StoredFile(String fileName, String fileType, Long size, String fileDownloadUri) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.size = size;
        this.fileDownloadUri = fileDownloadUri;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getSize() {
        return size;
    }
    public void setSize(Long size) {
        this.size = size;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }
    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }
}
