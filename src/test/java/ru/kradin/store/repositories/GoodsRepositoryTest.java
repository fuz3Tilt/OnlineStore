package ru.kradin.store.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.kradin.store.enums.Status;
import ru.kradin.store.models.Catalog;
import ru.kradin.store.models.Goods;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class GoodsRepositoryTest {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private CatalogRepository catalogRepository;

    @Test
    public void testAllMethods() {
        Catalog catalogToSave = new Catalog("Catalog");
        catalogRepository.save(catalogToSave);

        Catalog catalogFromDataBase = catalogRepository.findByOrderByNameAsc().get(0);
        assertThat(catalogFromDataBase).isNotNull();

        Goods goods1 = new Goods("A", catalogFromDataBase);
        goodsRepository.save(goods1);

        Goods goods2 = new Goods("D", catalogFromDataBase);
        goodsRepository.save(goods2);

        Goods goods3 = new Goods("C", catalogFromDataBase);
        goodsRepository.save(goods3);

        Goods goods4 = new Goods("B", catalogFromDataBase);
        goodsRepository.save(goods4);

        Goods goods5 = new Goods("E", catalogFromDataBase);
        goods5.setStatus(Status.UNAVAILABLE);
        goodsRepository.save(goods5);

        List<Goods> found1 = goodsRepository.findByCatalog_IdAndStatusOrderByNameAsc(catalogFromDataBase.getId(), Status.AVAILABLE);
        assertThat(found1).hasSize(4);
        assertThat(found1.get(0).getName()).isEqualTo(goods1.getName());
        assertThat(found1.get(0).getCatalog()).isEqualTo(catalogFromDataBase);
        assertThat(found1.get(1).getName()).isEqualTo(goods4.getName());
        assertThat(found1.get(1).getCatalog()).isEqualTo(catalogFromDataBase);
        assertThat(found1.get(2).getName()).isEqualTo(goods3.getName());
        assertThat(found1.get(2).getCatalog()).isEqualTo(catalogFromDataBase);
        assertThat(found1.get(3).getName()).isEqualTo(goods2.getName());
        assertThat(found1.get(3).getCatalog()).isEqualTo(catalogFromDataBase);

        List<Goods> found2 = goodsRepository.findByStatusOrderByNameAsc(Status.UNAVAILABLE);
        assertThat(found2.get(0).getName()).isEqualTo(goods5.getName());
        assertThat(found2.get(0).getCatalog()).isEqualTo(catalogFromDataBase);

        List<Goods> found3 = goodsRepository.findByCatalog_IdOrderByNameAsc(catalogFromDataBase.getId());
        assertThat(found3).hasSize(5);
        assertThat(found3.get(0).getName()).isEqualTo(goods1.getName());
        assertThat(found3.get(0).getCatalog()).isEqualTo(catalogFromDataBase);
        assertThat(found3.get(1).getName()).isEqualTo(goods4.getName());
        assertThat(found3.get(1).getCatalog()).isEqualTo(catalogFromDataBase);
        assertThat(found3.get(2).getName()).isEqualTo(goods3.getName());
        assertThat(found3.get(2).getCatalog()).isEqualTo(catalogFromDataBase);
        assertThat(found3.get(3).getName()).isEqualTo(goods2.getName());
        assertThat(found3.get(3).getCatalog()).isEqualTo(catalogFromDataBase);
        assertThat(found3.get(4).getName()).isEqualTo(goods5.getName());
        assertThat(found3.get(4).getCatalog()).isEqualTo(catalogFromDataBase);

        catalogRepository.delete(catalogFromDataBase);
        List<Goods> found4 = goodsRepository.findByCatalog_IdOrderByNameAsc(catalogFromDataBase.getId());
        assertThat(found4).hasSize(0);
    }
}