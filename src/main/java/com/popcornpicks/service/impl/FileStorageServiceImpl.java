package com.popcornpicks.service.impl;

import com.popcornpicks.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path uploadDir;

    public FileStorageServiceImpl(@Value("${file.upload-dir}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create upload directory: " + uploadDir, ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        // 1) Clean up the original filename
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

        // 2) Compute a unique filename (so we never overwrite)
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }
        String storedFilename = UUID.randomUUID().toString() + extension;

        try {
            // 3) Copy the data to our upload dir
            Path targetLocation = this.uploadDir.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return storedFilename;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file " + originalFilename, ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.uploadDir.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or not readable: " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found: " + filename, ex);
        }
    }
}
