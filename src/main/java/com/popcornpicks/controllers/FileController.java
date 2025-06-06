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

    /**
     * 1) Upload a file. Only admins may call this.
     *    Returns the stored filename (e.g. "a1b2c3d4-....jpg").
     */
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        // storeFile(...) returns the unique stored filename
        String storedFilename = fileStorageService.storeFile(file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(storedFilename);
    }

    /**
     * 2) Download a file by its stored filename. Any authenticated user may call this.
     */
    @GetMapping("/{filename:.+}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String filename,
            HttpServletRequest request
    ) {
        // Load the file as a Resource
        Resource resource = fileStorageService.loadFileAsResource(filename);

        // Try to determine file's content type
        String contentType;
        try {
            contentType = Objects.requireNonNull(
                    request.getServletContext()
                            .getMimeType(resource.getFile().getAbsolutePath())
            );
        } catch (IOException ex) {
            // Fallback to generic binary data if type detection fails
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        // Return the resource, setting Content-Type and forcing download
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\""
                )
                .body(resource);
    }
}


//package com.popcornpicks.controllers;
//
//import com.popcornpicks.service.FileStorageService;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import jakarta.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.Objects;
//
//@RestController
//@RequestMapping("/api/v1/files")
//public class FileController {
//
//    private final FileStorageService fileStorageService;
//
//    public FileController(FileStorageService fileStorageService) {
//        this.fileStorageService = fileStorageService;
//    }
//
//    /**
//     * 1) Upload a file. Only admins may call this.
//     *    Returns the stored filename.
//     */
//    @PostMapping("/upload")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        // storeFile(...) returns the unique stored filename (e.g., "a1b2c3d4-....jpg")
//        String storedFilename = fileStorageService.storeFile(file);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(storedFilename);
//    }
//
//    /**
//     * 2) Download a file by its stored filename. Any authenticated user may call this.
//     */
//    @GetMapping("/{filename:.+}")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<Resource> downloadFile(
//            @PathVariable String filename,
//            HttpServletRequest request
//    ) {
//        // Load the file as a Resource
//        Resource resource = fileStorageService.loadFileAsResource(filename);
//
//        // Try to determine file's content type
//        String contentType;
//        try {
//            contentType = Objects.requireNonNull(
//                    request.getServletContext().getMimeType(resource.getFile().getAbsolutePath())
//            );
//        } catch (IOException ex) {
//            // Fallback to generic binary data
//            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
//        }
//
//        // Return the resource, setting Content-Type and Content-Disposition
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
//    }
//}
