package com.deloitte.baseapp.modules.fileupload.repositories;

import com.deloitte.baseapp.commons.GenericRepository;
import com.deloitte.baseapp.modules.fileupload.entities.UploadedFile;
import com.deloitte.baseapp.modules.fileupload.entities.UploadedFilenames;

import java.util.List;


public interface UploadFileRepository extends GenericRepository<UploadedFile> {
    List<UploadedFilenames> findAllByOrigin(String origin);
}
