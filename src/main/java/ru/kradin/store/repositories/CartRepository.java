package ru.kradin.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kradin.store.models.Cart;
import ru.kradin.store.models.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}