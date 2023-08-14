package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.CatalogDTO;

import java.util.List;

/**
 * Uses for getting catalogs
 */
public interface UserCatalogService {
    public List<CatalogDTO> getAll();
}
