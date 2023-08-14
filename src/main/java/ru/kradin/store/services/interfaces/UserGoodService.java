package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.GoodDTO;

import java.util.List;

/**
 * Uses for getting goods
 */
public interface UserGoodService {
    public List<GoodDTO> getAll();
    public List<GoodDTO> getByCatalogId(Long catalogId);
}
