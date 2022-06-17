package com.deloitte.baseapp.modules.fileupload.services;

import com.deloitte.baseapp.modules.fileupload.entities.UploadedFile;
import com.deloitte.baseapp.modules.fileupload.entities.UploadedFilenames;
import com.deloitte.baseapp.modules.fileupload.repositories.UploadFileRepository;
import com.deloitte.baseapp.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class FileUploadService {

    @Autowired
    UploadFileRepository uploadFileRepository;

    public UploadedFile create(MultipartFile toUpload, String category) throws IOException {
        if(!FileUtils.validateFile(toUpload)){
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to Upload! No file in request. Filename: "
                            + toUpload.getOriginalFilename());
        }
        if (category == null || category.isEmpty()){
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to Upload! [category] is required. Filename: "
                            + toUpload.getOriginalFilename());
        }
        UploadedFile newFile = UploadedFile.builder()
                .filename(toUpload.getOriginalFilename())
                .type(toUpload.getContentType())
                .origin(category)
                .fileSizeKB(FileUtils.getFileSizeKB(toUpload))
                .data(toUpload.getBytes()).build();
        return uploadFileRepository.save(newFile);
    }
    public UploadedFile update(MultipartFile toUpdate, long id) throws IOException, NoSuchElementException {
        UploadedFile dbFile = uploadFileRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No file found matching ID: " + id));
        dbFile.setData(toUpdate.getBytes());
        dbFile.setFilename(toUpdate.getOriginalFilename());
        dbFile.setType(toUpdate.getContentType());
        dbFile.setFileSizeKB(FileUtils.getFileSizeKB(toUpdate));
        return uploadFileRepository.save(dbFile);
    }
    public UploadedFile getOne(long id) throws NoSuchElementException{
        return uploadFileRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No file found matching ID: " + id));
    }

    // Method will only list ID and matching Filenames
    public List<UploadedFilenames> findByCategory(String category){
        return uploadFileRepository.findAllByOrigin(category);
    }

}
