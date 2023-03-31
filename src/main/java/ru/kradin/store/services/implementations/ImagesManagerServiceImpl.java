package ru.kradin.store.services.implementations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kradin.store.services.interfaces.ImagesManagerService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImagesManagerServiceImpl implements ImagesManagerService {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public String save(MultipartFile imageToSave) throws IOException {
        Path uploadDir = Paths.get(uploadPath).normalize();
        if (!Files.exists(uploadDir)) {
            try{
                Files.createDirectories(uploadDir);
            } catch (IOException e){
                throw new IOException("Failed to create upload directory.");
            }
        }

        if (!imageToSave.getContentType().equals("image/jpeg")) {
            throw new IOException("Failed to read image. Invalid format.");
        }

        String fileName =
                "image_" + UUID.randomUUID().toString() + "_" + imageToSave.getOriginalFilename().toLowerCase().replaceAll(" ", "-");
        Path imageFilePath = uploadDir.resolve(fileName).normalize();

        try (InputStream imageStream = imageToSave.getInputStream()) {
            BufferedImage originalImage = ImageIO.read(imageStream);

            BufferedImage resizedImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = resizedImage.createGraphics();
            graphics.drawImage(originalImage, 0, 0, 256, 256, null);
            graphics.dispose();

            ImageIO.write(resizedImage, "jpg", imageFilePath.toFile());

        } catch (IOException e) {
            throw new IOException("Failed to save image.");
        }

        return fileName;
    }

    @Override
    public Resource getImageAsResource(String imageName) throws IOException {
        Path imageStorageLocation = Paths.get(uploadPath).toAbsolutePath().normalize();
        Path imagePath = imageStorageLocation.resolve(imageName).normalize();
        if (Files.exists(imagePath)) {
            try {
                Resource resource = new UrlResource(imagePath.toUri());
                return resource;
            } catch (IOException e) {
                throw new IOException("Failed to read image as Resource.");
            }
        } else {
            throw new IOException("Invalid file path. From getImageAsResource().");
        }
    }

    @Override
    public void delete(String imageName) throws IOException{
        Path imageStorageLocation = Paths.get(uploadPath).toAbsolutePath().normalize();
        Path imagePath = imageStorageLocation.resolve(imageName).normalize();
        if (Files.exists(imagePath)) {
            try {
                Files.delete(imagePath);
            } catch (IOException e){
                throw new IOException("Failed to delete image.");
            }
        } else {
            throw new IOException("Invalid file path. From delete().");
        }
    }
}
