package ru.kradin.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kradin.store.models.Cart;
import ru.kradin.store.models.CartGoodQuantity;

import java.util.List;

public interface CartGoodQuantityRepository extends JpaRepository<CartGoodQuantity, Long> {
    List<CartGoodQuantity> findByCart(Cart cart);
}