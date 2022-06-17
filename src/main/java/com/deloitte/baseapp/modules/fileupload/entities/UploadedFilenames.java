package com.deloitte.baseapp.modules.fileupload.entities;

// Interface projection to get only ID and associated Filename
// TODO: Consider splitting file data and file metadata to separate tables to prevent long preloads
public interface UploadedFilenames {
        String getFilename();
        long getId();
}
