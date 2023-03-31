package ru.kradin.store.services.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImagesManagerService {
    public String save(MultipartFile imageToSave) throws IOException;
    public Resource getImageAsResource(String imageName) throws IOException;
    public void delete(String imageName) throws IOException;
}
