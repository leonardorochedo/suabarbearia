package com.suabarbearia.backend.utils;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploader {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile image, String id, String folder) throws IOException {
        return cloudinary.uploader()
                .upload(image.getBytes(),
                        Map.of("public_id", id,
                                "folder", folder))
                .get("url")
                .toString();
    }

}
