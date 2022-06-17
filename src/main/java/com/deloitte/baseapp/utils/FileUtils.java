package com.deloitte.baseapp.utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Slf4j
public class FileUtils {

    /*
     * Performs various generic validations for an uploaded file.
     */
    public static boolean validateFile(MultipartFile file)
            throws MultipartException {
        // Generic NULL Validation
        if (file == null || file.getContentType() == null){
            throw new MultipartException("Uploaded File is empty or null");
        }
        // Perform any
        return true;
    }

    /* Converts filesize from bytes to binary kb
     * Note: result is NOT truncated or rounded decimal places
     */
    public static double getFileSizeKB(MultipartFile file){
        // MultipartFile.getSize returns bytes, which will need to be converted to binary KB
        return file.getSize() * 0.0009765625;
    }
    /*
     * Generally unnecessary, but functional for additional compression.
     */
    public static byte[] compressFile(byte[] fileData){
        // Using ZLIB Compression
        Deflater deflater = new Deflater();
        deflater.setInput(fileData);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(fileData.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            log.error("Error compressing file: " + e.getMessage());
        }
        log.info("Compression Successful: File Size:" + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }
    /*
     * (!) Remember to decompress on retrieval if the file had been compressed.
     */
    public static byte[] decompressFile(byte[] fileData){
        Inflater inflater = new Inflater();
        inflater.setInput(fileData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(fileData.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            log.error("Error decompressing file: " + e.getMessage());
        }
        return outputStream.toByteArray();
    }
}
