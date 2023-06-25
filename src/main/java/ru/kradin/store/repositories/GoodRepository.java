package ru.kradin.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kradin.store.models.Good;

import java.util.List;

public interface GoodRepository extends JpaRepository<Good,Long> {
    List<Good> findByCatalog_IdOrderByNameAsc(Long id);
}