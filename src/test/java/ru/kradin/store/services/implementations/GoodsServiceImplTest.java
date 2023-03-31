package ru.kradin.store.services.implementations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import ru.kradin.store.exceptions.NameAlreadyUseException;
import ru.kradin.store.models.enums.Status;
import ru.kradin.store.services.interfaces.CatalogService;
import ru.kradin.store.services.interfaces.GoodsService;
import ru.kradin.store.validators.CatalogValidator;
import ru.kradin.store.validators.GoodsValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class GoodsServiceImplTest {

    @Autowired
    GoodsService goodsService;

    @Autowired
    CatalogService catalogService;

    CatalogValidator catalogValidator1 = new CatalogValidator();
    CatalogValidator catalogValidator2 = new CatalogValidator();
    GoodsValidator goodsValidator1 = new GoodsValidator();
    GoodsValidator goodsValidator2 = new GoodsValidator();
    GoodsValidator goodsValidator3 = new GoodsValidator();
    GoodsValidator goodsValidator4 = new GoodsValidator();
    GoodsValidator goodsValidator5 = new GoodsValidator();
    GoodsValidator goodsValidator6 = new GoodsValidator();
    GoodsValidator goodsValidator7 = new GoodsValidator();
    GoodsValidator goodsValidator8 = new GoodsValidator();
    List<GoodsValidator> goodsValidators;

    @Test
    public void getAvailableCatalogGoodsByCatalogId() {
        List<GoodsValidator> goodsValidatorList1 = goodsService.getAvailableCatalogGoodsByCatalogId(catalogValidator1.getId());
        assertThat(goodsValidatorList1).hasSize(3);
        assertThat(goodsValidatorList1).containsExactly(goodsValidator3, goodsValidator4, goodsValidator5);

        List<GoodsValidator> goodsValidatorList2 = goodsService.getAvailableCatalogGoodsByCatalogId(catalogValidator2.getId());
        assertThat(goodsValidatorList2).hasSize(2);
        assertThat(goodsValidatorList2).containsExactly(goodsValidator6, goodsValidator7);
    }

    @Test
    public void getAllCatalogGoodsByCatalogId() {
        List<GoodsValidator> goodsValidatorList1 = goodsService.getAllCatalogGoodsByCatalogId(catalogValidator1.getId());
        assertThat(goodsValidatorList1).hasSize(5);
        assertThat(goodsValidatorList1).containsExactly(goodsValidator1, goodsValidator2, goodsValidator3, goodsValidator4, goodsValidator5);

        List<GoodsValidator> goodsValidatorList2 = goodsService.getAllCatalogGoodsByCatalogId(catalogValidator2.getId());
        assertThat(goodsValidatorList2).hasSize(3);
        assertThat(goodsValidatorList2).containsExactly(goodsValidator6, goodsValidator7, goodsValidator8);
    }

    @Test
    public void getAllUnavailableGoods() {
        List<GoodsValidator> goodsValidatorList = goodsService.getAllUnavailableGoods();
        assertThat(goodsValidatorList).hasSize(3);
        assertThat(goodsValidatorList).containsExactly(goodsValidator1, goodsValidator2, goodsValidator8);
    }

    @Test
    public void getAvailableGoodsById() {
        GoodsValidator goodsValidator = goodsService.getAvailableGoodsById(goodsValidators.get(3).getId());
        assertThat(goodsValidator).isNotNull();
        assertThat(goodsValidator).isEqualTo(goodsValidators.get(3));
        try {
            goodsValidator = goodsService.getAvailableGoodsById(goodsValidators.get(1).getId());
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e){
            assertThat(e.getMessage()).isEqualTo("Could not find the goods.");
        }
    }
    @Test
    public void getGoodsById() {
        GoodsValidator goodsValidator = goodsService.getGoodsById(goodsValidators.get(0).getId());
        assertThat(goodsValidator).isNotNull();
        assertThat(goodsValidator).isEqualTo(goodsValidators.get(0));

        try{
            goodsService.getGoodsById(-1);
            fail("Expected RuntimeException to be thrown");
        } catch(RuntimeException e){
            assertEquals("Could not find the goods.",e.getMessage());
        }
    }

    @Test
    public void saveGoods() throws IOException, NameAlreadyUseException {
        //update test
        GoodsValidator firstGoodsValidator = goodsService.getGoodsById(goodsValidators.get(0).getId());
        firstGoodsValidator.setName("Some name");
        firstGoodsValidator.setImageToUpload(new MockMultipartFile("S", "S", "image/jpeg", (byte[]) null));
        goodsService.saveGoods(firstGoodsValidator);
        GoodsValidator secondGoodsValidator = goodsService.getGoodsById(goodsValidators.get(0).getId());
        assertThat(secondGoodsValidator.getId()).isEqualTo(firstGoodsValidator.getId());
        assertThat(secondGoodsValidator.getName()).isEqualTo("Some name");

        secondGoodsValidator.setImageToUpload(new MockMultipartFile(
                "imageUpdateTest.jpg", "imageUpdateTest.jpg", "image/jpeg", "imageUpdateTest".getBytes()));
        goodsService.saveGoods(secondGoodsValidator);
        //test for exceptions
        GoodsValidator exceptionTest = new GoodsValidator();

        try {
            exceptionTest.setId(-1);
            goodsService.saveGoods(exceptionTest);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e){
            assertEquals("Could not find the catalog to puts the goods inside.",e.getMessage());
        }

        try {
            exceptionTest.setId(-1);
            exceptionTest.setCatalogId(catalogValidator1.getId());
            goodsService.saveGoods(exceptionTest);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e){
            assertEquals("Could not find the goods to update.",e.getMessage());
        }

        try {
            exceptionTest.setId(0);
            exceptionTest.setImageToUpload(new MockMultipartFile("S", "S", "image/jpeg", (byte[]) null));
            goodsService.saveGoods(exceptionTest);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e){
            assertEquals("Goods must have an image to upload.",e.getMessage());
        }

        try {
            exceptionTest.setId(goodsValidators.get(0).getId());
            exceptionTest.setDescription("some description");
            exceptionTest.setCatalogId(catalogValidator1.getId());
            exceptionTest.setName("catalogValidatorTest1");
            MockMultipartFile invalidFormatImageFile = new MockMultipartFile(
                    "invalid-file.png", "invalid-file.png", "image/png", "invalid-file".getBytes());
            exceptionTest.setImageToUpload(invalidFormatImageFile);
            goodsService.saveGoods(exceptionTest);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Failed to read image. Invalid format.", e.getMessage());
        }

        try {
            exceptionTest.setName(goodsValidators.get(1).getName());
            goodsService.saveGoods(exceptionTest);
            fail("Expected NameAlreadyUseException to be thrown");
        } catch (NameAlreadyUseException e){
            ///////////////////////////////
        }
    }

    @Test
    public void deleteGoodsById() throws IOException {
        goodsService.deleteGoodsById(goodsValidators.get(4).getId());
        List<GoodsValidator> goodsValidatorList = goodsService.getAvailableCatalogGoodsByCatalogId(catalogValidator1.getId());
        assertThat(goodsValidatorList).hasSize(2);
        assertThat(goodsValidatorList).containsExactly(goodsValidator3, goodsValidator4);

        try{
            goodsService.deleteGoodsById(-1);
            fail("Expected RuntimeException to be thrown");
        } catch(RuntimeException e){
            assertEquals("Could not find the goods to delete.",e.getMessage());
        }
    }

    @BeforeEach
    public void beforeEach() throws IOException, NameAlreadyUseException {
        Authentication auth = new TestingAuthenticationToken("username", "password", "ROLE_ADMIN");
        SecurityContextHolder.getContext().setAuthentication(auth);

        catalogValidator1.setName("catalogValidatorTest1");
        catalogValidator1.setImageToUpload(new MockMultipartFile(
                "catalogTest1.jpg", "catalogTest1.jpg", "image/jpeg", "catalogTest1".getBytes()));
        catalogService.saveCatalog(catalogValidator1);

        catalogValidator2.setName("catalogValidatorTest2");
        catalogValidator2.setImageToUpload(new MockMultipartFile(
                "catalogTest2.jpg", "catalogTest2.jpg", "image/jpeg", "catalogTest2".getBytes()));
        catalogService.saveCatalog(catalogValidator2);

        catalogValidator1 = catalogService.getAllCatalogs().get(0);
        catalogValidator2 = catalogService.getAllCatalogs().get(1);
        ///////////////////////////////////////////////////////////
        goodsValidator1.setName("goodsValidatorTest1");
        goodsValidator1.setDescription("goodsValidatorDescription1");
        goodsValidator1.setStatus(Status.UNAVAILABLE);
        goodsValidator1.setCatalogId(catalogValidator1.getId());
        goodsValidator1.setPrice(1);
        goodsValidator1.setImageToUpload(new MockMultipartFile(
                "goodsTest1.jpg", "goodsTest1.jpg", "image/jpeg", "goodsTest1".getBytes()));
        goodsService.saveGoods(goodsValidator1);

        goodsValidator2.setName("goodsValidatorTest2");
        goodsValidator2.setDescription("goodsValidatorDescription2");
        goodsValidator2.setStatus(Status.UNAVAILABLE);
        goodsValidator2.setCatalogId(catalogValidator1.getId());
        goodsValidator2.setPrice(2);
        goodsValidator2.setImageToUpload(new MockMultipartFile(
                "goodsTest2.jpg", "goodsTest2.jpg", "image/jpeg", "goodsTest2".getBytes()));
        goodsService.saveGoods(goodsValidator2);

        goodsValidator3.setName("goodsValidatorTest3");
        goodsValidator3.setDescription("goodsValidatorDescription3");
        goodsValidator3.setStatus(Status.AVAILABLE);
        goodsValidator3.setCatalogId(catalogValidator1.getId());
        goodsValidator3.setPrice(3);
        goodsValidator3.setImageToUpload(new MockMultipartFile(
                "goodsTest3.jpg", "goodsTest3.jpg", "image/jpeg", "goodsTest3".getBytes()));
        goodsService.saveGoods(goodsValidator3);

        goodsValidator4.setName("goodsValidatorTest4");
        goodsValidator4.setDescription("goodsValidatorDescription4");
        goodsValidator4.setStatus(Status.AVAILABLE);
        goodsValidator4.setCatalogId(catalogValidator1.getId());
        goodsValidator4.setPrice(4);
        goodsValidator4.setImageToUpload(new MockMultipartFile(
                "goodsTest4.jpg", "goodsTest4.jpg", "image/jpeg", "goodsTest4".getBytes()));
        goodsService.saveGoods(goodsValidator4);

        goodsValidator5.setName("goodsValidatorTest5");
        goodsValidator5.setDescription("goodsValidatorDescription5");
        goodsValidator5.setStatus(Status.AVAILABLE);
        goodsValidator5.setCatalogId(catalogValidator1.getId());
        goodsValidator5.setPrice(5);
        goodsValidator5.setImageToUpload(new MockMultipartFile("goodsTest5.jpg", "goodsTest5.jpg", "image/jpeg", "goodsTest5".getBytes()));
        goodsService.saveGoods(goodsValidator5);

        goodsValidator6.setName("goodsValidatorTest6");
        goodsValidator6.setDescription("goodsValidatorDescription6");
        goodsValidator6.setStatus(Status.AVAILABLE);
        goodsValidator6.setCatalogId(catalogValidator2.getId());
        goodsValidator6.setPrice(6);
        goodsValidator6.setImageToUpload(new MockMultipartFile("goodsTest6.jpg", "goodsTest6.jpg", "image/jpeg", "goodsTest6".getBytes()));
        goodsService.saveGoods(goodsValidator6);

        goodsValidator7.setName("goodsValidatorTest7");
        goodsValidator7.setDescription("goodsValidatorDescription7");
        goodsValidator7.setStatus(Status.AVAILABLE);
        goodsValidator7.setCatalogId(catalogValidator2.getId());
        goodsValidator7.setPrice(7);
        goodsValidator7.setImageToUpload(new MockMultipartFile("goodsTest7.jpg", "goodsTest7.jpg", "image/jpeg", "goodsTest7".getBytes()));
        goodsService.saveGoods(goodsValidator7);

        goodsValidator8.setName("goodsValidatorTest8");
        goodsValidator8.setDescription("goodsValidatorDescription8");
        goodsValidator8.setStatus(Status.UNAVAILABLE);
        goodsValidator8.setCatalogId(catalogValidator2.getId());
        goodsValidator8.setPrice(8);
        goodsValidator8.setImageToUpload(new MockMultipartFile("goodsTest8.jpg", "goodsTest8.jpg", "image/jpeg", "goodsTest8".getBytes()));
        goodsService.saveGoods(goodsValidator8);

        goodsValidators = new ArrayList<>();
        goodsValidators.addAll(goodsService.getAllCatalogGoodsByCatalogId(catalogValidator1.getId()));
        goodsValidators.addAll(goodsService.getAllCatalogGoodsByCatalogId(catalogValidator2.getId()));
    }

    @AfterEach
    public void afterEach() throws IOException {
        catalogService.deleteCatalogById(catalogValidator1.getId());
        catalogService.deleteCatalogById(catalogValidator2.getId());

        catalogValidator1 = new CatalogValidator();
        catalogValidator2 = new CatalogValidator();
        goodsValidator1 = new GoodsValidator();
        goodsValidator2 = new GoodsValidator();
        goodsValidator3 = new GoodsValidator();
        goodsValidator4 = new GoodsValidator();
        goodsValidator5 = new GoodsValidator();
        goodsValidator6 = new GoodsValidator();
        goodsValidator7 = new GoodsValidator();
        goodsValidator8 = new GoodsValidator();
        goodsValidators = null;
    }
}