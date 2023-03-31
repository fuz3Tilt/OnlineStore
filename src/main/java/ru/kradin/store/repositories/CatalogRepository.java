package ru.kradin.store.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.kradin.store.models.Catalog;

import java.util.List;
import java.util.Optional;

public interface CatalogRepository extends CrudRepository<Catalog, Integer> {
    Optional<Catalog> findByName(String name);

    @Query("select c from Catalog c order by c.name")
    List<Catalog> findByOrderByNameAsc();

}