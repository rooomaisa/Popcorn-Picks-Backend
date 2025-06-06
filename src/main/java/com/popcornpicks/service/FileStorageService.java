package com.popcornpicks.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /**
     * Saves the incoming MultipartFile to disk under a unique filename.
     * @param file the uploaded file
     * @return the generated stored filename (including extension)
     */
    String storeFile(MultipartFile file);

    /**
     * Loads a previously‐stored file as a Spring Resource (so it can be returned from a controller).
     * @param filename the name under which the file was saved
     * @return the Resource pointing to that file on disk
     */
    Resource loadFileAsResource(String filename);
}
