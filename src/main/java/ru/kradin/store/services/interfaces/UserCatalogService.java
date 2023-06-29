package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.CatalogDTO;

import java.util.List;

public interface UserCatalogService {
    public List<CatalogDTO> getAll();
}
