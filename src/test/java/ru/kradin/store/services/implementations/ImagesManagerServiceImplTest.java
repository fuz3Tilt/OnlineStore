package ru.kradin.store.services.implementations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import ru.kradin.store.services.interfaces.ImagesManagerService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ImagesManagerServiceImplTest {

    @Autowired
    private ImagesManagerService imagesManager;

    @Value("${upload.path}")
    private String uploadPath;

    @Test
    public void testSave() throws IOException {
        MockMultipartFile imageFile = new MockMultipartFile(
                "test.jpg", "test.jpg", "image/jpeg", "test".getBytes());

        String savedFileName = imagesManager.save(imageFile);

        assertNotNull(savedFileName);

        Path savedFilePath = Paths.get(uploadPath).resolve(savedFileName);
        assertTrue(Files.exists(savedFilePath));

        Files.deleteIfExists(savedFilePath);

        try {
            MockMultipartFile invalidFormatImageFile = new MockMultipartFile(
                    "invalid-file.jpg", "invalid-file.png", "image/png", "invalid-file".getBytes());
            imagesManager.save(invalidFormatImageFile);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Failed to read image. Invalid format.", e.getMessage());
        }
    }

    @Test
    public void testGetImageAsResource() throws IOException {
        MockMultipartFile imageFile = new MockMultipartFile(
                "test.jpg", "test.jpg", "image/jpeg", "test".getBytes());

        String savedFileName = imagesManager.save(imageFile);

        Resource imageResource = imagesManager.getImageAsResource(savedFileName);

        assertTrue(imageResource.exists());
        assertTrue(imageResource.isReadable());

        Files.deleteIfExists(Paths.get(uploadPath).resolve(savedFileName));

        try {
            imagesManager.getImageAsResource("invalid-file.jpg");
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Invalid file path. From getImageAsResource().", e.getMessage());
        }
    }

    @Test
    public void testDelete() throws IOException {
        MockMultipartFile imageFile = new MockMultipartFile(
                "test.jpg", "test.jpg", "image/jpeg", "test".getBytes());

        String savedFileName = imagesManager.save(imageFile);

        imagesManager.delete(savedFileName);

        assertFalse(Files.exists(Paths.get(uploadPath).resolve(savedFileName)));

        try {
            imagesManager.delete("invalid-file.jpg");
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Invalid file path. From delete().", e.getMessage());
        }
    }
}
