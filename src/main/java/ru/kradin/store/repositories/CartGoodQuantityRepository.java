package ru.kradin.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kradin.store.models.CartGoodQuantity;

public interface CartGoodQuantityRepository extends JpaRepository<CartGoodQuantity, Long> {
}