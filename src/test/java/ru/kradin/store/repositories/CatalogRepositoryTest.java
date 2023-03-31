package ru.kradin.store.repositories;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.kradin.store.models.Catalog;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CatalogRepositoryTest {

    @Autowired
    private CatalogRepository catalogRepository;

    @Test
    public void testFindByOrderByNameAsc() {
        Catalog catalog1 = new Catalog( "Catalog A");
        Catalog catalog2 = new Catalog( "Catalog C");
        Catalog catalog3 = new Catalog( "Catalog B");

        catalogRepository.save(catalog1);
        catalogRepository.save(catalog2);
        catalogRepository.save(catalog3);

        List<Catalog> catalogsInOrder = catalogRepository.findByOrderByNameAsc();
        assertThat(catalogsInOrder).hasSize(3);
        assertThat(catalogsInOrder.get(0).getName()).isEqualTo(catalog1.getName());
        assertThat(catalogsInOrder.get(1).getName()).isEqualTo(catalog3.getName());
        assertThat(catalogsInOrder.get(2).getName()).isEqualTo(catalog2.getName());

        for(Catalog catalog:catalogsInOrder)
            catalogRepository.delete(catalog);
    }
}