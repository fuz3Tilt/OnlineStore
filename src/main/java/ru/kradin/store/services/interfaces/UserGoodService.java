package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.GoodDTO;

import java.util.List;

public interface UserGoodService {
    public List<GoodDTO> getAll();
    public List<GoodDTO> getByCatalogId(Long catalogId);
}
