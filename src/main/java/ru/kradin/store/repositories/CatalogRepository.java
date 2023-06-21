package ru.kradin.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kradin.store.models.Catalog;

public interface CatalogRepository extends JpaRepository<Catalog,Long> {

}