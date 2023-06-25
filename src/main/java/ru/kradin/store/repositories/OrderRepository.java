package ru.kradin.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kradin.store.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}