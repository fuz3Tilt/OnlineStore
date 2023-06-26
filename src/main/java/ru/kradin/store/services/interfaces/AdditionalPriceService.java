package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.AdditionalPriceCreateDTO;

public interface AdditionalPriceService {
    public void add(AdditionalPriceCreateDTO additionalPriceCreateDTO);
    public void remove(Long id);
}
