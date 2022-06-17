package com.deloitte.baseapp.modules.fileupload.controllers;
import com.deloitte.baseapp.commons.GenericController;
import com.deloitte.baseapp.commons.MessageResponse;
import com.deloitte.baseapp.modules.fileupload.entities.UploadedFile;
import com.deloitte.baseapp.modules.fileupload.entities.UploadedFilenames;
import com.deloitte.baseapp.modules.fileupload.repositories.UploadFileRepository;
import com.deloitte.baseapp.modules.fileupload.services.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartException;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;


@CrossOrigin(origins = "*")
@RestController
@Slf4j
@RequestMapping("/api/uploadfile")
public class UploadFileController extends GenericController<UploadedFile> {
    @Autowired
    FileUploadService fileUploadService;

    public UploadFileController(UploadFileRepository repository) {
        super(repository);
    }

    // Posts a file to be uploaded
    @PostMapping("/upload")
    public MessageResponse<?> uploadFile(
            @RequestParam("file") MultipartFile toUpload, @RequestParam("category") String category)
            throws IOException {
        try{
            UploadedFile dbFile = fileUploadService.create(toUpload, category);
            return new MessageResponse<>(dbFile, "Successfully Uploaded.");
        }
        catch(MultipartException e){
            log.error("Error during file upload: " + e.getMessage());
            return new MessageResponse<>(null, "Failed to Upload: " + e.getMessage());
        }


    }
    // Updates the uploaded file matching {id}
    @PutMapping(path = {"/update/{id}"})
    public MessageResponse<?> updateFile(
            @RequestParam("file") MultipartFile toUpdate, @PathVariable("id") Long id)
            throws IOException {
        try {
            UploadedFile newFile = fileUploadService.update(toUpdate, id);
            return new MessageResponse<>(newFile, "Successfully Updated.");
        }
        catch(NoSuchElementException e){
            return new MessageResponse<>(null, "No such file found: " + e.getMessage());
        }
    }

    // Returns the uploaded file matching {id} directly
    @GetMapping(path = {"/download/{id}"})
    public ResponseEntity<byte[]> downloadFileById(@PathVariable("id") Long id) {
        try {
            UploadedFile dbFile = fileUploadService.getOne(id);
            byte[] retrievedFile = dbFile.getData();
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.valueOf(dbFile.getType()))
                    .body(dbFile.getData());
                    /* If previously compressed, decompression is required
                     .body(FileUtils.decompressFile(dbFile.get().getData()));
                     */
        }
        catch(NoSuchElementException e){
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(null);
        }
    }
    // Returns the uploaded file matching {id} directly
    @GetMapping(path = {"/view/{id}"})
    public MessageResponse<?> getFileById(@PathVariable("id") Long id) throws IOException {
        try {
            UploadedFile dbFile = fileUploadService.getOne(id);
            return new MessageResponse<>(dbFile, "Successfully retrieved.");
        }
        catch(NoSuchElementException e){
            return new MessageResponse<>(null, "Failed retrieving: " + e.getMessage());
        }
    }

    @GetMapping(path = {"/list/category/{category}"})
    public MessageResponse<?> listByCategory(@PathVariable("category") String category) throws IOException {
        List<UploadedFilenames> dbFiles = fileUploadService.findByCategory(category);
        if (dbFiles == null || dbFiles.isEmpty()){
            return new MessageResponse<>(null, "No matching files found.");
        }
        return new MessageResponse<>(dbFiles, "Successfully retrieved filenames.");
    }
}
