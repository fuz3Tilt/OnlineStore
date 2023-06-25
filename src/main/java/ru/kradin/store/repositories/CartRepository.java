package ru.kradin.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kradin.store.models.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}