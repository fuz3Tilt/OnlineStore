package ru.kradin.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kradin.store.models.Good;

public interface GoodRepository extends JpaRepository<Good,Long> {

}