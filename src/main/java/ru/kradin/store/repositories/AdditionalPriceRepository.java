package ru.kradin.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kradin.store.models.AdditionalPrice;

public interface AdditionalPriceRepository extends JpaRepository<AdditionalPrice, Long> {
}