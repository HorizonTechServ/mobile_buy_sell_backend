package com.one.societyAPI.service;


import com.one.societyAPI.entity.ComplaintImage;
import com.one.societyAPI.repository.ComplaintImageRepository;
import com.one.societyAPI.utils.ImageUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ComplaintImageService {

    private final ComplaintImageRepository repository;

    public ComplaintImageService(ComplaintImageRepository repository) {
        this.repository = repository;
    }

    public ComplaintImage saveImage(MultipartFile file) throws IOException {
        ComplaintImage image = ComplaintImage.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtil.compressImage(file.getBytes()))
                .build();
        return repository.save(image);
    }

    public byte[] getImage(Long id) {
        return repository.findById(id)
                .map(img -> ImageUtil.decompressImage(img.getImageData()))
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }
}
