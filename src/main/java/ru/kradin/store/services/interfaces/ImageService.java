package ru.kradin.store.services.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    public String save(MultipartFile imageToSave) throws IOException;
    public void delete(String imageName) throws IOException;
}
