package ru.kradin.store.services.implementations;

import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import ru.kradin.store.exceptions.NameAlreadyUseException;
import ru.kradin.store.services.interfaces.CatalogService;
import ru.kradin.store.validators.CatalogValidator;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CatalogServiceImplTest {

    @Autowired
    CatalogService catalogService;

    CatalogValidator catalogValidator1 = new CatalogValidator();
    CatalogValidator catalogValidator2 = new CatalogValidator();
    CatalogValidator catalogValidator3 = new CatalogValidator();
    CatalogValidator catalogValidator4 = new CatalogValidator();
    CatalogValidator catalogValidator5 = new CatalogValidator();
    CatalogValidator catalogValidator6 = new CatalogValidator();
    CatalogValidator catalogValidator7 = new CatalogValidator();
    CatalogValidator catalogValidator8 = new CatalogValidator();
    List<CatalogValidator> catalogValidators;

    @Test
    public void getAllCatalogs() {
        List<CatalogValidator> catalogValidatorList = catalogService.getAllCatalogs();
        assertThat(catalogValidatorList).hasSize(8);
        assertThat(catalogValidatorList).containsExactly(catalogValidator1, catalogValidator2, catalogValidator3, catalogValidator4, catalogValidator5, catalogValidator6, catalogValidator7, catalogValidator8);
    }

    @Test
    public void getCatalogById() {
        CatalogValidator catalogValidator = catalogService.getCatalogById(catalogValidators.get(0).getId());
        assertThat(catalogValidator).isNotNull();
        assertThat(catalogValidator).isEqualTo(catalogValidator1);

        try {
            catalogService.getCatalogById(-1);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e){
            assertEquals("Could not find the catalog.",e.getMessage());
        }
    }

    @Test
    public void saveCatalog() throws IOException, NameAlreadyUseException {
        //update test
        CatalogValidator firstCatalogValidator = catalogService.getCatalogById(catalogValidators.get(0).getId());
        firstCatalogValidator.setName("Some name");
        firstCatalogValidator.setImageToUpload(new MockMultipartFile("S", "S", "image/jpeg", (byte[]) null));
        catalogService.saveCatalog(firstCatalogValidator);
        CatalogValidator secondCatalogValidator = catalogService.getCatalogById(catalogValidators.get(0).getId());
        assertThat(secondCatalogValidator.getId()).isEqualTo(firstCatalogValidator.getId());
        assertThat(secondCatalogValidator.getName()).isEqualTo("Some name");

        secondCatalogValidator.setImageToUpload(new MockMultipartFile(
                "imageUpdateTest.jpg", "imageUpdateTest.jpg", "image/jpeg", "imageUpdateTest".getBytes()));
        catalogService.saveCatalog(secondCatalogValidator);
        //test for exceptions
        CatalogValidator exceptionTest = new CatalogValidator();

        try {
            exceptionTest.setId(-1);
            catalogService.saveCatalog(exceptionTest);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e){
            assertEquals("Could not find the catalog to update.",e.getMessage());
        }

        try {
            exceptionTest.setId(0);
            exceptionTest.setImageToUpload(new MockMultipartFile("S", "S", "image/jpeg", (byte[]) null));
            catalogService.saveCatalog(exceptionTest);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e){
            assertEquals("Catalog must have an image to upload.",e.getMessage());
        }

        try {
            exceptionTest.setId(catalogValidators.get(0).getId());
            exceptionTest.setName("Some name");
            MockMultipartFile invalidFormatImageFile = new MockMultipartFile(
                    "invalid-file.png", "invalid-file.png", "image/invalid", "invalid-file".getBytes());
            exceptionTest.setImageToUpload(invalidFormatImageFile);
            catalogService.saveCatalog(exceptionTest);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Failed to read image. Invalid format.", e.getMessage());
        }

        try {
            exceptionTest.setName(catalogValidators.get(1).getName());
            catalogService.saveCatalog(exceptionTest);
            fail("Expected NameAlreadyUseException to be thrown");
        } catch (NameAlreadyUseException e){
            ///////////////////////////////
        }
    }

    @Test
    public void deleteCatalogById() throws IOException {
        catalogService.deleteCatalogById(catalogValidators.get(7).getId());
        List<CatalogValidator> catalogValidatorList = catalogService.getAllCatalogs();
        assertThat(catalogValidatorList).hasSize(7);
        assertThat(catalogValidatorList).containsExactly(catalogValidator1, catalogValidator2, catalogValidator3, catalogValidator4, catalogValidator5, catalogValidator6, catalogValidator7);
        try {
            catalogService.deleteCatalogById(-1);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e){
            assertEquals("Could not find the catalog to delete.",e.getMessage());
        }
    }

    @BeforeEach
    public void beforeEach() throws IOException, NameAlreadyUseException {
        Authentication auth = new TestingAuthenticationToken("username", "password", "ROLE_ADMIN");
        SecurityContextHolder.getContext().setAuthentication(auth);

        catalogValidator1.setName("catalogValidatorTest1");
        catalogValidator1.setImageToUpload(new MockMultipartFile(
                "test1.jpg", "test1.jpg", "image/jpeg", "test1".getBytes()));
        catalogService.saveCatalog(catalogValidator1);

        catalogValidator2.setName("catalogValidatorTest2");
        catalogValidator2.setImageToUpload(new MockMultipartFile(
                "test2.jpg", "test2.jpg", "image/jpeg", "test2".getBytes()));
        catalogService.saveCatalog(catalogValidator2);

        catalogValidator3.setName("catalogValidatorTest3");
        catalogValidator3.setImageToUpload(new MockMultipartFile(
                "test3.jpg", "test3.jpg", "image/jpeg", "test3".getBytes()));
        catalogService.saveCatalog(catalogValidator3);

        catalogValidator4.setName("catalogValidatorTest4");
        catalogValidator4.setImageToUpload(new MockMultipartFile(
                "test4.jpg", "test4.jpg", "image/jpeg", "test4".getBytes()));
        catalogService.saveCatalog(catalogValidator4);

        catalogValidator5.setName("catalogValidatorTest5");
        catalogValidator5.setImageToUpload(new MockMultipartFile(
                "test5.jpg", "test5.jpg", "image/jpeg", "test5".getBytes()));
        catalogService.saveCatalog(catalogValidator5);

        catalogValidator6.setName("catalogValidatorTest6");
        catalogValidator6.setImageToUpload(new MockMultipartFile(
                "test6.jpg", "test6.jpg", "image/jpeg", "test6".getBytes()));
        catalogService.saveCatalog(catalogValidator6);

        catalogValidator7.setName("catalogValidatorTest7");
        catalogValidator7.setImageToUpload(new MockMultipartFile(
                "test7.jpg", "test7.jpg", "image/jpeg", "test7".getBytes()));
        catalogService.saveCatalog(catalogValidator7);

        catalogValidator8.setName("catalogValidatorTest8");
        catalogValidator8.setImageToUpload(new MockMultipartFile(
                "test8.jpg", "test8.jpg", "image/jpeg", "test8".getBytes()));
        catalogService.saveCatalog(catalogValidator8);

        catalogValidators = catalogService.getAllCatalogs();
    }

    @AfterEach
    public void afterEach() throws IOException {
        List<CatalogValidator> catalogValidatorList = catalogService.getAllCatalogs();
        for(CatalogValidator catalogValidator:catalogValidatorList){
            catalogService.deleteCatalogById(catalogValidator.getId());
        }
        catalogValidator1 = new CatalogValidator();
        catalogValidator2 = new CatalogValidator();
        catalogValidator3 = new CatalogValidator();
        catalogValidator4 = new CatalogValidator();
        catalogValidator5 = new CatalogValidator();
        catalogValidator6 = new CatalogValidator();
        catalogValidator7 = new CatalogValidator();
        catalogValidator8 = new CatalogValidator();
        catalogValidators = null;
    }
}
