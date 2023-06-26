package ru.kradin.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kradin.store.models.OrderGoodQuantity;

public interface OrderGoodQuantityRepository extends JpaRepository<OrderGoodQuantity, Long> {
}