package ru.kradin.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kradin.store.models.Order;
import ru.kradin.store.models.OrderGoodQuantity;

import java.util.List;

public interface OrderGoodQuantityRepository extends JpaRepository<OrderGoodQuantity, Long> {
    List<OrderGoodQuantity> findByOrder(Order order);
}