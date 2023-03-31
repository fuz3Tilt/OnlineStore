package ru.kradin.store.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.kradin.store.enums.Status;
import ru.kradin.store.models.Goods;

import java.util.List;
import java.util.Optional;

public interface GoodsRepository extends CrudRepository<Goods, Integer> {
    Optional<Goods> findByName(String name);

    Goods findByIdAndStatus(int id, Status status);

    List<Goods> findByCatalog_IdAndStatusOrderByNameAsc(int id, Status status);

    List<Goods> findByCatalog_IdOrderByNameAsc(int id);

    List<Goods> findByStatusOrderByNameAsc(Status status);
}