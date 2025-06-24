package com.popcornpicks.controllers;

import com.popcornpicks.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }


    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

        String storedFilename = fileStorageService.storeFile(file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(storedFilename);
    }


    @GetMapping("/{filename:.+}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String filename,
            HttpServletRequest request
    ) {

        Resource resource = fileStorageService.loadFileAsResource(filename);


        String contentType;
        try {
            contentType = Objects.requireNonNull(
                    request.getServletContext()
                            .getMimeType(resource.getFile().getAbsolutePath())
            );
        } catch (IOException ex) {

            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\""
                )
                .body(resource);
    }
}