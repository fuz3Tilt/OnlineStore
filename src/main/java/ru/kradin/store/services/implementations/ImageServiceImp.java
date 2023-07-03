package ru.kradin.store.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kradin.store.services.interfaces.ImageService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageServiceImp implements ImageService {
    private static final Logger log = LoggerFactory.getLogger(ImageServiceImp.class);
    @Value("${store.uploadPath}")
    private String uploadPath;

    @Override
    public String save(MultipartFile imageToSave) throws IOException {
        Path uploadDir = Paths.get(uploadPath).normalize();
        createDirectoryIfNotExist(uploadDir);

        checkContentType(imageToSave);

        String fileName =
                "image_" + UUID.randomUUID().toString() + "_" + imageToSave.getOriginalFilename().toLowerCase().replaceAll(" ", "-");

        Path pathToImage = uploadDir.resolve(fileName).normalize();
        saveImage(imageToSave,pathToImage);

        log.info("Image {} saved.",fileName);

        return fileName;
    }

    @Override
    public void delete(String imageName) throws IOException{
        deleteByPath(getPathToImage(imageName));
        log.info("Image {} deleted.",imageName);
    }

    private void saveImage(MultipartFile imageToSave, Path pathToImage) throws IOException {
        try (InputStream imageStream = imageToSave.getInputStream()) {
            BufferedImage originalImage = ImageIO.read(imageStream);

            ImageIO.write(originalImage, "jpg", pathToImage.toFile());
        } catch (IOException e) {
            throw new IOException("Failed to save image.");
        }
    }

    private void checkContentType(MultipartFile imageToSave) throws IOException {
        if (!imageToSave.getContentType().equals("image/jpeg")) {
            throw new IOException("Failed to read image. Invalid format.");
        }
    }

    private void createDirectoryIfNotExist(Path uploadDir) throws IOException {
        if (!Files.exists(uploadDir)) {
            try{
                Files.createDirectories(uploadDir);
            } catch (IOException e){
                throw new IOException("Failed to create upload directory.");
            }
        }
    }

    private Path getPathToImage(String imageName) {
        Path imageStorageLocation = Paths.get(uploadPath).toAbsolutePath().normalize();
        return imageStorageLocation.resolve(imageName).normalize();
    }

    private void deleteByPath(Path path) throws IOException {
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e){
                throw new IOException("Failed to delete image.");
            }
        } else {
            throw new IOException("Invalid file path. From delete().");
        }
    }
}
